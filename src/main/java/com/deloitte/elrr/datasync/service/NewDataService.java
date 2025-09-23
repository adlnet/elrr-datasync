package com.deloitte.elrr.datasync.service;

import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.deloitte.elrr.datasync.KafkaStatusCheck;
import com.deloitte.elrr.datasync.dto.MessageVO;
import com.deloitte.elrr.datasync.entity.ELRRAuditLog;
import com.deloitte.elrr.datasync.entity.Import;
import com.deloitte.elrr.datasync.exception.DatasyncException;
import com.deloitte.elrr.datasync.jpa.service.ELRRAuditLogService;
import com.deloitte.elrr.datasync.jpa.service.ImportService;
import com.deloitte.elrr.datasync.producer.KafkaProducer;
import com.yetanalytics.xapi.model.Statement;

import lombok.extern.slf4j.Slf4j;

@Service
@Slf4j
public class NewDataService {

    @Autowired
    private KafkaProducer kafkaProducer;

    @Autowired
    private ELRRAuditLogService elrrAuditLogService;

    @Autowired
    private ImportService importService;

    @Autowired
    private KafkaStatusCheck kafkaStatusCheck;

    @Value("${max.retries}")
    private int maxRetries;

    private static final String LRSNAME = "Yet Analytics LRS";

    /**
     * @param statements
     */
    @Transactional
    public void process(Statement[] statements) {

        try {

            if (kafkaStatusCheck.isKafkaRunning()) {
                processStatements(statements);
            } else {
                throw new DatasyncException("Kafka is not running");
            }

        } catch (DatasyncException e) {

            // Get number of retries
            Import importRecord = importService.findByName(LRSNAME);

            if (importRecord != null) {

                int attempts = importRecord.getRetries();
                attempts++;

                log.error("processStatements failed on attempt " + Integer
                        .toString(attempts) + "retrying...");

                if (attempts >= maxRetries) {
                    log.error("Max retries reached. Giving up.");
                    throw new DatasyncException(
                            "Max retries reached. Giving up.");
                } else {
                    importRecord.setRetries(attempts);
                    importService.update(importRecord);
                }

            }

        }
    }

    /**
     * @param statements
     * @throws DatasyncException
     */
    // 1. Iterate over statements.
    // 2. Insert ELRRAuditLog.
    // 3. Create Kafka message.
    @Transactional
    public void processStatements(Statement[] statements)
            throws DatasyncException {

        log.info("Process statements.");

        for (Statement stmnt : statements) {
            MessageVO kafkaMessage = new MessageVO();
            UUID id = stmnt.getId();
            kafkaMessage.setStatement(stmnt);
            insertAuditLog(id);
            kafkaProducer.sendMessage(kafkaMessage);
            // Future optimization
            // kafkaProducer.sendAsyncMessage(kafkaMessage);
        }

    }

    /**
     * @param id
     * @throws DatasyncException
     */
    private void insertAuditLog(final UUID id) {

        log.info("Creating ELRRAuditLog.");

        try {
            ELRRAuditLog auditLog = new ELRRAuditLog();
            auditLog.setStatementId(kafkaProducer.writeValueAsString(id));
            elrrAuditLogService.save(auditLog);
        } catch (DatasyncException e) {
            log.error("Error creating ELRRAuditLog record.", e);
            throw new DatasyncException("Error creating ELRRAuditLog record.",
                    e);
        }
    }
}
