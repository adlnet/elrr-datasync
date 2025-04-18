package com.deloitte.elrr.datasych.test.scheduler;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import com.deloitte.elrr.datasync.exception.DatasyncException;
import com.deloitte.elrr.datasync.exception.ResourceNotFoundException;
import com.deloitte.elrr.datasync.jpa.service.ImportService;
import com.deloitte.elrr.datasync.scheduler.LRSSyncSchedulingService;
import com.deloitte.elrr.datasync.service.LRSService;
import com.deloitte.elrr.datasync.service.NewDataService;

import lombok.extern.slf4j.Slf4j;

@ExtendWith(MockitoExtension.class)
@Slf4j
class LRSSyncSchedulingServiceTest {

  @Mock private LRSService lrsService;

  @Mock private NewDataService newDataService;

  @Mock private ImportService importService;

  @InjectMocks LRSSyncSchedulingService lrsSyncSchedulingservice;

  @Test
  void test() {

    try {

      lrsSyncSchedulingservice.run();

    } catch (DatasyncException | ResourceNotFoundException e) {
      e.printStackTrace();
    }
  }
}
