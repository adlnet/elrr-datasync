package com.deloitte.elrr.datasync.jpa.service;

import java.util.UUID;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Service;

import com.deloitte.elrr.datasync.entity.Import;
import com.deloitte.elrr.datasync.repository.ImportRepository;

@Service
public class ImportService implements CommonSvc<Import, UUID> {

  private final ImportRepository importsRepository;

  /**
   * @param newimportsRepository
   */
  public ImportService(final ImportRepository newimportsRepository) {
    this.importsRepository = newimportsRepository;
  }

  /**
   * @param name
   * @return Import
   */
  public Import findByName(final String name) {
    return importsRepository.findByName(name);
  }

  /**
   * @return CrudRepository<Import, UUID>
   */
  @Override
  public CrudRepository<Import, UUID> getRepository() {
    return this.importsRepository;
  }

  /**
   * @return UUID
   */
  @Override
  public UUID getId(final Import entity) {
    return entity.getId();
  }
}
