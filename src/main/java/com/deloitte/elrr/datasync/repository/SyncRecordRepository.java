package com.deloitte.elrr.datasync.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.deloitte.elrr.datasync.entity.SyncRecord;

@Repository
public interface SyncRecordRepository extends JpaRepository<SyncRecord, Long>{
    
	@Query("SELECT s FROM SyncRecord s WHERE LOWER(s.recordStatus) = LOWER(:recordStatus)")
    public List<SyncRecord> findUnprocessed(@Param("recordStatus") String recordStatus);
	
	@Query("SELECT s FROM SyncRecord s WHERE LOWER(s.recordStatus) = LOWER(:recordStatus) and LOWER(s.syncKey) = LOWER(:synckey)")
    public SyncRecord findExisting(@Param("recordStatus") String syncRecordStatus, @Param("synckey") String synckey);
	
}
