package com.deloitte.elrr.datasync.service;

import static org.assertj.core.api.Assertions.fail;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;

import java.io.File;
import java.io.IOException;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.Spy;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestTemplate;

import com.deloitte.elrr.datasync.exception.DatasyncException;
import com.deloitte.elrr.datasync.util.PrettyJson;
import com.deloitte.elrr.datasync.util.TestFileUtil;
import com.yetanalytics.xapi.model.Statement;
import com.yetanalytics.xapi.util.Mapper;

import lombok.extern.slf4j.Slf4j;

@ExtendWith(MockitoExtension.class)
@Slf4j
class LRSServiceTest {

    @Mock
    private RestTemplate restTemplate;

    @Spy
    private PrettyJson prettyJson;

    @InjectMocks
    private LRSService lrsService;

    @Test
    void testProcessSuccess() {

        try {

            File testFile = TestFileUtil.getJsonTestFile("completed.json");

            Statement[] stmts = Mapper.getMapper().readValue(testFile,
                    Statement[].class);
            assertTrue(stmts != null);

            ZonedDateTime zonedDateTime = ZonedDateTime.parse(
                    "2025-12-05T15:30:00Z", DateTimeFormatter.ISO_DATE_TIME);

            HttpHeaders httpHeaders = new HttpHeaders();
            httpHeaders.add("Cookie", "null");
            httpHeaders.add("X-Forwarded-Proto", "https");
            httpHeaders.add("Content-Type", "application/json");

            String completeURL = "null/api/lrsdata?lastReadDate="
                    + zonedDateTime + "&maxStatements=0";

            HttpEntity<String> entity = new HttpEntity<>(httpHeaders);

            ResponseEntity<String> json = new ResponseEntity<String>(Mapper
                    .getMapper().writeValueAsString(stmts), HttpStatus.OK);
            assertNotNull(json);

            Mockito.doReturn(json).when(restTemplate).exchange(eq(completeURL),
                    eq(HttpMethod.GET), any(HttpEntity.class), eq(
                            String.class));

            Statement[] returnStmts = lrsService.process(zonedDateTime);

        } catch (DatasyncException | NullPointerException | IOException
                | RestClientException e) {
            fail("Should not have thrown any exception");
        }
    }

    @Test
    void testProcessError() {

        try {

            File testFile = TestFileUtil.getJsonTestFile("completed.json");

            Statement[] stmts = Mapper.getMapper().readValue(testFile,
                    Statement[].class);
            assertTrue(stmts != null);

            ZonedDateTime zonedDateTime = ZonedDateTime.parse(
                    "2025-12-05T15:30:00Z", DateTimeFormatter.ISO_DATE_TIME);

            HttpHeaders httpHeaders = new HttpHeaders();
            httpHeaders.add("Cookie", "null");
            httpHeaders.add("X-Forwarded-Proto", "https");
            httpHeaders.add("Content-Type", "application/json");

            String completeURL = "null/api/lrsdata?lastReadDate="
                    + zonedDateTime + "&maxStatements=0";

            HttpEntity<String> entity = new HttpEntity<>(httpHeaders);

            ResponseEntity<String> json = new ResponseEntity<String>(Mapper
                    .getMapper().writeValueAsString(stmts), HttpStatus.OK);
            assertNotNull(json);

            Mockito.doThrow(new DatasyncException("Test Error")).when(
                    restTemplate).exchange(eq(completeURL), eq(HttpMethod.GET),
                            any(HttpEntity.class), eq(String.class));

            Statement[] returnStmts = lrsService.process(zonedDateTime);

        } catch (DatasyncException | IOException e) {
            assertEquals("Error calling LRS.", e.getMessage());
        }
    }

}
