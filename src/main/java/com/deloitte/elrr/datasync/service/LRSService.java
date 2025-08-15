package com.deloitte.elrr.datasync.service;

import java.time.ZonedDateTime;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestTemplate;

import com.deloitte.elrr.datasync.exception.DatasyncException;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.yetanalytics.xapi.model.Statement;
import com.yetanalytics.xapi.util.Mapper;

import lombok.extern.slf4j.Slf4j;

@Service
@Slf4j
public class LRSService {

    @Autowired
    private RestTemplate restTemplate;

    @Value("${lrsservice.url}")
    private String lrsURL;

    @Value("${lrsservice.cookie}")
    private String lrsCookie;

    /**
     * @param startDate
     * @return Statement[]
     * @throws DatasyncException
     */
    // Get xAPI statements from LRS
    public Statement[] process(final ZonedDateTime startDate)
            throws DatasyncException {

        Statement[] statements = null;

        // Get new statements from LRS since import.startdate
        statements = invokeLRS(startDate);

        return statements;
    }

    /**
     * @param startDate
     * @return statements
     * @throws DatasyncException
     */
    public Statement[] invokeLRS(final ZonedDateTime startDate) {

        Statement[] statements = null;

        try {

            HttpHeaders httpHeaders = new HttpHeaders();
            httpHeaders.add("Cookie", lrsCookie);
            httpHeaders.add("X-Forwarded-Proto", "https");
            httpHeaders.add("Content-Type", "application/json");
            log.info("Http headers: " + httpHeaders);

            // Call LRS
            // passing import.startdate = stored date
            String completeURL = lrsURL + "/api/lrsdata?lastReadDate="
                    + startDate;
            log.info("Complete URL: " + completeURL);

            HttpEntity<String> entity = new HttpEntity<>(httpHeaders);
            ResponseEntity<String> json = restTemplate.exchange(completeURL,
                    HttpMethod.GET, entity, String.class);
            log.info("Res status code: " + json.getStatusCode());
            log.info("Res headers: " + json.getHeaders());
            log.info("Res body: " + json.getBody());

            ObjectMapper mapper = Mapper.getMapper();
            statements = mapper.readValue(json.getBody(), Statement[].class);

            log.info(Integer.toString(statements.length) + " statements.");

        } catch (DatasyncException | RestClientException
                | JsonProcessingException e) {
            e.printStackTrace();
            throw new DatasyncException("Error calling LRS.", e);
        }

        return statements;
    }

}
