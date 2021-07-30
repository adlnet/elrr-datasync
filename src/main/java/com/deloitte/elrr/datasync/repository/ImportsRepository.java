package com.deloitte.elrr.datasync.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.deloitte.elrr.datasync.entity.Imports;

@Repository
public interface ImportsRepository extends JpaRepository<Imports, Long>{
    
	@Query("SELECT i FROM Imports i WHERE LOWER(i.importName) = LOWER(:importName)")
    public Imports findByName(@Param("importName") String importName);
	
}
