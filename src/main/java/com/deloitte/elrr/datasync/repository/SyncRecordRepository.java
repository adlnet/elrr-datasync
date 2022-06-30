package com.deloitte.elrr.datasync.repository;

import com.deloitte.elrr.datasync.entity.SyncRecord;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface SyncRecordRepository extends JpaRepository<SyncRecord, Long> {

  /**
   *
   * @param recordStatus
   * @return List<SyncRecord>
   */
  @Query(
    "SELECT s FROM SyncRecord s "
    + "WHERE LOWER(s.recordStatus) = LOWER(:recordStatus)"
  )
  List<SyncRecord> findUnprocessed(
    @Param("recordStatus") String recordStatus
  );

  /**
   *
   * @param syncRecordStatus
   * @param synckey
   * @return SyncRecord
   */
  @Query(
    "SELECT s FROM SyncRecord s WHERE LOWER(s.recordStatus) "
    + "= LOWER(:recordStatus) and LOWER(s.syncKey) = LOWER(:synckey)"
  )
  SyncRecord findExisting(
    @Param("recordStatus") String syncRecordStatus,
    @Param("synckey") String synckey
  );
}
