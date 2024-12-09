// PHL
package com.deloitte.elrr.datasync.jpa.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Service;

import com.deloitte.elrr.datasync.entity.Errors;
import com.deloitte.elrr.datasync.repository.ErrorsRepository;

import lombok.extern.slf4j.Slf4j;

@Service
@Slf4j
public class ErrorsService implements CommonSvc<Errors, Long> {
    
    @Autowired
    private ErrorsRepository errorsRepository;
    
  /**
   * @param synchRecordId
   * @param errorMsg
   * @return Errors
   */
  public Errors createErrors(final String synchRecordId, final String errorMsg) {
    Errors errors = new Errors();
    errors.setErrorMsg("synch record id = " + synchRecordId + " ERROR: " + errorMsg);
    errorsRepository.save(errors);
    return errors;
  }
  
  @Override
  public Long getId(Errors entity) {
      return entity.getErrorsId();
  }
  
  @Override
  public CrudRepository<Errors, Long> getRepository() {
      return this.errorsRepository;
  }
  
}