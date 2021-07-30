package com.deloitte.elrr.datasync.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.deloitte.elrr.datasync.entity.SyncRecordDetails;

@Repository
public interface SyncRecordDetailsRepository extends JpaRepository<SyncRecordDetails, Long>{
    
	@Query("SELECT s FROM SyncRecordDetails s WHERE LOWER(s.syncDetailsStatus) = LOWER(:syncDetailsStatus)")
    public List<SyncRecordDetails> findUnprocessed(@Param("syncDetailsStatus") String syncDetailsStatus);
	
	@Query("SELECT s FROM SyncRecordDetails s WHERE s.syncRecordId = :syncRecordId")
    public List<SyncRecordDetails> findBySyncRecordId(@Param("syncRecordId") long syncRecordId);
}
