package com.deloitte.elrr.datasync.jpa.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Service;

import com.deloitte.elrr.datasync.entity.ELRRAuditLog;
import com.deloitte.elrr.datasync.repository.ELRRAuditLogRepository;

@Service
public class ELRRAuditLogService implements CommonSvc<ELRRAuditLog, Long> {

  @Autowired private ELRRAuditLogRepository elrrAuditLogRepository;

  @Override
  public Long getId(final ELRRAuditLog entity) {

    return null;
  }

  @Override
  public CrudRepository<ELRRAuditLog, Long> getRepository() {

    return elrrAuditLogRepository;
  }
}
