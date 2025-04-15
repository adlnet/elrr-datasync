package com.deloitte.elrr.datasync.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.deloitte.elrr.datasync.dto.MessageVO;
import com.deloitte.elrr.datasync.entity.ELRRAuditLog;
import com.deloitte.elrr.datasync.exception.DatasyncException;
import com.deloitte.elrr.datasync.jpa.service.ELRRAuditLogService;
import com.deloitte.elrr.datasync.producer.KafkaProducer;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.yetanalytics.xapi.model.Statement;

import lombok.extern.slf4j.Slf4j;

@Service
@Slf4j
public class NewDataService {

  @Autowired private KafkaProducer kafkaProducer;

  @Autowired private KafkaProducer kafkaProd;

  // Use the interface
  @Autowired private ELRRAuditLogService elrrAuditLogService;

  /**
   * @param statements
   * @throws DatasyncException
   */
  // 1. Iterate over statements.
  // 2. Insert ELRRAuditLog.
  // 3. Create Kafka message.
  @Transactional
  public void process(Statement[] statements) {

    log.info(" **Inside NewDataService");

    try {

      for (Statement stmnt : statements) {
        MessageVO kafkaMessage = new MessageVO();
        kafkaMessage.setStatement(stmnt);
        insertAuditLog(kafkaMessage);
        kafkaProducer.sendMessage(kafkaMessage);
      }

    } catch (DatasyncException e) {
      log.error("Exception in processing " + e.getMessage());
      throw e;
    } catch (JsonProcessingException e) {
      log.error("Exception in processing " + e.getMessage());
    }
  }

  /**
   * @param messageVo
   * @param synchRecordId
   * @throws JsonProcessingException
   */
  // private void insertAuditLog(final MessageVO messageVo, final Long synchRecordId)
  private void insertAuditLog(final MessageVO messageVo) throws JsonProcessingException {

    log.info("Creating ELRRAuditLog.");

    try {
      ELRRAuditLog auditLog = new ELRRAuditLog();
      auditLog.setStatement(kafkaProd.writeValueAsString(messageVo.getStatement()));
      elrrAuditLogService.save(auditLog);
    } catch (JsonProcessingException e) {
      throw e;
    }
  }
}
