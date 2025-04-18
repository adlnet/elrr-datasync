package com.deloitte.elrr.datasync.test.service;

import static org.junit.jupiter.api.Assertions.assertTrue;

import java.io.File;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import com.deloitte.elrr.datasync.jpa.service.ELRRAuditLogService;
import com.deloitte.elrr.datasync.jpa.service.ImportService;
import com.deloitte.elrr.datasync.producer.KafkaProducer;
import com.deloitte.elrr.datasync.service.NewDataService;
import com.deloitte.elrr.test.datasync.test.util.TestFileUtils;
import com.yetanalytics.xapi.model.Statement;
import com.yetanalytics.xapi.util.Mapper;

import lombok.extern.slf4j.Slf4j;

@ExtendWith(MockitoExtension.class)
@Slf4j
class NewDataServiceTest {

  @Mock private KafkaProducer kafkaProducer;

  @Mock private KafkaProducer kafkaProd;

  @Mock private ELRRAuditLogService elrrAuditLogService;

  @Mock private ImportService importService;

  @InjectMocks NewDataService newDataService;

  @Test
  void test() {

    File testFile = TestFileUtils.getJsonTestFile("completed");

    try {

      Statement stmt = Mapper.getMapper().readValue(testFile, Statement.class);
      assertTrue(stmt != null);

      Statement[] statements = {stmt};

      newDataService.process(statements);

    } catch (Exception e) {
      e.printStackTrace();
    }
  }
}
