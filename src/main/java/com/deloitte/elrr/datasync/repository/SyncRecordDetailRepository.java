package com.deloitte.elrr.datasync.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.deloitte.elrr.datasync.entity.SyncRecordDetail;

@Repository
public interface SyncRecordDetailRepository extends JpaRepository<SyncRecordDetail, Long>{
    
	@Query("SELECT s FROM SyncRecordDetail s WHERE LOWER(s.recordStatus) = LOWER(:recordStatus)")
    public List<SyncRecordDetail> findUnprocessed(@Param("recordStatus") String recordStatus);
	
	@Query("SELECT s FROM SyncRecordDetail s WHERE s.syncRecordId = :syncRecordId")
    public List<SyncRecordDetail> findBySyncRecordId(@Param("syncRecordId") long syncRecordId);
}
