package com.deloitte.elrr.datasync.scheduler;

import java.time.ZonedDateTime;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import com.deloitte.elrr.datasync.jpa.service.ELRRAuditLogService;

import lombok.extern.slf4j.Slf4j;

@Service
@Slf4j
@SuppressWarnings("checkstyle:hiddenfield")
public class PurgeAuditLogSchedulingService {

    @Value("${purgeDays}")
    private int purgeDays;

    @Autowired
    private ELRRAuditLogService elrrAuditLogService;

    /**
     * @author phleven
     */
    @Scheduled(cron = "${purgeCronExpression}")
    public void run() {
        // Purge ELRRAuditLog
        log.info("Purge ELRRAuditLog.");
        purgeAuditLog(purgeDays);
    }

    /**
     * @param purgeDays
     */
    private void purgeAuditLog(int purgeDays) {
        // Purge ELRRAuditLog rows created <= purgeDays days before today.
        ZonedDateTime purgeDate = ZonedDateTime.now().minusDays(purgeDays);
        log.info("Purge date = " + purgeDate);
        elrrAuditLogService.deleteByDate(purgeDate);
    }
}
