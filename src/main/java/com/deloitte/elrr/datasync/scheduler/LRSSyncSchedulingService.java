package com.deloitte.elrr.datasync.scheduler;

import java.sql.Timestamp;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import com.deloitte.elrr.datasync.entity.Import;
import com.deloitte.elrr.datasync.exception.DatasyncException;
import com.deloitte.elrr.datasync.exception.RunTimeServiceException;
import com.deloitte.elrr.datasync.jpa.service.ImportService;
import com.deloitte.elrr.datasync.service.LRSService;
import com.deloitte.elrr.datasync.service.NewDataService;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.yetanalytics.xapi.model.Statement;

import lombok.extern.slf4j.Slf4j;

@Service
@Slf4j
public class LRSSyncSchedulingService {

  @Autowired private LRSService lrsService;

  @Autowired private NewDataService newDataService;

  @Autowired private ImportService importService;

  @Value("${initial.date}")
  private Timestamp initialDate;

  /*
   * 1. Connect to db and get Last sync date.
   *
   * 2. Make a call to LRS and get the data.
   *
   * 3. Update Import table.
   *
   * 4. Invoke New Processor to process unprocessed records.
   *
   */
  @Scheduled(cron = "${cronExpression}")
  public void run() {

    log.info("**inside LRS schedule method.");
    Import importRecord = importService.findByName(StatusConstants.LRSNAME);

    try {

      // If no import record
      if (importRecord == null) {
        importRecord = createImport();
      }

      Statement[] result = null;
      updateImportInProcess(importRecord);

      // Make call to LRSService.invokeLRS(final Timestamp startDate)
      result = lrsService.process(importRecord.getImportStartDate());

      // Update import status
      importRecord.setRecordStatus(StatusConstants.SUCCESS);
      importService.save(importRecord);

      // Process unprocessed
      newDataService.process(result);

    } catch (DatasyncException | RunTimeServiceException | JsonProcessingException e) {
      log.error("LRS Sync failed.");
      importRecord.setRetries(0);
      importService.update(importRecord);
    }
  }

  /**
   * @param imports
   */
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
    importRecord.setRetries(0);
    importRecord.setImportName(StatusConstants.LRSNAME);
    importRecord.setImportStartDate(initialDate);
    importRecord.setImportEndDate(initialDate);
    importService.save(importRecord);
    return importRecord;
  }
}
