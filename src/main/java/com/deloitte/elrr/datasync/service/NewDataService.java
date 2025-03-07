package com.deloitte.elrr.datasync.service;

import java.sql.Timestamp;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import com.deloitte.elrr.datasync.dto.MessageVO;
import com.deloitte.elrr.datasync.entity.Import;
import com.deloitte.elrr.datasync.entity.SyncRecord;
import com.deloitte.elrr.datasync.entity.SyncRecordDetail;
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

  private static String lrsName = "Deloitte LRS";

  /**
   * 1. Retrieve all unprocessed syncrecord records with INSERTED status. 2. Create the Kafka
   * message. 3. Update SyncRecord and SyncRecordDetails to SUCCCESS/INSERTED status.
   */
  public void process(Statement[] statements) {

    log.info("Inside NewDataService");

    // Unprocessed synchrecord.recordStatus=inserted
    List<SyncRecord> syncList = getUnprocessedRecords();
    log.info("==> Unprocessed synch records = " + syncList.size());

    Timestamp importStartDate = getLRSImport().getImportStartDate();
    Timestamp importEndDate = getLRSImport().getImportEndDate();

    int x = 0;

    for (SyncRecord syncRecord : syncList) {

      SyncRecordDetail syncRecordDetail = null;

      try {

        syncRecordDetail = syncRecordDetailService.findBySyncRecordId(syncRecord.getSyncRecordId());

        if (x <= statements.length - 1) {
          MessageVO kafkaMessage =
              createKafkaJsonMessage(statements[x], importStartDate, importEndDate);
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
  private MessageVO createKafkaJsonMessage(
      final Statement statement, final Timestamp importStartDate, final Timestamp importEndDate)
      throws JsonProcessingException {
    MessageVO vo = new MessageVO();
    vo.setStatement(statement);
    vo.setImportStartDate(importStartDate);
    vo.setImportEndDate(importEndDate);
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

  public Import getLRSImport() {
    return importService.findByName(lrsName);
  }
}
