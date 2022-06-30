package com.deloitte.elrr.datasync.jpa.service;

import com.deloitte.elrr.datasync.entity.Import;
import com.deloitte.elrr.datasync.repository.ImportRepository;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Service;

@Service
public class ImportService implements CommonSvc<Import, Long> {
  /**
   *
   */
  private final ImportRepository importsRepository;
  /**
   *
   * @param newimportsRepository
   */
  public ImportService(final ImportRepository newimportsRepository) {
    this.importsRepository = newimportsRepository;
  }
  /**
   *
   * @param name
   * @return Import
   */
  public Import findByName(final String name) {
    return importsRepository.findByName(name);
  }
  /**
   *
   *@return CrudRepository<Import, Long>
   */
  @Override
  public CrudRepository<Import, Long> getRepository() {
    return this.importsRepository;
  }
  /**
   *
   *@return Long
   */
  @Override
  public Long getId(final Import entity) {
    return null;
  }
}
