package com.deloitte.elrr.datasync.scheduler;

import java.sql.Timestamp;
import java.time.LocalDateTime;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import com.deloitte.elrr.datasync.jpa.service.ELRRAuditLogService;

import lombok.extern.slf4j.Slf4j;

@Service
@Slf4j
public class PurgeAuditLogSchedulingService {

  @Value("${purgeDays}")
  private int purgeDays;

  @Autowired private ELRRAuditLogService elrrAuditLogService;

  @Scheduled(cron = "${purgeCronExpression}")
  public void run() {
    // Purge ELRRAuditLog
    log.info("Purge ELRRAuditLog.");
    purgeAuditLog(purgeDays);
  }

  private void purgeAuditLog(int purgeDays) {
    // Purge ELRRAuditLog rows created <= purgeDays days before today.
    Timestamp currentTimestamp = new Timestamp(System.currentTimeMillis());
    LocalDateTime localDateTime = currentTimestamp.toLocalDateTime().minusDays(purgeDays);
    Timestamp purgeDate = Timestamp.valueOf(localDateTime);
    log.info("Purge date = " + purgeDate);
    elrrAuditLogService.deleteByDate(purgeDate);
  }
}
