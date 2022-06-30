package com.deloitte.elrr.datasync.jpa.service;

import com.deloitte.elrr.datasync.entity.SyncRecordDetail;
import com.deloitte.elrr.datasync.repository.SyncRecordDetailRepository;
import java.util.List;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Service;

@Service
@Slf4j
public class SyncRecordDetailService
  implements CommonSvc<SyncRecordDetail, Long> {
  /**
   *
   */
  @Autowired
  private SyncRecordDetailRepository syncRecordDetailsRepository;
  /**
   *
   * @param object
   */
  public void process(final Object object) {
    syncRecordDetailsRepository.save((SyncRecordDetail) object);
  }
  /**
   *
   * @return List<SyncRecordDetail>
   */
  public List<SyncRecordDetail> findUnprocessed() {
    return syncRecordDetailsRepository.findUnprocessed("inserted");
  }
  /**
   *
   * @param recordId
   * @return List<SyncRecordDetail>
   */
  public List<SyncRecordDetail> findBySyncRecordId(final long recordId) {
    return syncRecordDetailsRepository.findBySyncRecordId(recordId);
  }
  /**
   *
   * @return Long
   */
  @Override
  public Long getId(final SyncRecordDetail entity) {
    return null;
  }
  /**
   *
   *@return CrudRepository<SyncRecordDetail, Long>
   */
  @Override
  public CrudRepository<SyncRecordDetail, Long> getRepository() {
    return this.syncRecordDetailsRepository;
  }
}
