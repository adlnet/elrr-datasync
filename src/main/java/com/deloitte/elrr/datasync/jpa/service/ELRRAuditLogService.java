package com.deloitte.elrr.datasync.jpa.service;

import java.time.ZonedDateTime;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.deloitte.elrr.datasync.entity.ELRRAuditLog;
import com.deloitte.elrr.datasync.repository.ELRRAuditLogRepository;

@Service
public class ELRRAuditLogService implements CommonSvc<ELRRAuditLog, UUID> {

    @Autowired
    private ELRRAuditLogRepository elrrAuditLogRepository;

    @Override
    public UUID getId(final ELRRAuditLog entity) {
        return null;
    }

    /**
     * @param purgeDate
     */
    @Transactional
    public void deleteByDate(final ZonedDateTime purgeDate) {
        elrrAuditLogRepository.deleteByDate(purgeDate);
    }

    @Override
    public CrudRepository<ELRRAuditLog, UUID> getRepository() {
        return elrrAuditLogRepository;
    }
}
