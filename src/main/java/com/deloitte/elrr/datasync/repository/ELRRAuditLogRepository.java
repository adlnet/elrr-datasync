package com.deloitte.elrr.datasync.repository;

import java.time.ZonedDateTime;
import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.deloitte.elrr.datasync.entity.ELRRAuditLog;

@Repository
public interface ELRRAuditLogRepository
        extends JpaRepository<ELRRAuditLog, UUID> {

    /**
     * @param purgeDate
     */
    @Modifying
    @Query("DELETE FROM ELRRAuditLog a WHERE a.inserteddate <= :purgeDate")
    void deleteByDate(@Param("purgeDate") ZonedDateTime purgeDate);
}
