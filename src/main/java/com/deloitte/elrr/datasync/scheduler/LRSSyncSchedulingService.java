package com.deloitte.elrr.datasync.scheduler;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import com.deloitte.elrr.datasync.dto.ElrrStatement;
import com.deloitte.elrr.datasync.dto.LearnerChange;
import com.deloitte.elrr.datasync.dto.UserCourse;
import com.deloitte.elrr.datasync.entity.Imports;
import com.deloitte.elrr.datasync.entity.ImportsDetails;
import com.deloitte.elrr.datasync.entity.SyncRecord;
import com.deloitte.elrr.datasync.entity.SyncRecordDetails;
import com.deloitte.elrr.datasync.jpa.service.ImportsDetailsService;
import com.deloitte.elrr.datasync.jpa.service.ImportsService;
import com.deloitte.elrr.datasync.jpa.service.SyncRecordDetailsService;
import com.deloitte.elrr.datasync.jpa.service.SyncService;
import com.deloitte.elrr.datasync.service.LRSService;
import com.deloitte.elrr.datasync.service.NewDataService;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import gov.adlnet.xapi.model.Activity;
import gov.adlnet.xapi.model.Statement;
import gov.adlnet.xapi.model.StatementResult;
import lombok.extern.slf4j.Slf4j;


@Service
@Slf4j
public class LRSSyncSchedulingService {

	String LRS_NAME = "Deloitte LRS";
 
	@Autowired
	LRSService lrsService;
	
	@Autowired
	NewDataService newDataService;
	
	@Autowired
	ImportsService importsService;
	

	@Autowired
	ImportsDetailsService importsDetailsService;
	
	@Autowired
	SyncService syncService;

	@Autowired
	SyncRecordDetailsService syncRecordDetailsService;
	
	ObjectMapper mapper = new ObjectMapper();
	
	@Scheduled(cron="${cronExpression}")
	/*
	 * 1. connect to db and get Last sync date
	 * 2. Update the last sync record with the status INPROCESS and update to current time
	 * 3. make a call to LRS and get the data
	 * 4. insert Sync Records and Sync record details table with INSERTED status
	 * 5. updated Import table and insert to Imports detail table with SUCCESS/FAILED status
	 * 6. Invoke New Processor to processed unprocessed records
	 * 
	 */
	public void run() {
		
		log.info("**inside schedule method");
		Imports imports = getLRSImports();
		
		//StatementResult result = lrsService.process(imports.getImportStartDate(),imports.getImportEndDate());
	
 		if (imports != null) {
 			try {
 				updateImportsInProcess(imports);
 				ElrrStatement[] result = lrsService.process(imports.getImportStartDate(),imports.getImportEndDate());
 				if (result != null && result.length > 0) {
 					insertSyncRecords(result,imports);
 				}
 			} catch (Exception e) {
 				log.error("LRS Sync failed "+e.getMessage());
 				updateImportsFailed(imports);
 			}
			//Reason this is out of try catch is
 			// even if the LRS sync is failed but if any of the unprocessed messages sitting in DB 
 			// they can be processed
 			newDataService.process();
		} else {
			log.error("No record was defined for LRS in Imports table");
		}

	}
 
	private void insertSyncRecords(ElrrStatement[] list, Imports imports) {
		int success = 0;
		int failed = 0;
		int total = list.length;
		ImportsDetails importDetails = insertImportsDetails(total, success,failed, imports);
		for (ElrrStatement statement: list) {
			try {
				String key = statement.getActor();
				SyncRecord sync = syncService.findExistingRecord(key);
				if (sync == null) {
					sync = syncService.createSyncRecord(key,importDetails.getImportdetailsid());
				}
				createSyncRecordDetails(sync, statement);
				success++;
			} catch(Exception e) {
				log.error("Exception in processing "+e.getMessage());
				failed++;
			}
		}
		updateImportsDetails(total, success,failed, importDetails);
		updateImportsSuccess(imports);
	}


	private void updateImportsDetails(int total, int success, int failed, ImportsDetails importDetails) {
		importDetails.setFailedRecords(failed);
		importDetails.setTotalRecords(total);
		importDetails.setSuccessRecords(success);
		importDetails.setImportStatus("SUCCESS");
		importsDetailsService.save(importDetails);
	}

	private void createSyncRecordDetails(SyncRecord syncRecord, ElrrStatement statement) throws JsonProcessingException {
		
		SyncRecordDetails syncRecordDetails = new SyncRecordDetails();
		syncRecordDetails.setSyncRecordId(syncRecord.getSyncRecordId());
		LearnerChange learnerChange = getLearnerChange(statement);
		syncRecordDetails.setPayload(getJson(statement));
		syncRecordDetails.setLearner(getJson(learnerChange));
		syncRecordDetails.setSyncDetailsStatus("INSERTED");
		syncRecordDetailsService.save(syncRecordDetails);
	}
	
	private ImportsDetails insertImportsDetails(int total, int success, int failed, Imports imports) {
		ImportsDetails importDetails = new ImportsDetails();
		importDetails.setImportsId(imports.getImportid());
		importDetails.setImportBeginTime(imports.getImportStartDate());
		importDetails.setImportEndTime(imports.getImportEndDate());
		importDetails.setFailedRecords(failed);
		importDetails.setTotalRecords(total);
		importDetails.setSuccessRecords(success);
		importDetails.setImportStatus("INPROCESS");
		importsDetailsService.save(importDetails);
		return importDetails;
	}
	
	private String getJson(Object object) throws JsonProcessingException {
		return mapper.writeValueAsString(object);
	}

	private LearnerChange getLearnerChange(ElrrStatement statement) {
		LearnerChange learnerChange = new LearnerChange();
		List<UserCourse> userCourses = new ArrayList<>();
		learnerChange.setContactEmailAddress(statement.getActor());
		learnerChange.setName(statement.getActorName());
		UserCourse course = new UserCourse();
		//Activity act = (Activity) statement.getObject();
		//course.setCourseId(act.getId());
		//course.setCourseName(act.getDefinition().getName().get("en-US"));
		//course.setUserCourseStatus(statement.getVerb().getDisplay().get("en-US"));
		course.setCourseId(statement.getActivity());
		course.setCourseName(statement.getCourseName());
		course.setUserCourseStatus(statement.getVerb());
		userCourses.add(course);
		learnerChange.setCourses(userCourses);
		return learnerChange;

	}


	private void updateImportsInProcess(Imports imports) {
		//if previous run was success, we will update the dates
		//if not, we just re run again with same old dates
		if (imports.getRecordStatus().equals("SUCCESS")) {
	 		imports.setImportStartDate(imports.getImportEndDate());
	 		imports.setImportEndDate(getEndDate());
	 	}
 		imports.setRecordStatus("INPROCESS");
 		importsService.save(imports);
	}

	private void updateImportsSuccess(Imports imports) {
		imports.setRecordStatus("SUCCESS");
 		importsService.save(imports);
		
	}
	
	private void updateImportsFailed(Imports imports) {
		imports.setRecordStatus("FAILED");
 		importsService.save(imports);
		
	}

 	private Timestamp getEndDate() {
 		Timestamp timestamp = new Timestamp(System.currentTimeMillis());
		return timestamp;
	}
	
	private Imports getLRSImports() {
		Imports imports = importsService.findByName(LRS_NAME);
		return imports;
	}
}
