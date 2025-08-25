package com.deloitte.elrr.datasync.jpa.service;

import java.time.ZonedDateTime;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import com.deloitte.elrr.datasync.entity.Import;
import com.deloitte.elrr.datasync.entity.types.RecordStatus;
import com.deloitte.elrr.datasync.exception.ResourceNotFoundException;
import com.deloitte.elrr.datasync.repository.ImportRepository;

import lombok.extern.slf4j.Slf4j;

@Service
@Slf4j
public class ImportService implements CommonSvc<Import, UUID> {

    private final ImportRepository importsRepository;
    private static final String LRSNAME = "Yet Analytics LRS";

    @Value("${initial.date}")
    private ZonedDateTime initialDate;

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
        importRecord.setRecordStatus(RecordStatus.SUCCESS);
        importRecord.setRetries(0);
        importRecord.setImportName(LRSNAME);
        importRecord.setImportStartDate(initialDate);
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
    public Import updateImportStatus(Import importRecord, RecordStatus status)
            throws ResourceNotFoundException {

        log.info("Updating import status to " + status);

        importRecord.setRecordStatus(status);
        update(importRecord);

        return importRecord;
    }

    /**
     * @param importRecord
     * @param startDate
     * @return importRecord
     * @throws ResourceNotFoundException
     */
    public Import updateImportStartDate(Import importRecord,
            ZonedDateTime startDate) throws ResourceNotFoundException {

        log.info("Updating import start date.");

        importRecord.setImportStartDate(startDate);
        update(importRecord);

        return importRecord;
    }

    /**
     * @param importRecord
     * @return importRecord
     */
    public Import resetRetries(Import importRecord)
            throws ResourceNotFoundException {

        importRecord.setRetries(0);
        update(importRecord);

        return importRecord;
    }
}
