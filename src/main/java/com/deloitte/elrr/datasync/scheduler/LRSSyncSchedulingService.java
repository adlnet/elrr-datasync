package com.deloitte.elrr.datasync.scheduler;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import com.deloitte.elrr.datasync.dto.LearnerChange;
import com.deloitte.elrr.datasync.dto.UserCourse;
import com.deloitte.elrr.datasync.entity.Import;
import com.deloitte.elrr.datasync.entity.ImportDetail;
import com.deloitte.elrr.datasync.entity.SyncRecord;
import com.deloitte.elrr.datasync.entity.SyncRecordDetail;
import com.deloitte.elrr.datasync.jpa.service.ImportDetailService;
import com.deloitte.elrr.datasync.jpa.service.ImportService;
import com.deloitte.elrr.datasync.jpa.service.SyncRecordDetailService;
import com.deloitte.elrr.datasync.jpa.service.SyncRecordService;
import com.deloitte.elrr.datasync.service.LRSService;
import com.deloitte.elrr.datasync.service.NewDataService;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.yetanalytics.xapi.model.Activity;
import com.yetanalytics.xapi.model.ActivityDefinition;
import com.yetanalytics.xapi.model.Agent;
import com.yetanalytics.xapi.model.LangMap;
import com.yetanalytics.xapi.model.Statement;
import com.yetanalytics.xapi.model.Verb;

import lombok.extern.slf4j.Slf4j;

@Service
@Slf4j
public class LRSSyncSchedulingService {

  private static String lrsName = "Deloitte LRS";
  private static String success = "SUCCESS";

  @Autowired private LRSService lrsService;

  @Autowired private NewDataService newDataService;

  @Autowired private ImportService importService;

  @Autowired private ImportDetailService importDetailService;

  @Autowired private SyncRecordService syncService;

  @Autowired private SyncRecordDetailService syncRecordDetailService;

  private ObjectMapper mapper = new ObjectMapper();

  @Scheduled(cron = "${cronExpression}")
  /*
   * 1. Connect to db and get Last sync date.
   *
   * 2. Update the last sync record with the status INPROCESS and update to current time.
   *
   * 3. Make a call to LRS and get the data.
   *
   * 4. Insert Sync Records and Sync record details table with INSERTED status.
   *
   * 5. Update Import table and insert to Imports detail table with SUCCESS/FAILED status.
   *
   * 6. Invoke New Processor to process unprocessed records.
   *
   */
  public void run() {

    log.info("**inside schedule method");
    Import importRecord = getLRSImport();

    Statement[] result = null;
    int total = 0;
    int successCount = 0;
    int failedCount = 0;

    if (importRecord != null) {

      try {

        updateImportInProcess(importRecord);

        // Make call to LRSService.invokeLRS(final Timestamp startDate)
        result = lrsService.process(importRecord.getImportStartDate());

        ImportDetail importDetail = null;

        if (result != null && result.length > 0) {

          for (Statement statement : result) {
            importDetail = insertImportDetail(result.length, 0, 0, importRecord);
            successCount = insertSyncRecord(statement, importDetail);
            failedCount = 1 - successCount;
            total = successCount + failedCount;
            updateImportDetail(total, successCount, failedCount, importDetail);
            updateImportDetailSuccess(importDetail);
          }
        }

        updateImportSuccess(importRecord);

      } catch (Exception e) {
        log.error("LRS Sync failed " + e.getMessage());
        updateImportFailed(importRecord);
        e.getStackTrace();
      }
      // The reason this is out of the try catch block is that even if the LRS sync is failed
      // but if any of there are  unprocessed messages sitting in the DB, they can be processed.
      newDataService.process(result);
    } else {
      log.error("No record was defined for LRS in Imports table");
    }
  }

  /**
   *
   * @param Statement
   * @param importDetail
   * @return successCount
   */
  private int insertSyncRecord(final Statement statement, final ImportDetail importDetail) {
    int successCount = 0;
    try {

      String key = statement.getId().toString();
      SyncRecord sync = syncService.findExistingRecord(key);

      if (sync == null) {
        sync = syncService.createSyncRecord(key, importDetail.getImportdetailId());
      }

      createSyncRecordDetail(sync, statement);
      successCount++;

    } catch (Exception e) {
      log.error("Exception in processing " + e.getMessage());
      e.getStackTrace();
    }

    return successCount;
  }

  /**
   * @param total
   * @param newsuccess
   * @param failed
   * @param importDetail
   */
  private void updateImportDetail(
      final int total, final int newsuccess, final int failed, final ImportDetail importDetail) {
    importDetail.setFailedRecords(failed);
    importDetail.setTotalRecords(total);
    importDetail.setSuccessRecords(newsuccess);
    importDetailService.save(importDetail);
  }

  /**
   *
   * @param syncRecord
   * @param statement
   * @throws JsonProcessingException
   */
  private void createSyncRecordDetail(final SyncRecord syncRecord, final Statement statement)
      throws JsonProcessingException {
    SyncRecordDetail syncRecordDetail = new SyncRecordDetail();
    syncRecordDetail.setSyncRecordId(syncRecord.getSyncRecordId());
    LearnerChange learnerChange = getLearnerChange(statement);
    syncRecordDetail.setPayload(getJson(statement));
    syncRecordDetail.setLearner(getJson(learnerChange));
    syncRecordDetail.setRecordStatus("INSERTED");
    syncRecordDetailService.save(syncRecordDetail);
  }

  /**
   * @param total
   * @param newsuccess
   * @param failed
   * @param importRecord
   * @return ImportDetail
   */
  private ImportDetail insertImportDetail(
      final int total, final int newsuccess, final int failed, final Import importRecord) {

    ImportDetail importDetail = new ImportDetail();
    importDetail.setImportId(importRecord.getImportId());
    importDetail.setImportBeginTime(importRecord.getImportStartDate());
    importDetail.setImportEndTime(importRecord.getImportEndDate());
    importDetail.setFailedRecords(failed);
    importDetail.setTotalRecords(total);
    importDetail.setSuccessRecords(newsuccess);
    importDetail.setRecordStatus("INPROCESS");
    importDetailService.save(importDetail);
    return importDetail;
  }

  /**
   * @param object
   * @return String
   * @throws JsonProcessingException
   */
  private String getJson(final Object object) throws JsonProcessingException {
    return mapper.writeValueAsString(object);
  }

  /**
   *
   * @param statement
   * @return LearnerChange
   */
  private LearnerChange getLearnerChange(final Statement statement) {

    // Parse xAPI Statement
    // Actor
    String actorName = "";
    String actorEmail = "";

    Agent actor = (Agent) statement.getActor();

    if (actor != null) {
      actorName = actor.getName();
      actorEmail = actor.getMbox();
    }

    // Verb
    String verbDisplay = "";

    Verb verb = statement.getVerb();

    if (verb != null) {
      verbDisplay = verb.getDisplay().get("en-us");
    }

    // Activity
    Activity object = (Activity) statement.getObject();

    // Activity name
    String activityName = "";
    String nameLangCode = "";

    ActivityDefinition activityDefenition = object.getDefinition();
    LangMap nameLangMap = activityDefenition.getName();

    if (nameLangMap != null) {
      Set<String> nameLangCodes = nameLangMap.getLanguageCodes();
      nameLangCode = nameLangCodes.iterator().next();
      activityName = activityDefenition.getName().get(nameLangCode);
    }

    // Activity Description
    String activityDescription = "";
    String langCode = "";

    LangMap descLangMap = activityDefenition.getDescription();

    if (descLangMap != null) {
      Set<String> descLangCodes = descLangMap.getLanguageCodes();
      langCode = descLangCodes.iterator().next();
      activityDescription = activityDefenition.getDescription().get(langCode);
    }

    LearnerChange learnerChange = new LearnerChange();
    List<UserCourse> userCourses = new ArrayList<>();

    // Use xAPI Statement values
    learnerChange.setContactEmailAddress(actorEmail);
    learnerChange.setName(actorName);
    UserCourse course = new UserCourse();
    course.setCourseId(activityName);
    course.setCourseName(activityDescription);
    course.setUserCourseStatus(verbDisplay);
    userCourses.add(course);
    learnerChange.setCourses(userCourses);

    return learnerChange;
  }

  /**
   * @param imports
   */
  private void updateImportInProcess(final Import imports) {
    // If the previous run was successful, we will update the dates.
    // If not, we just re run again with same old dates.
    if (imports.getRecordStatus().equals(success)) {
      imports.setImportStartDate(imports.getImportEndDate());
      imports.setImportEndDate(getEndDate());
    }
    imports.setRecordStatus("INPROCESS");
    importService.save(imports);
  }

  /**
   * @param imports
   */
  private void updateImportSuccess(final Import imports) {
    imports.setRecordStatus(success);
    importService.save(imports);
  }

  /**
   * @param ImportDetail
   */
  private void updateImportDetailSuccess(final ImportDetail importDetail) {
    importDetail.setRecordStatus(success);
    importDetailService.save(importDetail);
  }

  /**
   * @param Import
   */
  private void updateImportFailed(final Import importRecord) {
    importRecord.setRecordStatus("FAILED");
    importService.save(importRecord);
  }

  /**
   * @return Timestamp
   */
  private Timestamp getEndDate() {
    return new Timestamp(System.currentTimeMillis());
  }

  /**
   * @return Import
   */
  private Import getLRSImport() {
    return importService.findByName(lrsName);
  }
}
