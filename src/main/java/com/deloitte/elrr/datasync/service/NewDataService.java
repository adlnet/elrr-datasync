package com.deloitte.elrr.datasync.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import com.deloitte.elrr.datasync.dto.AuditRecord;
import com.deloitte.elrr.datasync.dto.MessageVO;
import com.deloitte.elrr.datasync.entity.SyncRecord;
import com.deloitte.elrr.datasync.entity.SyncRecordDetail;
import com.deloitte.elrr.datasync.jpa.service.ErrorsService;
import com.deloitte.elrr.datasync.jpa.service.SyncRecordDetailService;
import com.deloitte.elrr.datasync.jpa.service.SyncRecordService;
import com.deloitte.elrr.datasync.producer.KafkaProducer;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.yetanalytics.xapi.model.Statement;

import lombok.extern.slf4j.Slf4j;

@Service
@Slf4j
public class NewDataService {
    
   // PHL
   @Value("${retries}")
   private int numberOfRetries;
    
   @Autowired
  private KafkaProducer kafkaProducer;
  
  @Autowired
  private SyncRecordService syncRecordService;
  
  @Autowired
  private SyncRecordDetailService syncRecordDetailService;
    
  // PHL
  @Autowired
  private ErrorsService errorsService;
  
  private ObjectMapper mapper = new ObjectMapper();

  /**
   * PHL
   * 1. Retrieve all unprocessed synchrecord records with INSERTED status
   * 2. Send the message to Kafka
   * 3. Update the records SyncRecord and SyncRecordDetails to SUCCCESS/INSERTED status
   *
   */
  public void process(Statement[] statements) {
      
    log.info("Inside NewDataService");
    
    // Unprocessed synchrecord.recordStatus=inserted
    List<SyncRecord> syncList = getUnprocessedRecords();
    log.info("==> Unprocessed synch records = " + syncList.size());
    
    int x = 0;
    
    for (SyncRecord syncRecord : syncList) {
        
       SyncRecordDetail detail = null;
       
       try {
           
        detail = syncRecordDetailService.findBySyncRecordId(syncRecord.getSyncRecordId());

        if (x <= statements.length - 1) {            
        
            MessageVO kafkaMessage = createKafkaJsonMessage(statements[x], detail);
            sendToKafka(kafkaMessage);
            
        }

        x++;    
        
        // Update synchrecord.recordStatus to success (processes)
        syncRecord.setRecordStatus("SUCCESS");
        syncRecordService.save(syncRecord);
        
        // Update synchrecorddetail.recordStatus to success
        detail.setRecordStatus("SUCCESS");
        syncRecordDetailService.save(detail);

      } catch (Exception e) {
          
        log.error("Exception in processing " + e.getMessage()); 
        e.printStackTrace();
        
        long retries = syncRecord.getRetries();
        
        if (retries < numberOfRetries) {
            
            // In case of exception change status back to inserted so that
            // they will be picked again next time and processed
            syncRecord.setRecordStatus("INSERTED");
            syncRecord.setRetries(retries + 1);
            syncRecordService.save(syncRecord);
            detail.setRecordStatus("INSERTED");
            syncRecordDetailService.save(detail);
            
        } else {
        
            syncRecord.setRecordStatus("FAILED");
            syncRecord.setRetries(0L);
            syncRecordService.save(syncRecord);
            detail.setRecordStatus("FAILED");
            syncRecordDetailService.save(detail);

             // Create Errors record
            errorsService.createErrors(Long.toString(syncRecord.getSyncRecordId()), e.getMessage());
        
        }
        
      }
    }
  }

  private void sendToKafka(final MessageVO message) {
    kafkaProducer.sendMessage(message);
  }

 /**
  * 
  * @param statement
  * @param detail
  * @return
  * @throws JsonProcessingException
  */
 private MessageVO createKafkaJsonMessage(final Statement statement, final SyncRecordDetail detail)
 throws JsonProcessingException {
     AuditRecord auditRecord = new AuditRecord(); 
     MessageVO vo = new MessageVO();
     
     Long detailId = detail.getSyncRecordDetailId();
     auditRecord.setAuditId(detail.getSyncRecordId());
     auditRecord.setAuditDetailId(detailId);

     vo.setStatement(statement);
     vo.setAuditRecord(auditRecord);
     
     return vo;
 }
 
   private List<SyncRecord> getUnprocessedRecords() {
    List<SyncRecord> syncList = syncRecordService.findUnprocessed();
    log.info("unprocessed list " + syncList.size());
    return syncList;
  }
}
