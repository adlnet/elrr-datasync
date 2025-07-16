package com.deloitte.elrr.datasync.scheduler;

import java.sql.Timestamp;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import com.deloitte.elrr.datasync.entity.Import;
import com.deloitte.elrr.datasync.entity.types.RecordStatus;
import com.deloitte.elrr.datasync.exception.DatasyncException;
import com.deloitte.elrr.datasync.exception.ResourceNotFoundException;
import com.deloitte.elrr.datasync.jpa.service.ImportService;
import com.deloitte.elrr.datasync.service.LRSService;
import com.deloitte.elrr.datasync.service.NewDataService;
import com.yetanalytics.xapi.model.Statement;

import lombok.extern.slf4j.Slf4j;

@Service
@Slf4j
public class LRSSyncSchedulingService {

    @Autowired
    private LRSService lrsService;

    @Autowired
    private NewDataService newDataService;

    @Autowired
    private ImportService importService;

    @Value("${initial.date}")
    private Timestamp initialDate;

    private static final String LRSNAME = "Yet Analytics LRS";

    /**
     * @author phleven
     *
     *         1. Connect to db and get Last sync date.
     *
     *         2. Make a call to LRS and get the data.
     *
     *         3. Update Import table.
     *
     *         4. Invoke New Processor to process unprocessed records.
     *
     */
    @Scheduled(cron = "${cronExpression}")
    public void run() {

        Import importRecord = importService.findByName(LRSNAME);

        try {

            // If no import record
            if (importRecord == null) {
                importRecord = importService.createImport();
            } else if (importRecord.getRecordStatus().equals(
                    RecordStatus.INPROCESS)) {
                log.info("Statements are still being processed.");
                return;
            }

            Statement[] result = null;

            // Update import start and end dates
            importRecord = importService.updateImportStartEndDates(
                    importRecord);

            // Update import status to INPROCESS
            importRecord = importService.updateImportStatus(importRecord,
                    RecordStatus.INPROCESS);

            // Make call to LRSService.invokeLRS(final Timestamp startDate)
            result = lrsService.process(importRecord.getImportStartDate());

            // Process unprocessed
            newDataService.process(result);

            // Update import status to SUCCESS
            importRecord = importService.updateImportStatus(importRecord,
                    RecordStatus.SUCCESS);

        } catch (DatasyncException e) {
            log.error("***** DatasyncException *****");
            log.error("Exception message: " + e.getMessage(), e);
            log.error("Exception cause: " + e.getCause());
            log.error("LRS Sync failed.");
            log.error("***** DatasyncException *****");

            if (importRecord != null) {
                importRecord.setRetries(0);
                importService.update(importRecord);
            }

        } catch (ResourceNotFoundException e) {
            log.error("***** ResourceNotFoundException *****");
            log.error("Exception message: " + e.getMessage(), e);
            log.error("Exception cause: " + e.getCause());
            log.error("LRS Sync failed.");
            log.error("***** ResourceNotFoundException *****");

            if (importRecord != null) {
                importRecord.setRetries(0);
                importService.update(importRecord);
            }

        } catch (NullPointerException e) {
            log.error("***** NullPointerException *****");
            log.error("Exception message: " + e.getMessage(), e);
            log.error("Exception cause: " + e.getCause());
            log.error("LRS Sync failed.");
            log.error("***** NullPointerException *****");

            if (importRecord != null) {
                importRecord.setRetries(0);
                importService.update(importRecord);
            }

        } catch (Exception e) {
            log.error("***** Exception *****");
            log.error("Exception message: " + e.getMessage(), e);
            log.error("Exception cause: " + e.getCause());
            log.error("LRS Sync failed.");
            log.error("***** Exception *****");

            if (importRecord != null) {
                importRecord.setRetries(0);
                importService.update(importRecord);
            }

        }

    }

}
