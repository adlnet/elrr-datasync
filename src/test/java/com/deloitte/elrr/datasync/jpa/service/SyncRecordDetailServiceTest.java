package com.deloitte.elrr.datasync.jpa.service;

import java.util.ArrayList;
import java.util.List;

import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.mockito.junit.jupiter.MockitoSettings;
import org.mockito.quality.Strictness;
import org.springframework.test.util.ReflectionTestUtils;

import com.deloitte.elrr.datasync.entity.SyncRecordDetail;
import com.deloitte.elrr.datasync.repository.SyncRecordDetailRepository;

@ExtendWith(MockitoExtension.class)
@MockitoSettings(strictness = Strictness.LENIENT)
class SyncRecordDetailServiceTest {

     /**
     *
     */
    @Mock
    private SyncRecordDetail mockSyncRecordDetail;

    /**
     *
     */
    @Mock
    private SyncRecordDetailRepository syncRecordDetailsRepository;

    @BeforeAll
    static void setUpBeforeClass() throws Exception {
    }

    @AfterAll
    static void tearDownAfterClass() throws Exception {
    }

    @BeforeEach
    void setUp() throws Exception {
    }

    @AfterEach
    void tearDown() throws Exception {
    }

    /**
     *
     * @throws Exception
     */
    @Test
    void test() throws Exception {
        SyncRecordDetailService syncRecordDetailService
        = new SyncRecordDetailService();
        ReflectionTestUtils.setField(syncRecordDetailService,
                "syncRecordDetailsRepository", syncRecordDetailsRepository);
        Mockito.doReturn(getSyncRecordDetailList())
        .when(syncRecordDetailsRepository).findBySyncRecordId(1L);
        syncRecordDetailService.getId(mockSyncRecordDetail);
        syncRecordDetailService.getId(null);
        syncRecordDetailService.get(1L);
        syncRecordDetailService.process(mockSyncRecordDetail);
        syncRecordDetailService.findBySyncRecordId(1L);
        syncRecordDetailService.findUnprocessed();
        syncRecordDetailService.deleteAll();
    }

    /**
    *
    * @return List <SyncRecordDetail>
    */
   private static List<SyncRecordDetail>  getSyncRecordDetailList() {
       List<SyncRecordDetail> syncRecordDetailList = new ArrayList<>();
       SyncRecordDetail syncRecordDetail = new SyncRecordDetail();
       syncRecordDetail.setSyncRecordId(1L);
       syncRecordDetailList.add(syncRecordDetail);
       return syncRecordDetailList;
   }
}
