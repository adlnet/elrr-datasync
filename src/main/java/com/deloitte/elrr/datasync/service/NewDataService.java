package com.deloitte.elrr.datasync.service;

import com.deloitte.elrr.datasync.dto.AuditRecord;
import com.deloitte.elrr.datasync.dto.LearnerChange;
import com.deloitte.elrr.datasync.dto.MessageVO;
import com.deloitte.elrr.datasync.dto.UserCourse;
import com.deloitte.elrr.datasync.entity.SyncRecord;
import com.deloitte.elrr.datasync.entity.SyncRecordDetail;
import com.deloitte.elrr.datasync.jpa.service.SyncRecordDetailService;
import com.deloitte.elrr.datasync.jpa.service.SyncRecordService;
import com.deloitte.elrr.datasync.producer.KafkaProducer;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.util.ArrayList;
import java.util.List;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
@Slf4j
public class NewDataService {
  /**
   *
   */
  @Autowired
  private KafkaProducer kafkaProducer;
  /**
   *
   */
  @Autowired
  private SyncRecordService syncRecordService;
  /**
   *
   */
  @Autowired
  private SyncRecordDetailService syncRecordDetailService;
  /**
   *
   */
  private ObjectMapper mapper = new ObjectMapper();

  /**
   * 1. Retrieve all the records that are with INSERTED status
   * 2. Create a LearnerChange Json
   * 3. send the message to Kafka
   * 4. update the records SyncRecord and SyncRecordDetails
   * to SUCCCESS/INSERTED status
   *
   */
  public void process() {
    log.info("Inside NewDataService");
    // Unprocessed synchrecord.recordStatus=inserted
    List<SyncRecord> syncList = getUnprocessedRecords();
    log.info("==> Unprocessed synch records = " + syncList.size());  // PHL
    for (SyncRecord syncRecord : syncList) {
      List<SyncRecordDetail> details = null;
      try {
        details =
          syncRecordDetailService.findBySyncRecordId(
            syncRecord.getSyncRecordId()
          );
        log.info("==> details size = " + details.size());  // PHL
        MessageVO kafkaMessage = createKafkaJsonMessage(details);
        sendToKafka(kafkaMessage);
        // Processed synchrecord.recordStatus=success
        syncRecord.setRecordStatus("SUCCESS");
        syncRecordService.save(syncRecord);
        for (SyncRecordDetail syncRecordDetails : details) {
          syncRecordDetails.setRecordStatus("SUCCESS");
          syncRecordDetailService.save(syncRecordDetails);
        }
      } catch (Exception e) {
        // In case of exception change status back to inserted so that
        // they will be picked again next time and processed
        log.error("Exception in processing " + e.getMessage());  
        syncRecord.setRecordStatus("INSERTED");
        syncRecordService.save(syncRecord);
        for (SyncRecordDetail syncRecordDetail : details) {
          syncRecordDetail.setRecordStatus("INSERTED");
          syncRecordDetailService.save(syncRecordDetail);
        }
      }
    }
  }

  private void sendToKafka(final MessageVO message) {
    kafkaProducer.sendMessage(message);
  }

  private MessageVO createKafkaJsonMessage(final List<SyncRecordDetail> details)
    throws JsonProcessingException {
    LearnerChange learnerChange = new LearnerChange();
    List<UserCourse> userCourses = new ArrayList<>();
    AuditRecord auditRecord = new AuditRecord();
    List<Long> detailIds = new ArrayList<>();
    for (SyncRecordDetail detail : details) {
      LearnerChange temp = getLearner(detail);
      learnerChange.setContactEmailAddress(temp.getContactEmailAddress());
      learnerChange.setName(temp.getName());
      //there will be only one course for each SyncRecordDetails
      userCourses.add(temp.getCourses().get(0));
      detailIds.add(detail.getSyncRecordDetailId());
      auditRecord.setAuditId(detail.getSyncRecordId());
    }

    learnerChange.setCourses(userCourses);
    auditRecord.setAuditDeailIds(detailIds);

    MessageVO vo = new MessageVO();
    vo.setAuditRecord(auditRecord);
    vo.setLearnerChange(learnerChange);
    return vo;
  }

  private LearnerChange getLearner(final SyncRecordDetail detail)
    throws JsonProcessingException {
    return mapper.readValue(detail.getLearner(), LearnerChange.class);
  }

  private List<SyncRecord> getUnprocessedRecords() {
    List<SyncRecord> syncList = syncRecordService.findUnprocessed();
    log.info("unprocessed list " + syncList.size());
    return syncList;
  }
}
