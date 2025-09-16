package com.deloitte.elrr.datasync.jpa.service;

import static org.assertj.core.api.Assertions.fail;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;

import java.time.ZonedDateTime;
import java.util.UUID;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
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
            UUID id = UUID.randomUUID();
            elrrAuditLog.setId(id);
            elrrAuditLogService.save(elrrAuditLog);

            Mockito.doReturn(true).when(elrrAuditLogRepository)
                    .existsById(any());
            elrrAuditLogService.update(elrrAuditLog);

            elrrAuditLogService.get(id);
            elrrAuditLogService.findAll();
            elrrAuditLog.getId();
            elrrAuditLogService.delete(id);
            elrrAuditLogService.deleteAll();
            elrrAuditLogService.getId(elrrAuditLog);

        } catch (DatasyncException e) {
            fail("Should not have thrown any exception");
        }
    }

    @Test
    void testResourceNotFound() {

        try {

            ELRRAuditLog elrrAuditLog = new ELRRAuditLog();
            UUID id = UUID.randomUUID();
            elrrAuditLog.setId(id);
            elrrAuditLogService.save(elrrAuditLog);

            Mockito.doReturn(false).when(elrrAuditLogRepository)
                    .existsById(any());
            elrrAuditLogService.update(elrrAuditLog);

        } catch (ResourceNotFoundException e) {
            assertTrue(e.getMessage().contains("Record to update not found"));
        }
    }

    @Test
    void testDelete() {

        try {

            ELRRAuditLog elrrAuditLog = new ELRRAuditLog();
            elrrAuditLog.setId(UUID.randomUUID());
            elrrAuditLogService.save(elrrAuditLog);

            Mockito.doReturn(true).when(elrrAuditLogRepository)
                    .existsById(any());
            elrrAuditLogService.update(elrrAuditLog);

            ZonedDateTime purgeDate = ZonedDateTime.now().minusDays(10);
            elrrAuditLogService.deleteByDate(purgeDate);

        } catch (DatasyncException e) {
            fail("Should not have thrown any exception");
        }
    }

}
