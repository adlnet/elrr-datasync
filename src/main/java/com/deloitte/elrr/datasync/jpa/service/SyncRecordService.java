package com.deloitte.elrr.datasync.jpa.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Service;

import com.deloitte.elrr.datasync.entity.SyncRecord;
import com.deloitte.elrr.datasync.repository.SyncRecordRepository;
import com.deloitte.elrr.datasync.scheduler.StatusConstants;

import lombok.extern.slf4j.Slf4j;

@Service
@Slf4j
public class SyncRecordService implements CommonSvc<SyncRecord, Long> {

  @Autowired private SyncRecordRepository syncRecordRepository;

  /**
   * @return List<SyncRecord>
   */
  public List<SyncRecord> findUnprocessed() {
    return syncRecordRepository.findUnprocessed("inserted");
  }

  /**
   * @param key
   * @param importDetailsId
   * @return SyncRecord
   */
  public SyncRecord createSyncRecord(final String key, final long importDetailsId) {
    log.info("Creating new sync record.");
    SyncRecord syncRecord = new SyncRecord();
    syncRecord.setSyncKey(key);
    syncRecord.setRecordStatus(StatusConstants.INSERTED);
    syncRecord.setImportdetailId(importDetailsId);
    syncRecordRepository.save(syncRecord);
    return syncRecord;
  }

  /**
   * @param key
   * @return SyncRecord
   */
  public SyncRecord findExistingRecord(final String key) {
    return syncRecordRepository.findExisting(StatusConstants.INSERTED, key);
  }

  @Override
  public Long getId(final SyncRecord entity) {
    return entity.getSyncRecordId();
  }

  @Override
  public CrudRepository<SyncRecord, Long> getRepository() {
    return this.syncRecordRepository;
  }
}
