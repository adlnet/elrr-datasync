package com.deloitte.elrr.datasync.service;

import static org.assertj.core.api.Assertions.fail;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;

import java.io.File;
import java.io.IOException;
import java.util.UUID;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

import com.deloitte.elrr.datasync.KafkaStatusCheck;
import com.deloitte.elrr.datasync.entity.ELRRAuditLog;
import com.deloitte.elrr.datasync.entity.Import;
import com.deloitte.elrr.datasync.exception.DatasyncException;
import com.deloitte.elrr.datasync.jpa.service.ELRRAuditLogService;
import com.deloitte.elrr.datasync.jpa.service.ImportService;
import com.deloitte.elrr.datasync.producer.KafkaProducer;
import com.deloitte.elrr.datasync.util.TestFileUtil;
import com.yetanalytics.xapi.model.Statement;
import com.yetanalytics.xapi.util.Mapper;

import lombok.extern.slf4j.Slf4j;

@ExtendWith(MockitoExtension.class)
@Slf4j
class NewDataServiceTest {

    @Mock
    private KafkaProducer kafkaProducer;

    @Mock
    private ELRRAuditLogService elrrAuditLogService;

    @Mock
    private ImportService importService;

    @Mock
    private KafkaStatusCheck kafkaStatusCheck;

    @InjectMocks
    private NewDataService newDataService;

    @Test
    void testBasicStatementProcessing() {

        try {

            File testFile = TestFileUtil.getJsonTestFile("completed.json");

            Statement[] stmts = Mapper.getMapper().readValue(testFile,
                    Statement[].class);
            assertTrue(stmts != null);

            Mockito.doReturn(true).when(kafkaStatusCheck).isKafkaRunning();

            ELRRAuditLog auditLog = new ELRRAuditLog();
            auditLog.setStatementId(UUID.randomUUID().toString());

            Mockito.doReturn(auditLog).when(elrrAuditLogService).save(any());

            newDataService.process(stmts);

        } catch (DatasyncException | IOException e) {
            fail("Should not have thrown any exception");
        }
    }

    @Test
    void testFailedSerializationAuditLog() {

        try {

            File testFile = TestFileUtil.getJsonTestFile("completed.json");

            Statement[] stmts = Mapper.getMapper().readValue(testFile,
                    Statement[].class);
            assertTrue(stmts != null);

            Mockito.doReturn(true).when(kafkaStatusCheck).isKafkaRunning();

            Mockito.doReturn("Test Kafka message.").when(kafkaProducer)
                    .writeValueAsString(stmts[0].getId());

            ELRRAuditLog auditLog = new ELRRAuditLog();
            auditLog.setStatementId(UUID.randomUUID().toString());

            Mockito.doReturn(auditLog).when(elrrAuditLogService).save(any());

            newDataService.process(stmts);

        } catch (DatasyncException | IOException e) {
            assertEquals("Test processing ex", e.getMessage());
        }
    }

    @Test
    void testNoKafka() {

        try {

            File testFile = TestFileUtil.getJsonTestFile("completed.json");

            Statement[] stmts = Mapper.getMapper().readValue(testFile,
                    Statement[].class);
            assertTrue(stmts != null);

            Import imp = new Import();
            imp.setId(UUID.randomUUID());
            imp.setImportName("name");
            imp.setRetries(0);
            importService.save(imp);

            Mockito.doReturn(imp).when(importService).findByName(any());

            newDataService.process(stmts);

        } catch (DatasyncException | IOException e) {
            assertEquals(e.getMessage(), "Max retries reached. Giving up.");
        }
    }

}
