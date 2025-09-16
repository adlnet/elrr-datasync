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

            // Make call to LRSService.invokeLRS()
            result = lrsService.process(importRecord.getImportStartDate());

            if (result != null && result.length > 0) {

                // Update import status to INPROCESS
                importRecord = importService.updateImportStatus(importRecord,
                        RecordStatus.INPROCESS);

                for (int x = 1; result.length > 0; x++) {

                    // Process unprocessed
                    newDataService.process(result);

                    // Get stored
                    ZonedDateTime stored = result[result.length - 1]
                            .getStored();

                    // Update import startDate
                    importRecord = importService.updateImportStartDate(
                            importRecord, stored);

                    // Make call to LRSService.invokeLRS()
                    result = lrsService.process(importRecord
                            .getImportStartDate());

                    log.debug("Processing LRS batch " + x + " sucessful.");

                }

            } else {

                log.info("No statements returned from LRS.");

            }

            // Update import status to SUCCESS
            if (importRecord.getRecordStatus().equals(RecordStatus.INPROCESS)) {
                importRecord = importService.updateImportStatus(importRecord,
                        RecordStatus.SUCCESS);
            }

        } catch (DatasyncException | ResourceNotFoundException
                | NullPointerException e) {

            if (importRecord != null) {
                importService.resetRetries(importRecord);
            }

            throw new DatasyncException("Error LRS sync failed.", e);

        }

    }

}
