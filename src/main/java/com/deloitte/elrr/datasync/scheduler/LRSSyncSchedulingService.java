package com.deloitte.elrr.datasync.scheduler;

import java.sql.Timestamp;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import com.deloitte.elrr.datasync.entity.Import;
import com.deloitte.elrr.datasync.entity.ImportDetail;
import com.deloitte.elrr.datasync.entity.SyncRecord;
import com.deloitte.elrr.datasync.entity.SyncRecordDetail;
import com.deloitte.elrr.datasync.exception.DatasyncException;
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
  public void run() {

    log.info("**inside schedule method");
    Import importRecord = importService.findByName(StatusConstants.LRSNAME);

    // If no import record
    if (importRecord == null) {
      importRecord = createImport();
    }

    Statement[] result = null;
    int total = 0;
    int successCount = 0;
    int failedCount = 0;

    if (importRecord != null) {

      try {

        updateImportInProcess(importRecord);

        // Make call to LRSService.invokeLRS(final Timestamp startDate)
        result = lrsService.process(importRecord.getImportStartDate());

        if (result != null && result.length > 0) {

          for (Statement statement : result) {
            ImportDetail importDetail = insertImportDetail(result.length, 0, 0, importRecord);
            successCount = insertSyncRecord(statement, importDetail);
            failedCount = 1 - successCount;
            total = successCount + failedCount;
            updateImportDetail(total, successCount, failedCount, importDetail);
            // Update import detail status
            importDetail.setRecordStatus(StatusConstants.SUCCESS);
            importDetailService.save(importDetail);
          }
        }

        // Update import status
        importRecord.setRecordStatus(StatusConstants.SUCCESS);
        importService.save(importRecord);

      } catch (Exception e) {
        log.error("LRS Sync failed - " + e.getMessage());
        e.printStackTrace();
        // Update import status
        importRecord.setRecordStatus(StatusConstants.FAILED);
        importService.save(importRecord);
      }

      // The reason this is out of the try catch block is that even if the LRS sync is failed
      // but if any of there are  unprocessed messages sitting in the DB, they can be processed.
      try {
        newDataService.process(result);
      } catch (DatasyncException e) {
        log.error("Error processing statements - " + e.getMessage());
        e.printStackTrace();
      }

    } else {
      log.error("No record was defined for LRS in Imports table");
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
   * @param syncRecord
   * @param statement
   */
  private void createSyncRecordDetail(final SyncRecord syncRecord, final Statement statement) {
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
  private void updateImportInProcess(final Import imports) {
    // If the previous run was successful, we will update the dates.
    // If not, we just re run again with same old dates.
    if (imports.getRecordStatus().equals(StatusConstants.SUCCESS)) {
      imports.setImportStartDate(imports.getImportEndDate());
      imports.setImportEndDate(new Timestamp(System.currentTimeMillis()));
    }
    imports.setRecordStatus(StatusConstants.INPROCESS);
    importService.save(imports);
  }

  private Import createImport() {
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
