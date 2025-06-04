package com.deloitte.elrr.datasync.scheduler;

import static org.assertj.core.api.Assertions.fail;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import com.deloitte.elrr.datasync.exception.DatasyncException;
import com.deloitte.elrr.datasync.exception.ResourceNotFoundException;
import com.deloitte.elrr.datasync.jpa.service.ELRRAuditLogService;

import lombok.extern.slf4j.Slf4j;

@ExtendWith(MockitoExtension.class)
@Slf4j
class PurgeAuditLogSchedulingServiceTest {

    @Mock
    private ELRRAuditLogService elrrAuditLogService;

    @InjectMocks
    private PurgeAuditLogSchedulingService purgeAuditLogSchedulingservice;

    @Test
    void test() {

        try {

            purgeAuditLogSchedulingservice.run();

        } catch (DatasyncException | ResourceNotFoundException e) {
            fail("Should not have thrown any exception");
        }
    }
}
