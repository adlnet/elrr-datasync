/**
 *
 */
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

import com.deloitte.elrr.datasync.dto.ElrrStatement;
import com.deloitte.elrr.datasync.entity.Import;
import com.deloitte.elrr.datasync.entity.ImportDetail;
import com.deloitte.elrr.datasync.entity.SyncRecord;
import com.deloitte.elrr.datasync.jpa.service.ImportDetailService;
import com.deloitte.elrr.datasync.jpa.service.ImportService;
import com.deloitte.elrr.datasync.jpa.service.SyncRecordDetailService;
import com.deloitte.elrr.datasync.jpa.service.SyncRecordService;
import com.deloitte.elrr.datasync.service.LRSService;
import com.deloitte.elrr.datasync.service.NewDataService;

/**
 * @author mnelakurti
 *
 */
@ExtendWith(MockitoExtension.class)
@MockitoSettings(strictness = Strictness.LENIENT)
class LRSSyncSchedulingServiceTest {

    /**
    *
    */
    @Mock
    private LRSService lrsService;
   /**
    *
    */
    @Mock
   private NewDataService newDataService;
   /**
    *
    */
    @Mock
   private ImportService importService;
   /**
    *
    */
    @Mock
   private ImportDetailService importDetailService;
   /**
    *
    */
    @Mock
   private SyncRecordService syncService;
   /**
    *
    */
    @Mock
   private SyncRecordDetailService syncRecordDetailService;

   /**
    *
    */
   private static final Timestamp STARTDATE
   = new Timestamp(System.currentTimeMillis());

    /**
     *
     */
    @Test
    void testRun() {
        LRSSyncSchedulingService mockLRSSyncSchedulingService
        = new LRSSyncSchedulingService();
        ReflectionTestUtils.setField(mockLRSSyncSchedulingService,
                "lrsService", lrsService);
        ReflectionTestUtils.setField(mockLRSSyncSchedulingService,
                "newDataService", newDataService);
        ReflectionTestUtils.setField(mockLRSSyncSchedulingService,
                "importService", importService);
        ReflectionTestUtils.setField(mockLRSSyncSchedulingService,
                "importDetailService", importDetailService);
        ReflectionTestUtils.setField(mockLRSSyncSchedulingService,
                "syncService", syncService);
        ReflectionTestUtils.setField(mockLRSSyncSchedulingService,
                "syncRecordDetailService", syncRecordDetailService);
        Mockito.doReturn(getImport()).when(importService)
        .findByName("Deloitte LRS");
        Mockito.doReturn(getElrrStatement()).when(lrsService).process(null);
        Mockito.doReturn(getImportDetails()).when(importDetailService)
        .save(getImportDetails());
        Mockito.doReturn(getSyncRecord()).when(syncService)
        .findExistingRecord(null);
        mockLRSSyncSchedulingService.run();
    }

    /**
    *
    */
   @Test
   void testNoSyncRun() {
       LRSSyncSchedulingService mockLRSSyncSchedulingService
       = new LRSSyncSchedulingService();
       ReflectionTestUtils.setField(mockLRSSyncSchedulingService,
               "lrsService", lrsService);
       ReflectionTestUtils.setField(mockLRSSyncSchedulingService,
               "newDataService", newDataService);
       ReflectionTestUtils.setField(mockLRSSyncSchedulingService,
               "importService", importService);
       ReflectionTestUtils.setField(mockLRSSyncSchedulingService,
               "importDetailService", importDetailService);
       ReflectionTestUtils.setField(mockLRSSyncSchedulingService,
               "syncService", syncService);
       ReflectionTestUtils.setField(mockLRSSyncSchedulingService,
               "syncRecordDetailService", syncRecordDetailService);
       Mockito.doReturn(getImport()).when(importService)
       .findByName("Deloitte LRS");
       Mockito.doReturn(getElrrStatement()).when(lrsService).process(null);
       Mockito.doReturn(getImportDetails()).when(importDetailService)
       .save(getImportDetails());
       mockLRSSyncSchedulingService.run();
   }
    /**
    *
    */
   @Test
   void testNORun() {
       LRSSyncSchedulingService mockLRSSyncSchedulingService
       = new LRSSyncSchedulingService();
       ReflectionTestUtils.setField(mockLRSSyncSchedulingService,
               "lrsService", lrsService);
       ReflectionTestUtils.setField(mockLRSSyncSchedulingService,
               "newDataService", newDataService);
       ReflectionTestUtils.setField(mockLRSSyncSchedulingService,
               "importService", importService);
       ReflectionTestUtils.setField(mockLRSSyncSchedulingService,
               "importDetailService", importDetailService);
       ReflectionTestUtils.setField(mockLRSSyncSchedulingService,
               "syncService", syncService);
       ReflectionTestUtils.setField(mockLRSSyncSchedulingService,
               "syncRecordDetailService", syncRecordDetailService);
       Mockito.doReturn(getImport()).when(importService)
       .findByName("Deloitte LRS");
       Mockito.doReturn(getElrrStatement()).when(lrsService).process(STARTDATE);
       Mockito.doReturn(getImportDetails()).when(importDetailService)
       .save(getImportDetails());
       //Mockito.doReturn(getSyncRecord()).when(syncService)
       //.findExistingRecord(null);
       mockLRSSyncSchedulingService.run();
   }
    /**
     *
     * @return Import
     */
    private static Import getImport() {
        Import newimports = new Import();
        newimports.setImportName("Deloitte LRS");
        newimports.setImportId(1L);
        newimports.setRecordStatus("SUCCESS");
        newimports.setImportStartDate(STARTDATE);
        return newimports;
    }
    /**
    *
    * @return List <ImportDetail>
    */
   private static ImportDetail  getImportDetails() {
       ImportDetail newimportDetail = new ImportDetail();
       newimportDetail.setImportId(1L);
       return newimportDetail;
   }
    /**
     *
     * @return ElrrStatement[]
     */
    public static ElrrStatement[] getElrrStatement() {
        ElrrStatement[] elrrStatements = new ElrrStatement[1];
        ElrrStatement elrrStatement = new ElrrStatement();
        elrrStatements[0] = elrrStatement;
        return elrrStatements;
      }

    /**
    *
    * @return ElrrStatement[]
    */
   public static SyncRecord getSyncRecord() {
       SyncRecord syncRecord = new SyncRecord();
       return syncRecord;
     }

}
