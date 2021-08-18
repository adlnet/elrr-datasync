package com.deloitte.elrr.datasync.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.deloitte.elrr.datasync.entity.Import;

@Repository
public interface ImportRepository extends JpaRepository<Import, Long>{
    
	@Query("SELECT i FROM Import i WHERE LOWER(i.importName) = LOWER(:importName)")
    public Import findByName(@Param("importName") String importName);
	
}
