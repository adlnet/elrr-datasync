package com.deloitte.elrr.datasync.repository;

import com.deloitte.elrr.datasync.entity.SyncRecordDetail;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface SyncRecordDetailRepository
  extends JpaRepository<SyncRecordDetail, Long> {

  /**
   *
   * @param recordStatus
   * @return  List<SyncRecordDetail>
   */
  @Query(
    "SELECT s FROM SyncRecordDetail s "
    + "WHERE LOWER(s.recordStatus) = LOWER(:recordStatus)"
  )
  List<SyncRecordDetail> findUnprocessed(
    @Param("recordStatus") String recordStatus
  );

  /**
   *
   * @param syncRecordId
   * @return List<SyncRecordDetail>
   */
  @Query(
    "SELECT s FROM SyncRecordDetail s WHERE s.syncRecordId = :syncRecordId"
  )
  List<SyncRecordDetail> findBySyncRecordId(
    @Param("syncRecordId") long syncRecordId
  );
}
