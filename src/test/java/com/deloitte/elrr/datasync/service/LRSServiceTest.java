package com.deloitte.elrr.datasync.service;

import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.io.File;
import java.io.IOException;
import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.web.client.RestTemplate;

import com.deloitte.elrr.datasync.exception.DatasyncException;
import com.deloitte.elrr.test.datasync.util.TestFileUtils;
import com.yetanalytics.xapi.model.Statement;
import com.yetanalytics.xapi.util.Mapper;

import lombok.extern.slf4j.Slf4j;

@ExtendWith(MockitoExtension.class)
@Slf4j
class LRSServiceTest {

    @Mock
    private RestTemplate restTemplate;

    @InjectMocks
    private LRSService lrsService;

    @Test
    void test() {

        try {

            File testFile = TestFileUtils.getJsonTestFile("completed.json");

            Statement[] stmts = Mapper.getMapper().readValue(testFile,
                    Statement[].class);
            assertTrue(stmts != null);

            LocalDateTime localDateTime = LocalDateTime.parse(
                    "2025-12-05T15:30:00Z", DateTimeFormatter.ISO_DATE_TIME);

            Timestamp timestamp = Timestamp.valueOf(localDateTime);

            String strDate = lrsService.formatStoredDate(timestamp);
            assertNotNull(strDate);

        } catch (DatasyncException | NullPointerException | IOException e) {
            e.printStackTrace();
        }
    }
}
