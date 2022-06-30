/**
 *
 */
package com.deloitte.elrr.datasync.jpa.service;

import static org.junit.Assert.assertNotNull;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.mockito.junit.jupiter.MockitoSettings;
import org.mockito.quality.Strictness;
import org.springframework.test.util.ReflectionTestUtils;

import com.deloitte.elrr.datasync.entity.SyncRecord;
import com.deloitte.elrr.datasync.repository.SyncRecordRepository;

/**
 * @author mnelakurti
 *
 */
@ExtendWith(MockitoExtension.class)
@MockitoSettings(strictness = Strictness.LENIENT)
class SyncRecordServiceTest {

    /**
     *
     */
    @Mock
    private SyncRecord mockSyncRecord;

    /**
     *
     */
    @Mock
    private SyncRecordRepository syncRecordRepository;


    /**
     *
     * @throws Exception
     */
    @Test
    void test() throws Exception {
        SyncRecordService syncRecordService
        = new SyncRecordService();
        ReflectionTestUtils.setField(syncRecordService,
                "syncRecordRepository", syncRecordRepository);
        Mockito.doReturn(getSyncRecord())
        .when(syncRecordRepository).findExisting("", "");
        syncRecordService.findExistingRecord("");
        syncRecordService.get(1L);
        assertNotNull(syncRecordService.getId(mockSyncRecord));
        assertNotNull(syncRecordService.get(1L));
        syncRecordService.createSyncRecord("key", 1L);
        syncRecordService.deleteAll();
    }

    /**
    *
    * @return SyncRecord
    */
   private static SyncRecord  getSyncRecord() {
       SyncRecord syncRecord = new SyncRecord();
       syncRecord.setSyncRecordId(1L);
       syncRecord.setSyncRecordId(1L);
       return syncRecord;
   }
}
