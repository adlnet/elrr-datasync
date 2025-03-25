package com.deloitte.elrr.datasync.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import com.deloitte.elrr.datasync.dto.MessageVO;
import com.deloitte.elrr.datasync.entity.ELRRAuditLog;
import com.deloitte.elrr.datasync.entity.Import;
import com.deloitte.elrr.datasync.entity.SyncRecord;
import com.deloitte.elrr.datasync.entity.SyncRecordDetail;
import com.deloitte.elrr.datasync.jpa.service.ELRRAuditLogService;
import com.deloitte.elrr.datasync.jpa.service.ErrorsService;
import com.deloitte.elrr.datasync.jpa.service.ImportService;
import com.deloitte.elrr.datasync.jpa.service.SyncRecordDetailService;
import com.deloitte.elrr.datasync.jpa.service.SyncRecordService;
import com.deloitte.elrr.datasync.producer.KafkaProducer;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.yetanalytics.xapi.model.Statement;

import lombok.extern.slf4j.Slf4j;

@Service
@Slf4j
public class NewDataService {

  @Value("${retries}")
  private int numberOfRetries;

  @Autowired private KafkaProducer kafkaProducer;

  @Autowired private SyncRecordService syncRecordService;

  @Autowired private SyncRecordDetailService syncRecordDetailService;

  @Autowired private ErrorsService errorsService;

  @Autowired private ImportService importService;

  @Autowired private KafkaProducer kafkaProd;

  @Autowired private ELRRAuditLogService elrrAuditLogService;

  private static String lrsName = "Deloitte LRS";
  private static String syncStatus = "inserted";
  private static String updatedBy = "ELRR";

  /**
   * 1. Retrieve all unprocessed syncrecord records with INSERTED status. 2. Create ELRRAuditLog 3.
   * Create the Kafka message. 4. Update SyncRecord and SyncRecordDetails to SUCCCESS/INSERTED
   * status.
   */
  public void process(Statement[] statements) {

    log.info("Inside NewDataService");

    // Unprocessed synchrecord.recordStatus=inserted
    SyncRecord syncRec = syncRecordService.findExistingRecord(lrsName);

    // If no SyncRecord
    if (syncRec == null) {
      Import importRecord = getLRSImport();
      createSyncRecord(importRecord);
    }

    List<SyncRecord> syncList = getUnprocessedRecords();
    log.info("Unprocessed synch records = " + syncList.size());

    int x = 0;

    for (SyncRecord syncRecord : syncList) {

      SyncRecordDetail syncRecordDetail = null;

      try {

        syncRecordDetail = syncRecordDetailService.findBySyncRecordId(syncRecord.getSyncRecordId());

        // If no SyncRecordDetail
        if (syncRecordDetail == null) {
          syncRecordDetail = createSyncRecordDetail(syncRecord.getSyncRecordId());
        }

        if (x <= statements.length - 1) {
          MessageVO kafkaMessage = createKafkaJsonMessage(statements[x]);
          insertAuditLog(kafkaMessage, syncRecordDetail.getSyncRecordId());
          sendToKafka(kafkaMessage);
        }

        x++;

        // Update synchrecord.recordStatus to success (processes)
        syncRecord.setRecordStatus("SUCCESS");
        syncRecordService.save(syncRecord);

        // Update synchrecorddetail.recordStatus to success
        syncRecordDetail.setRecordStatus("SUCCESS");
        syncRecordDetailService.save(syncRecordDetail);

      } catch (JsonProcessingException e) {

        log.error("Exception in processing " + e.getMessage());
        e.printStackTrace();

        long retries = syncRecord.getRetries();

        if (retries < numberOfRetries) {

          // In case of exception change status back to inserted so that
          // they will be picked again next time and processed
          syncRecord.setRecordStatus("INSERTED");
          syncRecord.setRetries(retries + 1);
          syncRecordService.save(syncRecord);
          syncRecordDetail.setRecordStatus("INSERTED");
          syncRecordDetailService.save(syncRecordDetail);

        } else {

          syncRecord.setRecordStatus("FAILED");
          syncRecord.setRetries(0L);
          syncRecordService.save(syncRecord);
          syncRecordDetail.setRecordStatus("FAILED");
          syncRecordDetailService.save(syncRecordDetail);

          // Create Errors record
          errorsService.createErrors(Long.toString(syncRecord.getSyncRecordId()), e.getMessage());
        }

        log.error("Exception in processing " + e.getMessage());
        e.getStackTrace();
      }
    }
  }

  /**
   * @param message
   */
  private void sendToKafka(final MessageVO message) {
    kafkaProducer.sendMessage(message);
  }

  /**
   * @param statement
   * @param syncRecordDetail
   * @return
   * @throws JsonProcessingException
   */
  private MessageVO createKafkaJsonMessage(final Statement statement)
      throws JsonProcessingException {
    MessageVO vo = new MessageVO();
    vo.setStatement(statement);

    return vo;
  }

  /**
   * @return List<SyncRecord>
   */
  private List<SyncRecord> getUnprocessedRecords() {
    List<SyncRecord> syncList = syncRecordService.findUnprocessed();
    log.info("unprocessed list " + syncList.size());
    return syncList;
  }

  /**
   * @return Import
   */
  public Import getLRSImport() {
    return importService.findByName(lrsName);
  }

  /**
   * @param importRecord
   * @return SyncREcord
   */
  private SyncRecord createSyncRecord(Import importRecord) {
    log.info("Creating SyncRecord.");
    SyncRecord syncRecord = new SyncRecord();
    syncRecord.setRecordStatus(syncStatus);
    syncRecord.setSyncKey(lrsName);
    syncRecord.setImportdetailId(importRecord.getImportId());
    syncRecord.setUpdatedBy(updatedBy);
    syncRecord.setRetries(0L);
    return syncRecord;
  }

  /**
   * @param syncRecordId
   * @return SyncRecordDetail
   */
  private SyncRecordDetail createSyncRecordDetail(long syncRecordId) {
    log.info("Creating SyncRecordDetail.");
    SyncRecordDetail syncRecordDetail = new SyncRecordDetail();
    syncRecordDetail.setSyncRecordId(syncRecordId);
    syncRecordDetail.setRecordStatus(syncStatus);
    syncRecordDetail.setUpdatedBy(updatedBy);
    syncRecordDetailService.save(syncRecordDetail);
    return syncRecordDetail;
  }

  /**
   * @param messageVo
   * @param synchRecordId
   */
  private void insertAuditLog(final MessageVO messageVo, Long synchRecordId) {
    log.info("Creating ELRRAuditLog.");
    ELRRAuditLog auditLog = new ELRRAuditLog();
    auditLog.setSyncid(synchRecordId);
    auditLog.setStatement(kafkaProd.writeValueAsString(messageVo.getStatement()));
    auditLog.setUpdatedBy(updatedBy);
    elrrAuditLogService.save(auditLog);
  }
}
