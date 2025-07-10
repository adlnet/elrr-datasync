package com.deloitte.elrr.datasync.jpa.service;

import java.sql.Timestamp;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import com.deloitte.elrr.datasync.entity.Import;
import com.deloitte.elrr.datasync.exception.ResourceNotFoundException;
import com.deloitte.elrr.datasync.repository.ImportRepository;
import com.deloitte.elrr.datasync.scheduler.StatusConstants;

import lombok.extern.slf4j.Slf4j;

@Service
@Slf4j
public class ImportService implements CommonSvc<Import, UUID> {

    private final ImportRepository importsRepository;

    @Value("${initial.date}")
    private Timestamp initialDate;

    /**
     * @param newimportsRepository
     */
    public ImportService(final ImportRepository newimportsRepository) {
        this.importsRepository = newimportsRepository;
    }

    /**
     * @param name
     * @return Import
     */
    public Import findByName(final String name) {
        return importsRepository.findByName(name);
    }

    /**
     * @return CrudRepository<Import, UUID>
     */
    @Override
    public CrudRepository<Import, UUID> getRepository() {
        return this.importsRepository;
    }

    /**
     * @return UUID
     */
    @Override
    public UUID getId(final Import entity) {
        return entity.getId();
    }

    /**
     * @return importRecord
     */
    @Transactional(propagation = Propagation.REQUIRES_NEW)
    public Import createImport() {
        log.info("Creating new import.");
        Import importRecord = new Import();
        importRecord.setRecordStatus(StatusConstants.SUCCESS);
        importRecord.setRetries(0);
        importRecord.setImportName(StatusConstants.LRSNAME);
        importRecord.setImportStartDate(initialDate);
        importRecord.setImportEndDate(initialDate);
        save(importRecord);
        return importRecord;
    }

    /**
     * @param importRecord
     * @param status
     * @return importRecord
     * @throws ResourceNotFoundException
     */
    @Transactional(propagation = Propagation.REQUIRES_NEW)
    public Import updateImportStatus(Import importRecord, String status)
            throws ResourceNotFoundException {

        log.info("Updating import status to " + status);

        importRecord.setRecordStatus(status);
        update(importRecord);

        return importRecord;
    }

    /**
     * @param importRecord
     * @return importRecord
     * @throws ResourceNotFoundException
     */
    @Transactional(propagation = Propagation.REQUIRES_NEW)
    public Import updateImportStartEndDates(Import importRecord)
            throws ResourceNotFoundException {

        log.info("Updating import start and end dates.");

        if (importRecord.getRecordStatus().equals(StatusConstants.SUCCESS)) {
            importRecord.setImportStartDate(importRecord.getImportEndDate());
            importRecord.setImportEndDate(new Timestamp(System
                    .currentTimeMillis()));
        }

        update(importRecord);

        return importRecord;
    }

}
