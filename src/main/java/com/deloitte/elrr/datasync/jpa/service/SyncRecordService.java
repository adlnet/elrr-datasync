package com.deloitte.elrr.datasync.jpa.service;

import com.deloitte.elrr.datasync.entity.SyncRecord;
import com.deloitte.elrr.datasync.repository.SyncRecordDetailRepository;
import com.deloitte.elrr.datasync.repository.SyncRecordRepository;
import java.util.List;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Service;

@Service
@Slf4j
public class SyncRecordService implements CommonSvc<SyncRecord, Long> {
  /**
   *
   */
  private static String inserted = "INSERTED";
  /**
   *
   */
  @Autowired
  private SyncRecordRepository syncRecordRepository;
  /**
   *
   */
  @Autowired
  private SyncRecordDetailRepository syncRecordDetailsRepository;
  /**
   *
   * @return List<SyncRecord>
   */
  public List<SyncRecord> findUnprocessed() {
    return syncRecordRepository.findUnprocessed("inserted");
  }
  /**
   *
   * @param key
   * @param importDetailsId
   * @return SyncRecord
   */
  public SyncRecord createSyncRecord(final String key,
          final long importDetailsId) {
    SyncRecord syncRecord = new SyncRecord();
    syncRecord.setSyncKey(key);
    syncRecord.setRecordStatus(inserted);
    syncRecord.setImportdetailId(importDetailsId);
    syncRecordRepository.save(syncRecord);
    return syncRecord;
  }
  /**
   *
   * @param key
   * @return SyncRecord
   */
  public SyncRecord findExistingRecord(final String key) {
    return syncRecordRepository.findExisting(inserted, key);
  }
  /**
   *
   */
  @Override
  public Long getId(final SyncRecord entity) {
    return entity.getSyncRecordId();
  }
  /**
   *
   */
  @Override
  public CrudRepository<SyncRecord, Long> getRepository() {
    return this.syncRecordRepository;
  }
}
