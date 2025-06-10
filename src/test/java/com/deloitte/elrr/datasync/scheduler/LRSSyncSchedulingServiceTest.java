package com.deloitte.elrr.datasync.scheduler;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.fail;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doNothing;

import java.io.File;
import java.io.IOException;
import java.util.UUID;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

import com.deloitte.elrr.datasync.entity.Import;
import com.deloitte.elrr.datasync.exception.DatasyncException;
import com.deloitte.elrr.datasync.exception.ResourceNotFoundException;
import com.deloitte.elrr.datasync.jpa.service.ImportService;
import com.deloitte.elrr.datasync.service.LRSService;
import com.deloitte.elrr.datasync.service.NewDataService;
import com.deloitte.elrr.datasync.util.LogCapture;
import com.deloitte.elrr.datasync.util.LogCaptureExtension;
import com.deloitte.elrr.datasync.util.TestFileUtil;
import com.yetanalytics.xapi.model.Statement;
import com.yetanalytics.xapi.util.Mapper;

import lombok.extern.slf4j.Slf4j;

@ExtendWith({ MockitoExtension.class, LogCaptureExtension.class })
@Slf4j
class LRSSyncSchedulingServiceTest {

    @Mock
    private LRSService lrsService;

    @Mock
    private NewDataService newDataService;

    @Mock
    private ImportService importService;

    @InjectMocks
    private LRSSyncSchedulingService lrsSyncSchedulingservice;

    @Test
    void testImportAlreadyExists() {

        try {

            File testFile = TestFileUtil.getJsonTestFile("completed.json");

            Statement[] stmts = Mapper.getMapper().readValue(testFile,
                    Statement[].class);
            assertTrue(stmts != null);

            Import imp = new Import();
            imp.setId(UUID.randomUUID());
            imp.setImportName(StatusConstants.LRSNAME);
            imp.setRecordStatus(StatusConstants.SUCCESS);
            imp.setRetries(0);
            Mockito.doReturn(imp).when(importService).findByName(any());

            doNothing().when(newDataService).process(any());

            Mockito.doReturn(stmts).when(lrsService).process(any());

            lrsSyncSchedulingservice.run();

        } catch (DatasyncException | ResourceNotFoundException
                | IOException e) {
            fail("Should not have thrown any exception");
        }
    }

    @Test
    void testImportDoesNotExists() {

        try {

            File testFile = TestFileUtil.getJsonTestFile("completed.json");

            Statement[] stmts = Mapper.getMapper().readValue(testFile,
                    Statement[].class);
            assertTrue(stmts != null);

            Import imp = new Import();
            imp.setId(UUID.randomUUID());
            imp.setImportName(StatusConstants.LRSNAME);
            imp.setRecordStatus(StatusConstants.SUCCESS);
            imp.setRetries(0);
            Mockito.doReturn(null).when(importService).findByName(any());

            doNothing().when(newDataService).process(any());

            Mockito.doReturn(stmts).when(lrsService).process(any());

            lrsSyncSchedulingservice.run();

        } catch (DatasyncException | ResourceNotFoundException
                | IOException e) {
            e.printStackTrace();
            fail("Should not have thrown any exception");
        }
    }

    @Test
    @SuppressWarnings("checkstyle:linelength")
    void testLogging(LogCapture logCapture) {

        try {

            logCapture.clear();

            File testFile = TestFileUtil.getJsonTestFile("completed.json");

            Statement[] stmts = Mapper.getMapper().readValue(testFile,
                    Statement[].class);
            assertTrue(stmts != null);

            Import imp = new Import();
            imp.setId(UUID.randomUUID());
            imp.setImportName(StatusConstants.LRSNAME);
            imp.setRecordStatus(StatusConstants.SUCCESS);
            imp.setRetries(0);
            Mockito.doReturn(imp).when(importService).findByName(any());

            doNothing().when(newDataService).process(any());

            Mockito.doReturn(stmts).when(lrsService).process(any());

            lrsSyncSchedulingservice.run();
            assertThat(logCapture.getLoggingEvents()).hasSize(2);
            assertEquals(logCapture.getFirstFormattedMessage(),
                    "===============inside LRS schedule method.===============\n");

        } catch (DatasyncException | ResourceNotFoundException
                | IOException e) {
            fail("Should not have thrown any exception");
        }
    }

}
