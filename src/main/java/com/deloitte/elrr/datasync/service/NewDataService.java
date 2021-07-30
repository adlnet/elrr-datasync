package com.deloitte.elrr.datasync.service;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.deloitte.elrr.datasync.dto.AuditRecord;
import com.deloitte.elrr.datasync.dto.LearnerChange;
import com.deloitte.elrr.datasync.dto.MessageVO;
import com.deloitte.elrr.datasync.dto.UserCourse;
import com.deloitte.elrr.datasync.entity.SyncRecord;
import com.deloitte.elrr.datasync.entity.SyncRecordDetails;
import com.deloitte.elrr.datasync.jpa.service.SyncRecordDetailsService;
import com.deloitte.elrr.datasync.jpa.service.SyncService;
import com.deloitte.elrr.datasync.producer.KafkaProducer;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import lombok.extern.slf4j.Slf4j;

@Service
@Slf4j
public class NewDataService {

	@Autowired
	KafkaProducer kafkaProducer;
	
	@Autowired
	SyncService syncService;
	
	@Autowired
	SyncRecordDetailsService syncRecordDetailsService;
	
	ObjectMapper mapper = new ObjectMapper();
	
	/*
	 * 1. Retrieve all the records that are with INSERTED status
	 * 2. Create a LearnerChange Json
	 * 3. send the message to Kafka
	 * 4. update the records SyncRecord and SyncRecordDetails to SUCCCESS/INSERTED status
	 * 
	 */
	public void process() {
		
		log.info("Inside NewDataService");
		List<SyncRecord> syncList = getUnprocessedRecords();
		log.info("inside getNewData1 "+syncList.size());
		for (SyncRecord syncRecord: syncList) {
			List<SyncRecordDetails> details = null;
			try {
				details = syncRecordDetailsService.findBySyncRecordId(syncRecord.getSyncRecordId());
				MessageVO kakfaMessage = createKafkaJsonMessage(details);
				sendToKafka(kakfaMessage);
				syncRecord.setSyncRecordStatus("SUCCESS");
				syncService.save(syncRecord);
				for (SyncRecordDetails syncRecordDetails:details) {
					syncRecordDetails.setSyncDetailsStatus("SUCCESS");
					syncRecordDetailsService.save(syncRecordDetails);
				}
			} catch(Exception e) {
				//In case of exception change status back to inserted so that 
				// they will be picked again next time and processed
				syncRecord.setSyncRecordStatus("INSERTED");
				syncService.save(syncRecord);
				for (SyncRecordDetails syncRecordDetails:details) {
					syncRecordDetails.setSyncDetailsStatus("INSERTED");
					syncRecordDetailsService.save(syncRecordDetails);
				}
			}
			
		}
	}

	private void sendToKafka(MessageVO message) {
 		kafkaProducer.sendMessage(message);
	}
	
	private MessageVO createKafkaJsonMessage(List<SyncRecordDetails> details) throws JsonMappingException, JsonProcessingException {
		LearnerChange learnerChange = new LearnerChange();
		List<UserCourse> userCourses = new ArrayList<>();
		AuditRecord auditRecord = new AuditRecord();
		List<Long> detailIds = new ArrayList<>();
		for (SyncRecordDetails detail: details) {
			LearnerChange temp = getLearner(detail);
			learnerChange.setContactEmailAddress(temp.getContactEmailAddress());
			learnerChange.setName(temp.getName());
			//there will be only one course for each SyncRecordDetails
			userCourses.add(temp.getCourses().get(0));
			detailIds.add(detail.getSyncRecordDetailsId());
			auditRecord.setAuditId(detail.getSyncRecordId());
		}
		
		learnerChange.setCourses(userCourses);
		auditRecord.setAuditDeailIds(detailIds);
		
		MessageVO vo = new MessageVO();
		vo.setAuditRecord(auditRecord);
		vo.setLearnerChange(learnerChange);
		return vo;
	}
	
	private LearnerChange getLearner(SyncRecordDetails  detail) throws JsonMappingException, JsonProcessingException {
		return mapper.readValue(detail.getLearner(), LearnerChange.class);
	}

	private List<SyncRecord> getUnprocessedRecords() {
		List<SyncRecord> syncList = syncService.findUnprocessed();
		log.info("unprocessed list "+syncList.size());
		return syncList;
	}
	
}
