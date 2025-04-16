package com.deloitte.elrr.datasync.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.deloitte.elrr.datasync.dto.MessageVO;
import com.deloitte.elrr.datasync.entity.ELRRAuditLog;
import com.deloitte.elrr.datasync.entity.Import;
import com.deloitte.elrr.datasync.exception.DatasyncException;
import com.deloitte.elrr.datasync.jpa.service.ELRRAuditLogService;
import com.deloitte.elrr.datasync.jpa.service.ImportService;
import com.deloitte.elrr.datasync.producer.KafkaProducer;
import com.deloitte.elrr.datasync.scheduler.StatusConstants;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.yetanalytics.xapi.model.Statement;

import lombok.extern.slf4j.Slf4j;

@Service
@Slf4j
public class NewDataService {

  @Autowired private KafkaProducer kafkaProducer;

  @Autowired private KafkaProducer kafkaProd;

  @Autowired private ELRRAuditLogService elrrAuditLogService;

  @Autowired private ImportService importService;

  @Value("${max.retries}")
  private int maxRetries;

  /**
   * @param statements
   * @throws DatasyncException
   * @throws JsonProcessingException
   */
  @Transactional
  public void process(Statement[] statements) throws JsonProcessingException {

    log.info("**Inside NewDataService");

    try {

      processStatements(statements);

    } catch (DatasyncException | JsonProcessingException e) {

      // Get number of retries
      Import importRecord = importService.findByName(StatusConstants.LRSNAME);
      int attempts = importRecord.getRetries();
      attempts++;

      log.error("processStatements failed on attempt " + attempts + " retrying...");

      if (attempts >= maxRetries) {
        log.error("Max retries reached. Giving up.");
        throw new DatasyncException("Max retries reached. Giving up.");
      } else {
        importRecord.setRetries(attempts);
        importService.update(importRecord);
      }
    }
  }

  /**
   * @param statements
   * @throws JsonProcessingException
   */
  // 1. Iterate over statements.
  // 2. Insert ELRRAuditLog.
  // 3. Create Kafka message.
  @Transactional
  public void processStatements(Statement[] statements) throws JsonProcessingException {

    log.info("Process statements.");

    try {

      for (Statement stmnt : statements) {
        MessageVO kafkaMessage = new MessageVO();
        kafkaMessage.setStatement(stmnt);
        insertAuditLog(kafkaMessage);
        kafkaProducer.sendMessage(kafkaMessage);
      }

    } catch (JsonProcessingException e) {
      throw e;
    }
  }

  /**
   * @param messageVo
   * @param synchRecordId
   * @throws JsonProcessingException
   */
  private void insertAuditLog(final MessageVO messageVo) throws JsonProcessingException {

    log.info("Creating ELRRAuditLog.");

    try {
      ELRRAuditLog auditLog = new ELRRAuditLog();
      auditLog.setStatement(kafkaProd.writeValueAsString(messageVo.getStatement()));
      elrrAuditLogService.save(auditLog);
    } catch (JsonProcessingException e) {
      log.error("Error creating ELRRAuditLog record - " + e.getMessage());
      throw e;
    }
  }
}
