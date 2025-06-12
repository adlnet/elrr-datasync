package com.deloitte.elrr.datasync.repository;

import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.deloitte.elrr.datasync.entity.Import;

@Repository
@SuppressWarnings("checkstyle:linelength")
public interface ImportRepository extends JpaRepository<Import, UUID> {

    /**
     * @param importName
     * @return Import
     */
    @Query("SELECT i FROM Import i WHERE LOWER(i.importName) = LOWER(:importName)")
    Import findByName(@Param("importName") String importName);
}
