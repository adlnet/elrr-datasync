package com.deloitte.elrr.datasync.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.deloitte.elrr.datasync.entity.ImportDetail;

@Repository
public interface ImportDetailRepository extends JpaRepository<ImportDetail, Long>{
    
}
