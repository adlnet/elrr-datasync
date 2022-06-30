/**
 *
 */
package com.deloitte.elrr.datasync.repository;

import static org.junit.Assert.assertEquals;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import com.deloitte.elrr.datasync.entity.SyncRecordDetail;

/**
 * @author mnelakurti
 *
 */
@ExtendWith(MockitoExtension.class)
class SyncRecordDetailRepositoryTest {

    /**
    *
    */
   @Mock
   private SyncRecordDetailRepository mockSyncRecordDetailRepository;

   /**
    *
    */
   @Mock
   private SyncRecordDetail syncRecordDetail;

    @Test
    void test() {
        syncRecordDetail.setSyncRecordId(1L);
        mockSyncRecordDetailRepository.findAll();
        mockSyncRecordDetailRepository.findById(1L);
        mockSyncRecordDetailRepository.save(syncRecordDetail);
        assertEquals(syncRecordDetail.getSyncRecordId(), 0);
        mockSyncRecordDetailRepository.delete(syncRecordDetail);
    }

}
