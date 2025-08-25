package com.deloitte.elrr.datasync.scheduler;

import static org.assertj.core.api.Assertions.fail;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;

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
import com.deloitte.elrr.datasync.entity.types.RecordStatus;
import com.deloitte.elrr.datasync.exception.DatasyncException;
import com.deloitte.elrr.datasync.exception.ResourceNotFoundException;
import com.deloitte.elrr.datasync.jpa.service.ImportService;
import com.deloitte.elrr.datasync.service.LRSService;
import com.deloitte.elrr.datasync.service.NewDataService;
import com.deloitte.elrr.datasync.util.TestFileUtil;
import com.yetanalytics.xapi.model.Statement;
import com.yetanalytics.xapi.util.Mapper;

import lombok.extern.slf4j.Slf4j;

@ExtendWith(MockitoExtension.class)
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

    private static final String LRSNAME = "Yet Analytics LRS";

    @Test
    void testImportAlreadyExists() {

        try {

            File testFile = TestFileUtil.getJsonTestFile("completed.json");

            Statement[] stmts = Mapper.getMapper().readValue(testFile,
                    Statement[].class);
            assertTrue(stmts != null);

            Import imp = new Import();
            imp.setId(UUID.randomUUID());
            imp.setImportName(LRSNAME);
            imp.setRecordStatus(RecordStatus.SUCCESS);
            imp.setRetries(0);
            Mockito.doReturn(imp).when(importService).findByName(any());

            lrsSyncSchedulingservice.run();

        } catch (DatasyncException | ResourceNotFoundException
                | IOException e) {
            fail("Should not have thrown any exception");
        }
    }

    @Test
    void testImportInProcess() {

        try {

            File testFile = TestFileUtil.getJsonTestFile("completed.json");

            Statement[] stmts = Mapper.getMapper().readValue(testFile,
                    Statement[].class);
            assertTrue(stmts != null);

            Import imp = new Import();
            imp.setId(UUID.randomUUID());
            imp.setImportName(LRSNAME);
            imp.setRecordStatus(RecordStatus.INPROCESS);
            imp.setRetries(0);
            Mockito.doReturn(imp).when(importService).findByName(any());

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
            imp.setImportName(LRSNAME);
            imp.setRecordStatus(RecordStatus.SUCCESS);
            imp.setRetries(0);
            Mockito.doReturn(null).when(importService).findByName(any());

            lrsSyncSchedulingservice.run();

        } catch (DatasyncException | IOException e) {
            assertEquals("Error LRS sync failed.", e.getMessage());
        }
    }

    @Test
    void testSuccessfulExecution() {

        try {

            File testFile = TestFileUtil.getJsonTestFile("completed.json");

            Statement[] stmts = Mapper.getMapper().readValue(testFile,
                    Statement[].class);
            assertTrue(stmts != null);

            Import imp = new Import();
            imp.setId(UUID.randomUUID());
            imp.setImportName(LRSNAME);
            imp.setRecordStatus(RecordStatus.SUCCESS);
            imp.setRetries(0);
            Mockito.doReturn(imp).when(importService).findByName(any());
            Mockito.doReturn(imp).when(importService).updateImportStatus(any(),
                    any());
            Mockito.doReturn(stmts).when(lrsService).process(any());

            lrsSyncSchedulingservice.run();

        } catch (DatasyncException | ResourceNotFoundException
                | IOException e) {
            assertEquals("Error LRS sync failed.", e.getMessage());
        }
    }

    /**
     *
     * @throws IOException
     */
    @Test
    void testDatasyncException() throws IOException {

        try {

            File testFile = TestFileUtil.getJsonTestFile("completed.json");

            Statement[] stmts = Mapper.getMapper().readValue(testFile,
                    Statement[].class);
            assertTrue(stmts != null);

            Import imp = new Import();
            imp.setId(UUID.randomUUID());
            imp.setImportName(LRSNAME);
            imp.setRecordStatus(RecordStatus.SUCCESS);
            imp.setRetries(0);
            Mockito.doReturn(imp).when(importService).findByName(any());
            Mockito.doThrow(new DatasyncException("Test Error")).when(
                    lrsService).process(any());

            lrsSyncSchedulingservice.run();

        } catch (DatasyncException e) {
            assertEquals("Error LRS sync failed.", e.getMessage());
        }

    }

    /**
     *
     * @throws IOException
     */
    @Test
    void testResourceNotFoundException() throws IOException {

        try {

            File testFile = TestFileUtil.getJsonTestFile("completed.json");

            Statement[] stmts = Mapper.getMapper().readValue(testFile,
                    Statement[].class);
            assertTrue(stmts != null);

            Import imp = new Import();
            imp.setId(UUID.randomUUID());
            imp.setImportName(LRSNAME);
            imp.setRecordStatus(RecordStatus.SUCCESS);
            imp.setRetries(0);
            Mockito.doReturn(imp).when(importService).findByName(any());
            Mockito.doThrow(new ResourceNotFoundException("Test Error")).when(
                    lrsService).process(any());

            lrsSyncSchedulingservice.run();

        } catch (DatasyncException | NullPointerException
                | ResourceNotFoundException e) {
            assertEquals("Error LRS sync failed.", e.getMessage());
        }
    }

    /**
     *
     * @throws IOException
     */
    @Test
    void testNullPointerException() throws IOException {

        try {

            File testFile = TestFileUtil.getJsonTestFile("completed.json");

            Statement[] stmts = Mapper.getMapper().readValue(testFile,
                    Statement[].class);
            assertTrue(stmts != null);

            Import imp = new Import();
            imp.setId(UUID.randomUUID());
            imp.setImportName(LRSNAME);
            imp.setRecordStatus(RecordStatus.SUCCESS);
            imp.setRetries(0);
            Mockito.doReturn(imp).when(importService).findByName(any());
            Mockito.doThrow(new NullPointerException("Test Error")).when(
                    lrsService).process(any());

            lrsSyncSchedulingservice.run();

        } catch (DatasyncException | NullPointerException
                | ResourceNotFoundException e) {
            assertEquals("Error LRS sync failed.", e.getMessage());
        }
    }

    /**
     *
     * @throws IOException
     */
    @Test
    void testGenericException() throws IOException {

        try {

            File testFile = TestFileUtil.getJsonTestFile("completed.json");

            Statement[] stmts = Mapper.getMapper().readValue(testFile,
                    Statement[].class);
            assertTrue(stmts != null);

            Import imp = new Import();
            imp.setId(UUID.randomUUID());
            imp.setImportName(LRSNAME);
            imp.setRecordStatus(RecordStatus.SUCCESS);
            imp.setRetries(0);
            Mockito.doReturn(imp).when(importService).findByName(any());
            Mockito.doThrow(new RuntimeException("Test Error")).when(lrsService)
                    .process(any());

            lrsSyncSchedulingservice.run();

        } catch (RuntimeException e) {
            assertEquals("Test Error", e.getMessage());
        }

    }

}
