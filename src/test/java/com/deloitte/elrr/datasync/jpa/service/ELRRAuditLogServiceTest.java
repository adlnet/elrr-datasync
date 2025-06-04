package com.deloitte.elrr.datasync.jpa.service;

import static org.assertj.core.api.Assertions.fail;

import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.UUID;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import com.deloitte.elrr.datasync.entity.ELRRAuditLog;
import com.deloitte.elrr.datasync.exception.DatasyncException;
import com.deloitte.elrr.datasync.exception.ResourceNotFoundException;
import com.deloitte.elrr.datasync.repository.ELRRAuditLogRepository;

import lombok.extern.slf4j.Slf4j;

@ExtendWith(MockitoExtension.class)
@Slf4j
class ELRRAuditLogServiceTest {

    @Mock
    private ELRRAuditLogRepository elrrAuditLogRepository;

    @InjectMocks
    private ELRRAuditLogService elrrAuditLogService;

    @Test
    void test() {

        try {

            ELRRAuditLog elrrAuditLog = new ELRRAuditLog();
            elrrAuditLog.setId(UUID.randomUUID());
            elrrAuditLogService.save(elrrAuditLog);

            elrrAuditLogService.findAll();
            elrrAuditLog.getId();
            elrrAuditLogService.deleteAll();
            elrrAuditLogService.getId(elrrAuditLog);

        } catch (DatasyncException e) {
            fail("Should not have thrown any exception");
        }
    }

    @Test
    void testDelete() {

        try {

            ELRRAuditLog elrrAuditLog = new ELRRAuditLog();
            elrrAuditLog.setId(UUID.randomUUID());
            elrrAuditLogService.save(elrrAuditLog);

            LocalDateTime localDateTime = LocalDateTime.parse(
                    "2025-12-05T15:30:00Z", DateTimeFormatter.ISO_DATE_TIME);

            Timestamp timestamp = Timestamp.valueOf(localDateTime);

            elrrAuditLogService.deleteByDate(timestamp);

        } catch (DatasyncException e) {
            fail("Should not have thrown any exception");
        }
    }

    @Test
    void testCommomSvc() {

        try {

            ELRRAuditLog auditLog = new ELRRAuditLog();
            UUID id = UUID.randomUUID();
            auditLog.setId(id);
            elrrAuditLogService.save(auditLog);

            elrrAuditLogService.findAll();
            elrrAuditLogService.delete(id);
            elrrAuditLogService.deleteAll();

        } catch (ResourceNotFoundException e) {
        }
    }

}
