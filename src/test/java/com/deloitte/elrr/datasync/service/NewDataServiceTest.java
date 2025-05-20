package com.deloitte.elrr.datasync.service;

import static org.junit.jupiter.api.Assertions.assertTrue;

import java.io.File;
import java.io.IOException;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import com.deloitte.elrr.datasync.exception.DatasyncException;
import com.deloitte.elrr.datasync.jpa.service.ELRRAuditLogService;
import com.deloitte.elrr.datasync.jpa.service.ImportService;
import com.deloitte.elrr.datasync.producer.KafkaProducer;
import com.deloitte.elrr.test.datasync.util.TestFileUtils;
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

    @InjectMocks
    private NewDataService newDataService;

    @Test
    void test() {

        try {

            File testFile = TestFileUtils.getJsonTestFile("completed.json");

            Statement[] stmts = Mapper.getMapper().readValue(testFile,
                    Statement[].class);
            assertTrue(stmts != null);

            newDataService.process(stmts);

        } catch (DatasyncException | IOException e) {
            e.printStackTrace();
        }
    }
}
