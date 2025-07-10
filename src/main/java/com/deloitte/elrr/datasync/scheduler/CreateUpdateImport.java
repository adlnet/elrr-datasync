package com.deloitte.elrr.datasync.scheduler;

import java.sql.Timestamp;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import com.deloitte.elrr.datasync.entity.Import;
import com.deloitte.elrr.datasync.exception.ResourceNotFoundException;
import com.deloitte.elrr.datasync.jpa.service.ImportService;

import lombok.extern.slf4j.Slf4j;

@Component
@Slf4j
public class CreateUpdateImport {

    @Autowired
    private ImportService importService;

    @Value("${initial.date}")
    private Timestamp initialDate;

    /**
     * @return importRecord
     */
    @Transactional(propagation = Propagation.REQUIRES_NEW)
    public Import createImport() {

        // This method uses Propagation.REQUIRES_NEW in a seperate class to
        // force commit on return.

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

    /**
     * @param importRecord
     * @return importRecord
     * @throws ResourceNotFoundException
     */
    @Transactional(propagation = Propagation.REQUIRES_NEW)
    public Import updateImportInProcess(Import importRecord)
            throws ResourceNotFoundException {

        // This method uses Propagation.REQUIRES_NEW in a seperate class to
        // force commit on return.

        // If the previous run was successful, we will update the dates.
        // If not, we just re run again with same old dates.

        log.info("Updating import.");

        if (importRecord.getRecordStatus().equals(StatusConstants.SUCCESS)) {
            importRecord.setImportStartDate(importRecord.getImportEndDate());
            importRecord.setImportEndDate(new Timestamp(System
                    .currentTimeMillis()));
        }

        importRecord.setRecordStatus(StatusConstants.INPROCESS);
        importService.update(importRecord);

        return importRecord;
    }

    /**
     * @param importRecord
     * @return importRecord
     * @throws ResourceNotFoundException
     */
    @Transactional(propagation = Propagation.REQUIRES_NEW)
    public Import updateImportSuccess(Import importRecord)
            throws ResourceNotFoundException {

        // This method uses Propagation.REQUIRES_NEW in a seperate class to
        // force commit on return.

        log.info("Updating import.");

        importRecord.setRecordStatus(StatusConstants.SUCCESS);
        importService.update(importRecord);

        return importRecord;
    }

}
