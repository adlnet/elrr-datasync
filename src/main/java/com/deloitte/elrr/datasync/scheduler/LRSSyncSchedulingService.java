package com.deloitte.elrr.datasync.scheduler;

import java.time.ZonedDateTime;

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
    private ZonedDateTime initialDate;

    private static final String LRSNAME = "Yet Analytics LRS";
    private static final String EXCEPTION_MESSAGE = "Exception message: ";
    private static final String EXCEPTION_CAUSE = "Exception cause: ";
    private static final String LRS_SYNC_FAILED = "LRS Sync failed.";

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

            // Update import status to INPROCESS
            importRecord = importService.updateImportStatus(importRecord,
                    RecordStatus.INPROCESS);

            // Make call to LRSService.invokeLRS(final ZonedDateTime startDate)
            result = lrsService.process(importRecord.getImportStartDate());

            if (result != null && result.length > 0) {

                // Process unprocessed
                newDataService.process(result);

                // Get stored
                ZonedDateTime stored = result[result.length - 1].getStored();
                log.info("stored = " + stored);

                // Update import startDate
                importRecord = importService.updateImportStartDate(importRecord,
                        stored);

                importRecord = importService.updateImportEndDate(importRecord);

            } else {

                log.info("No statements returned from LRS.");

            }

            // Update import status to SUCCESS
            importRecord = importService.updateImportStatus(importRecord,
                    RecordStatus.SUCCESS);

        } catch (DatasyncException e) {

            handleDifferentExceptions(importRecord, e, "DatasyncException");

        } catch (ResourceNotFoundException e) {

            handleDifferentExceptions(importRecord, e,
                    "ResourceNotFoundException");

        } catch (NullPointerException e) {

            handleDifferentExceptions(importRecord, e, "NullPointerException");

        } catch (Exception e) {

            handleDifferentExceptions(importRecord, e, "Exception");

        }

    }

    private void handleDifferentExceptions(Import importRecord, Exception e,
            String exceptionType) {

        log.error("***** " + exceptionType + " *****");
        log.error(EXCEPTION_MESSAGE + e.getMessage(), e);
        log.error(EXCEPTION_CAUSE + e.getCause());
        log.error(LRS_SYNC_FAILED);
        log.error("***** " + exceptionType + " *****");

        if (importRecord != null) {
            importRecord.setRetries(0);
            importService.update(importRecord);
        }
    }

}
