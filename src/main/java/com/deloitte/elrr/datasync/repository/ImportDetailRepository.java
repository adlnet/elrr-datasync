package com.deloitte.elrr.datasync.repository;

import com.deloitte.elrr.datasync.entity.ImportDetail;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface ImportDetailRepository
  extends JpaRepository<ImportDetail, Long> {

  /**
   *
   * @param importId
   * @return List<ImportDetail>
   */
  @Query(
    "SELECT i FROM ImportDetail i WHERE "
    + "importId = :importId order by importBeginTime desc "
  )
  List<ImportDetail> findByImportId(@Param("importId") long importId);
}
