package com.deloitte.elrr.datasync.jpa.service;

import java.sql.Timestamp;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Primary;
import org.springframework.context.annotation.Scope;
import org.springframework.context.annotation.ScopedProxyMode;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.deloitte.elrr.datasync.entity.ELRRAuditLog;
import com.deloitte.elrr.datasync.repository.ELRRAuditLogRepository;

@Service
@Primary
//@Scope(proxyMode = ScopedProxyMode.TARGET_CLASS)
public class ELRRAuditLogService implements CommonSvc<ELRRAuditLog, Long> {

  @Autowired private ELRRAuditLogRepository elrrAuditLogRepository;

  @Override
  public Long getId(final ELRRAuditLog entity) {
    return null;
  }

  /**
   * @param purgeDate
   */
  @Transactional
  public void deleteByDate(final Timestamp purgeDate) {
    elrrAuditLogRepository.deleteByDate(purgeDate);
  }

  @Override
  public CrudRepository<ELRRAuditLog, Long> getRepository() {
    return elrrAuditLogRepository;
  }
}
