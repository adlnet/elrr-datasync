package com.deloitte.elrr.datasync.scheduler;

import java.sql.Timestamp;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.deloitte.elrr.datasync.entity.Import;
import com.deloitte.elrr.datasync.entity.ImportDetail;
import com.deloitte.elrr.datasync.entity.SyncRecord;
import com.deloitte.elrr.datasync.entity.SyncRecordDetail;
import com.deloitte.elrr.datasync.exception.DatasyncException;
import com.deloitte.elrr.datasync.exception.RunTimeServiceException;
import com.deloitte.elrr.datasync.jpa.service.ImportDetailService;
import com.deloitte.elrr.datasync.jpa.service.ImportService;
import com.deloitte.elrr.datasync.jpa.service.SyncRecordDetailService;
import com.deloitte.elrr.datasync.jpa.service.SyncRecordService;
import com.deloitte.elrr.datasync.service.LRSService;
import com.deloitte.elrr.datasync.service.NewDataService;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.yetanalytics.xapi.model.Statement;

import lombok.extern.slf4j.Slf4j;

@Service
@Slf4j
public class LRSSyncSchedulingService {

  @Autowired private LRSService lrsService;

  @Autowired private NewDataService newDataService;

  @Autowired private ImportService importService;

  @Autowired private ImportDetailService importDetailService;

  @Autowired private SyncRecordService syncService;

  @Autowired private SyncRecordDetailService syncRecordDetailService;

  @Value("${initial.date}")
  private Timestamp initialDate;

  private ObjectMapper mapper = new ObjectMapper();

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
  @Scheduled(cron = "${cronExpression}")
  @Transactional
  public void run() {

    log.info("**inside LRS schedule method.");
    Import importRecord = importService.findByName(StatusConstants.LRSNAME);

    try {

      // If no import record
      if (importRecord == null) {
        importRecord = createImport();
      }

      Statement[] result = null;
      int total = 0;
      int successCount = 0;
      int failedCount = 0;

      updateImportInProcess(importRecord);

      // Make call to LRSService.invokeLRS(final Timestamp startDate)
      result = lrsService.process(importRecord.getImportStartDate());

      if (result != null && result.length > 0) {

        for (Statement statement : result) {
          ImportDetail importDetail = insertImportDetail(result.length, 0, 0, importRecord);
          successCount = insertSyncRecord(statement, importDetail);
          failedCount = 1 - successCount;
          total = successCount + failedCount;

          // Update import detail
          updateImportDetail(
              total, successCount, failedCount, StatusConstants.SUCCESS, importDetail);
        }
      }

      // Update import status
      importRecord.setRecordStatus(StatusConstants.SUCCESS);
      importService.save(importRecord);

      // Process unprocessed
      newDataService.process(result);

    } catch (DatasyncException | RunTimeServiceException e) {
      log.error("LRS Sync failed - " + e.getMessage());
      e.printStackTrace();
      throw e;
    }
  }

  /**
   * @param Statement
   * @param importDetail
   * @return successCount
   * @throws DatasyncException
   */
  private int insertSyncRecord(final Statement statement, final ImportDetail importDetail) {

    int successCount = 0;

    String key = statement.getId().toString();
    SyncRecord sync = syncService.findExistingRecord(key);

    if (sync == null) {
      sync = syncService.createSyncRecord(key, importDetail.getImportdetailId());
    }

    createSyncRecordDetail(sync, statement);
    successCount++;

    return successCount;
  }

  /**
   * @param total
   * @param newsuccess
   * @param failed
   * @param status
   * @param importDetail
   */
  private ImportDetail updateImportDetail(
      final int total,
      final int newsuccess,
      final int failed,
      final String status,
      ImportDetail importDetail) {

    log.info("Updating import detail.");

    importDetail.setFailedRecords(failed);
    importDetail.setTotalRecords(total);
    importDetail.setSuccessRecords(newsuccess);
    importDetail.setRecordStatus(status);

    try {
      importDetailService.update(importDetail);
    } catch (RunTimeServiceException e) {
      e.printStackTrace();
      throw new DatasyncException("Update import detail failed.");
    }
    return importDetail;
  }

  /**
   * @param syncRecord
   * @param statement
   */
  private void createSyncRecordDetail(final SyncRecord syncRecord, final Statement statement) {
    log.info("Creating syncrecord detail.");
    SyncRecordDetail syncRecordDetail = new SyncRecordDetail();
    syncRecordDetail.setSyncRecordId(syncRecord.getSyncRecordId());
    syncRecordDetail.setRecordStatus(StatusConstants.INSERTED);
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

    log.info("Creating import detail.");
    ImportDetail importDetail = new ImportDetail();
    importDetail.setImportId(importRecord.getImportId());
    importDetail.setImportBeginTime(importRecord.getImportStartDate());
    importDetail.setImportEndTime(importRecord.getImportEndDate());
    importDetail.setFailedRecords(failed);
    importDetail.setTotalRecords(total);
    importDetail.setSuccessRecords(newsuccess);
    importDetail.setRecordStatus(StatusConstants.INPROCESS);
    importDetailService.save(importDetail);
    return importDetail;
  }

  /**
   * @param imports
   */
  @Transactional
  private void updateImportInProcess(Import imports) {

    // If the previous run was successful, we will update the dates.
    // If not, we just re run again with same old dates.

    log.info("Updating import.");

    try {
      if (imports.getRecordStatus().equals(StatusConstants.SUCCESS)) {
        imports.setImportStartDate(imports.getImportEndDate());
        imports.setImportEndDate(new Timestamp(System.currentTimeMillis()));
      }

      imports.setRecordStatus(StatusConstants.INPROCESS);
      importService.update(imports);

    } catch (RunTimeServiceException e) {
      throw e;
    }
  }

  public Import createImport() {
    log.info("Crerating new import.");
    Import importRecord = new Import();
    importRecord.setRecordStatus(StatusConstants.SUCCESS);
    importRecord.setImportName(StatusConstants.LRSNAME);
    importRecord.setImportStartDate(initialDate);
    importRecord.setImportEndDate(initialDate);
    importService.save(importRecord);
    return importRecord;
  }
}
