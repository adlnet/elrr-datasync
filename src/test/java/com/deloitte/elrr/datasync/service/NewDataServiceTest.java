/**
 *
 */
package com.deloitte.elrr.datasync.service;

import java.util.ArrayList;
import java.util.List;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.mockito.junit.jupiter.MockitoSettings;
import org.mockito.quality.Strictness;
import org.springframework.test.util.ReflectionTestUtils;

import com.deloitte.elrr.datasync.dto.LearnerChange;
import com.deloitte.elrr.datasync.dto.UserCourse;
import com.deloitte.elrr.datasync.entity.SyncRecord;
import com.deloitte.elrr.datasync.entity.SyncRecordDetail;
import com.deloitte.elrr.datasync.jpa.service.SyncRecordDetailService;
import com.deloitte.elrr.datasync.jpa.service.SyncRecordService;
import com.deloitte.elrr.datasync.producer.KafkaProducer;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;

/**
 * @author mnelakurti
 *
 */
@ExtendWith(MockitoExtension.class)
@MockitoSettings(strictness = Strictness.LENIENT)
class NewDataServiceTest {

   /**
    *
    */
    @Mock
    private KafkaProducer kafkaProducer;

    /**
     *
     */
    @Mock
    private SyncRecordService syncRecordService;

     /**
     *
     */
    @Mock
    private SyncRecordDetailService syncRecordDetailService;

    /**
     *
     */
    @Mock
    private ObjectMapper mapper = new ObjectMapper();

    @Test
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
        Mockito.doReturn(getSyncRecordDeatils())
        .when(syncRecordDetailService).findBySyncRecordId(1L);
        Mockito.doReturn(getLearnerChange())
        .when(mapper).readValue(getSyncRecordDeatils().get(0).getLearner(),
                LearnerChange.class);
        newDataService.process();
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
        Mockito.doReturn(getSyncRecordDeatils())
        .when(syncRecordDetailService).findBySyncRecordId(1L);
        //Mockito.doReturn(getLearnerChange())
        //.when(mapper).readValue(getSyncRecordDeatils().get(0).getLearner(),
        //LearnerChange.class);
        newDataService.process();
    }
    /**
    *
    * @return ElrrStatement[]
    */
   public static  List<SyncRecord> getSyncRecord() {
       List<SyncRecord> syncList = new ArrayList<>();
       SyncRecord syncRecord = new SyncRecord();
       syncRecord.setSyncRecordId(1L);
       syncList.add(syncRecord);
       return syncList;
     }

   /**
   *
   * @return ElrrStatement[]
   */
  public static  List<SyncRecordDetail> getSyncRecordDeatils() {
      List<SyncRecordDetail> detailsList = new ArrayList<>();
      SyncRecordDetail syncRecordDetail = new SyncRecordDetail();
      syncRecordDetail.setSyncRecordId(1L);
      syncRecordDetail.setLearner("mailto:c.cooper@yahoo.com");
      detailsList.add(syncRecordDetail);
      return detailsList;
    }

  /**
  *
  * @return ElrrStatement[]
  */
 public static LearnerChange getLearnerChange() {
     List<UserCourse> userCourseList = new ArrayList<>();
     userCourseList.add(new UserCourse());
     LearnerChange learnerChange = new LearnerChange();
     learnerChange.setContactEmailAddress("c.cooper@yahoo.com");
     learnerChange.setName("Cristopher Cunningham");
     learnerChange.setCourses(userCourseList);
     return learnerChange;
   }
}
