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
import com.deloitte.elrr.datasync.exception.DatasyncException;
import com.deloitte.elrr.datasync.jpa.service.ELRRAuditLogService;
import com.deloitte.elrr.datasync.jpa.service.ErrorsService;
import com.deloitte.elrr.datasync.jpa.service.ImportService;
import com.deloitte.elrr.datasync.jpa.service.SyncRecordDetailService;
import com.deloitte.elrr.datasync.jpa.service.SyncRecordService;
import com.deloitte.elrr.datasync.producer.KafkaProducer;
import com.deloitte.elrr.datasync.scheduler.StatusConstants;
import com.deloitte.elrr.datasync.scheduler.VerbIdConstants;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.yetanalytics.xapi.model.AbstractObject;
import com.yetanalytics.xapi.model.Activity;
import com.yetanalytics.xapi.model.Statement;
import com.yetanalytics.xapi.model.Verb;

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

  // Use the interface
  @Autowired private ELRRAuditLogService elrrAuditLogService;

  /**
   * @param statements
   * @throws DatasyncException
   */
  // 1. Retrieve unprocessed syncrecord (status = INSERTED).
  // 2. Insert ELRRAuditLog.
  // 3. Create Kafka message.
  // 4. Update syncrecord and syncrecorddetail status to SUCCESS/INSERTED.
  public void process(Statement[] statements) {

    log.info("Inside NewDataService");

    // Unprocessed synchrecord.recordStatus=inserted
    SyncRecord syncRec = syncRecordService.findExistingRecord(StatusConstants.LRSNAME);

    // If no SyncRecord
    if (syncRec == null) {
      Import importRecord = importService.findByName(StatusConstants.LRSNAME);
      createSyncRecord(importRecord);
    }

    List<SyncRecord> syncList = syncRecordService.findUnprocessed();

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

          MessageVO kafkaMessage = new MessageVO();
          kafkaMessage.setStatement(statements[x]);

          insertAuditLog(kafkaMessage, syncRecordDetail.getSyncRecordId());

          // Can Verb Id be processed
          boolean fireRule = fireRule(statements[x]);

          if (fireRule) {
            kafkaProducer.sendMessage(kafkaMessage);
          } else {
            log.info("Verb Id " + statements[x].getVerb().getId() + " cannot be processed. \n\n");
          }
        }

        x++;

        // Update synchrecord.recordStatus to success (processes)
        syncRecord.setRecordStatus("SUCCESS");
        syncRecordService.save(syncRecord);

        // Update synchrecorddetail.recordStatus to success
        syncRecordDetail.setRecordStatus("SUCCESS");
        syncRecordDetailService.save(syncRecordDetail);

      } catch (DatasyncException | JsonProcessingException e) {

        log.error("Exception in processing " + e.getMessage());

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

          log.error("Exception in processing after 3 retries.");
        }
      }
    }
  }

  /**
   * @param importRecord
   * @return SyncRecord
   */
  private SyncRecord createSyncRecord(Import importRecord) {
    log.info("Creating SyncRecord.");
    SyncRecord syncRecord = new SyncRecord();
    syncRecord.setRecordStatus(StatusConstants.INSERTED);
    syncRecord.setSyncKey(StatusConstants.LRSNAME);
    syncRecord.setImportdetailId(importRecord.getImportId());
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
    syncRecordDetail.setRecordStatus(StatusConstants.INSERTED);
    syncRecordDetailService.save(syncRecordDetail);
    return syncRecordDetail;
  }

  /**
   * @param messageVo
   * @param synchRecordId
   * @throws JsonProcessingException
   */
  private void insertAuditLog(final MessageVO messageVo, Long synchRecordId)
      throws JsonProcessingException {

    log.info("Creating ELRRAuditLog.");

    try {
      ELRRAuditLog auditLog = new ELRRAuditLog();
      auditLog.setSyncid(synchRecordId);
      auditLog.setStatement(kafkaProd.writeValueAsString(messageVo.getStatement()));
      auditLog.setVerbid(messageVo.getStatement().getVerb().getId());
      elrrAuditLogService.save(auditLog);
    } catch (JsonProcessingException e) {
      throw e;
    }
  }

  /**
   * @param statement
   * @return boolean
   */
  private boolean fireRule(Statement statement) {

    // Get Object
    AbstractObject obj = statement.getObject();

    // Get Verb
    Verb verb = statement.getVerb();

    // Is Verb Id completed and activity
    if (verb.getId().equalsIgnoreCase(VerbIdConstants.COMPLETED_VERB_ID)
        && obj instanceof Activity) {
      return true;
    } else {
      return false;
    }
  }
}
