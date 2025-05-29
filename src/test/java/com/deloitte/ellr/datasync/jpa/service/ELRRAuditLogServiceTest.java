package com.deloitte.ellr.datasync.jpa.service;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import com.deloitte.elrr.datasync.entity.ELRRAuditLog;
import com.deloitte.elrr.datasync.exception.DatasyncException;
import com.deloitte.elrr.datasync.jpa.service.ELRRAuditLogService;
import com.deloitte.elrr.datasync.repository.ELRRAuditLogRepository;

import lombok.extern.slf4j.Slf4j;

@ExtendWith(MockitoExtension.class)
@Slf4j
class ELRRAuditLogServiceTest {

    @Mock
    ELRRAuditLogRepository elrrAuditLogRepository;

    @InjectMocks
    private ELRRAuditLogService elrrAuditLogService;

    @Test
    void test() {

        try {

            ELRRAuditLog elrrAuditLog = new ELRRAuditLog();

            elrrAuditLogService.findAll();
            elrrAuditLogService.deleteAll();
            elrrAuditLogService.getId(elrrAuditLog);

        } catch (DatasyncException e) {
            e.printStackTrace();
        }
    }
}
