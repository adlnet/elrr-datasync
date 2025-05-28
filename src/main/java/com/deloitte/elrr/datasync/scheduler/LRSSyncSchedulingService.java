package com.deloitte.elrr.datasync.scheduler;

import java.sql.Timestamp;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.deloitte.elrr.datasync.entity.Import;
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

        log.info("===============inside LRS schedule method.===============\n");
        Import importRecord = importService.findByName(StatusConstants.LRSNAME);

        try {

            // If no import record
            if (importRecord == null) {
                importRecord = createImport();
            }

            Statement[] result = null;
            importRecord = updateImportInProcess(importRecord);

            // Make call to LRSService.invokeLRS(final Timestamp startDate)
            result = lrsService.process(importRecord.getImportStartDate());

            // Update import status
            importRecord.setRecordStatus(StatusConstants.SUCCESS);
            importService.save(importRecord);

            // Process unprocessed
            newDataService.process(result);

        } catch (DatasyncException | ResourceNotFoundException e) {

            log.error("LRS Sync failed.");
            importRecord.setRetries(0);
            importService.update(importRecord);
        }
    }

    /**
     * @param Import
     * @return Import
     * @throws ResourceNotFoundException
     */
    @Transactional
    private Import updateImportInProcess(Import importRecord) {

        // If the previous run was successful, we will update the dates.
        // If not, we just re run again with same old dates.

        log.info("Updating import.");

        try {

            if (importRecord.getRecordStatus().equals(StatusConstants.SUCCESS)) {
                importRecord.setImportStartDate(importRecord.getImportEndDate());
                importRecord.setImportEndDate(new Timestamp(System.currentTimeMillis()));
            }

            importRecord.setRecordStatus(StatusConstants.INPROCESS);
            importService.update(importRecord);

        } catch (ResourceNotFoundException e) {
            log.error("Error updating Import", e);
            e.printStackTrace();
            throw e;
        }

        return importRecord;
    }

    public Import createImport() {
        log.info("Creating new import.");
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
