/** */
package com.deloitte.elrr.datasync.service;

import java.util.ArrayList;
import java.util.List;

import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.mockito.junit.jupiter.MockitoSettings;
import org.mockito.quality.Strictness;

import com.deloitte.elrr.datasync.entity.SyncRecord;
import com.deloitte.elrr.datasync.entity.SyncRecordDetail;
import com.deloitte.elrr.datasync.jpa.service.SyncRecordDetailService;
import com.deloitte.elrr.datasync.jpa.service.SyncRecordService;
import com.deloitte.elrr.datasync.producer.KafkaProducer;
import com.fasterxml.jackson.databind.ObjectMapper;

/**
 * @author mnelakurti
 */
@ExtendWith(MockitoExtension.class)
@MockitoSettings(strictness = Strictness.LENIENT)
class NewDataServiceTest {

  /** */
  @Mock private KafkaProducer kafkaProducer;

  /** */
  @Mock private SyncRecordService syncRecordService;

  /** */
  @Mock private SyncRecordDetailService syncRecordDetailService;

  /** */
  @Mock private ObjectMapper mapper = new ObjectMapper();

  /* @Test
  void test() throws JsonMappingException, JsonProcessingException {
      NewDataService newDataService = new NewDataService();
      ReflectionTestUtils.setField(newDataService,
              "kafkaProducer", kafkaProducer);
      ReflectionTestUtils.setField(newDataService,
              "syncRecordService", syncRecordService);
      ReflectionTestUtils.setField(newDataService,
              "syncRecordDetailService", syncRecordDetailService);
      ReflectionTestUtils.setField(newDataService,
              "mapper", mapper);
      Mockito.doReturn(getSyncRecord()).when(syncRecordService)
      .findUnprocessed();
      Mockito.doReturn(getSyncRecordDeatil())
      .when(syncRecordDetailService).findBySyncRecordId(1L);
      Mockito.doReturn(getLearnerChange())
      .when(mapper).readValue(getSyncRecordDeatil().getLearner(),
              LearnerChange.class);
      //newDataService.process();
  }

  @Test
  void testNoData() throws JsonMappingException, JsonProcessingException {
      NewDataService newDataService = new NewDataService();
      ReflectionTestUtils.setField(newDataService,
              "kafkaProducer", kafkaProducer);
      ReflectionTestUtils.setField(newDataService,
              "syncRecordService", syncRecordService);
      ReflectionTestUtils.setField(newDataService,
              "syncRecordDetailService", syncRecordDetailService);
      ReflectionTestUtils.setField(newDataService,
              "mapper", mapper);
      Mockito.doReturn(getSyncRecord()).when(syncRecordService)
      .findUnprocessed();
      Mockito.doReturn(getSyncRecordDeatil())
      .when(syncRecordDetailService).findBySyncRecordId(1L);
      //Mockito.doReturn(getLearnerChange())
      //.when(mapper).readValue(getSyncRecordDeatils().get(0).getLearner(),
      //LearnerChange.class);
      //newDataService.process();
  }*/

  /**
   * @return List<SyncRecord>
   */
  public static List<SyncRecord> getSyncRecord() {
    List<SyncRecord> syncList = new ArrayList<>();
    SyncRecord syncRecord = new SyncRecord();
    syncRecord.setSyncRecordId(1L);
    syncList.add(syncRecord);
    return syncList;
  }

  /**
   * @return SyncRecordDetail
   */
  public static SyncRecordDetail getSyncRecordDeatil() {
    SyncRecordDetail syncRecordDetail = new SyncRecordDetail();
    syncRecordDetail.setSyncRecordId(1L);
    return syncRecordDetail;
  }
}
