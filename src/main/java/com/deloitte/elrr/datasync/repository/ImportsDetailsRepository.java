package com.deloitte.elrr.datasync.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.deloitte.elrr.datasync.entity.ImportsDetails;

@Repository
public interface ImportsDetailsRepository extends JpaRepository<ImportsDetails, Long>{
    
}
