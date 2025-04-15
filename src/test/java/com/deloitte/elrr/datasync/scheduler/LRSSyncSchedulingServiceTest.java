/** */
package com.deloitte.elrr.datasync.scheduler;

import java.sql.Timestamp;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.mockito.junit.jupiter.MockitoSettings;
import org.mockito.quality.Strictness;
import org.springframework.test.util.ReflectionTestUtils;

import com.deloitte.elrr.datasync.entity.Import;
import com.deloitte.elrr.datasync.jpa.service.ImportService;
import com.deloitte.elrr.datasync.service.LRSService;
import com.deloitte.elrr.datasync.service.NewDataService;
import com.yetanalytics.xapi.model.Statement;

/**
 * @author mnelakurti
 */
@ExtendWith(MockitoExtension.class)
@MockitoSettings(strictness = Strictness.LENIENT)
class LRSSyncSchedulingServiceTest {

  @Mock private LRSService lrsService;

  @Mock private NewDataService newDataService;

  @Mock private ImportService importService;

  private static final Timestamp STARTDATE = new Timestamp(System.currentTimeMillis());

  @Test
  void testRun() {
    try {
      LRSSyncSchedulingService mockLRSSyncSchedulingService = new LRSSyncSchedulingService();
      ReflectionTestUtils.setField(mockLRSSyncSchedulingService, "lrsService", lrsService);
      ReflectionTestUtils.setField(mockLRSSyncSchedulingService, "newDataService", newDataService);
      ReflectionTestUtils.setField(mockLRSSyncSchedulingService, "importService", importService);
      Mockito.doReturn(getStatement()).when(lrsService).process(null);
      mockLRSSyncSchedulingService.run();
    } catch (NullPointerException e) {
      e.printStackTrace();
    }
  }

  @Test
  void testNoSyncRun() {
    try {
      LRSSyncSchedulingService mockLRSSyncSchedulingService = new LRSSyncSchedulingService();
      ReflectionTestUtils.setField(mockLRSSyncSchedulingService, "lrsService", lrsService);
      ReflectionTestUtils.setField(mockLRSSyncSchedulingService, "newDataService", newDataService);
      ReflectionTestUtils.setField(mockLRSSyncSchedulingService, "importService", importService);
      Mockito.doReturn(getStatement()).when(lrsService).process(null);
      mockLRSSyncSchedulingService.run();
    } catch (NullPointerException e) {
      e.printStackTrace();
    }
  }

  @Test
  void testNORun() {
    try {
      LRSSyncSchedulingService mockLRSSyncSchedulingService = new LRSSyncSchedulingService();
      ReflectionTestUtils.setField(mockLRSSyncSchedulingService, "lrsService", lrsService);
      ReflectionTestUtils.setField(mockLRSSyncSchedulingService, "newDataService", newDataService);
      ReflectionTestUtils.setField(mockLRSSyncSchedulingService, "importService", importService);
      Mockito.doReturn(getImport()).when(importService).findByName("Deloitte LRS");
      Mockito.doReturn(getStatement()).when(lrsService).process(STARTDATE);
      mockLRSSyncSchedulingService.run();
    } catch (NullPointerException e) {
      e.printStackTrace();
    }
  }

  /**
   * @return Import
   */
  private static Import getImport() {
    Import newimports = new Import();
    newimports.setImportName("Deloitte LRS");
    newimports.setRecordStatus("SUCCESS");
    newimports.setImportStartDate(STARTDATE);
    return newimports;
  }

  /**
   * @return Statement[]
   */
  public static Statement[] getStatement() {
    Statement[] statements = new Statement[1];
    Statement statement = new Statement();
    statements[0] = statement;
    return statements;
  }
}
