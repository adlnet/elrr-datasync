package com.deloitte.elrr.datasync.service;

import java.sql.Timestamp;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.TimeZone;

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
    public Statement[] process(final Timestamp startDate) {

        Statement[] statements = null;

        try {

            // Get new statements from LRS since import.startdate
            statements = invokeLRS(startDate);

        } catch (DatasyncException e) {
            log.error("Error getting statements from LRS.", e);
            throw e;
        }

        return statements;
    }

    /**
     * @param Timestamp
     * @return Statement[]
     * @throws DatasyncException
     */
    private Statement[] invokeLRS(final Timestamp startDate) {

        Statement[] statements = null;

        try {

            // Format import.startdate date (yyyy-MM-DDThh:mm:ssZ)
            String lastReadDate = formatStoredDate(startDate);

            HttpHeaders httpHeaders = new HttpHeaders();
            httpHeaders.add("Cookie", lrsCookie);
            httpHeaders.add("X-Forwarded-Proto", "https");
            httpHeaders.add("Content-Type", "application/json");

            // Call LRS (ELRRStagrController.localdata() in elrrexternalservices)
            // passing import.startdate = stored date
            String completeURL = lrsURL + "/api/lrsdata?lastReadDate=" + lastReadDate;

            HttpEntity<String> entity = new HttpEntity<>("body", httpHeaders);
            ResponseEntity<String> json = restTemplate.exchange(completeURL, HttpMethod.GET, entity, String.class);

            ObjectMapper mapper = Mapper.getMapper();
            statements = mapper.readValue(json.getBody(), Statement[].class);

            log.info(Integer.toString(statements.length) + " statements.");

        } catch (DatasyncException | RestClientException | JsonProcessingException e) {
            log.error("Error calling LRS.", e);
            e.printStackTrace();
            throw new DatasyncException("Error calling LRS.", e);
        }

        return statements;
    }

    /**
     * @param startDate
     * @return lastReadDate
     * @throws DatasyncException
     */
    private String formatStoredDate(final Timestamp startDate) {

        String lastReadDate = null;

        try {

            DateFormat formatter = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'");

            // Convert Timestamp to Long (ms)
            long startDateLong = startDate.getTime();

            // Convert Long to Date
            Date date = new Date(startDateLong);

            // Convert to GMT
            formatter.setTimeZone(TimeZone.getTimeZone("GMT"));
            lastReadDate = formatter.format(date);
            log.info("Last read date = " + lastReadDate);

        } catch (IllegalArgumentException | NullPointerException e) {
            log.error("Error formatting last read date.", e);
            e.printStackTrace();
            throw new DatasyncException("Error formatting last read date.", e);
        }

        return lastReadDate;
    }
}
