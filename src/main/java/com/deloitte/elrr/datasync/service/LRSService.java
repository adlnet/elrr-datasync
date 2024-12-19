package com.deloitte.elrr.datasync.service;

import java.sql.Timestamp;
import java.text.DateFormat;
import java.text.SimpleDateFormat;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

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

  /*
   * This process is to get the deltas
   */
  // 06/01 -- 06/22
  /**
   *
   * @param startDate
   * @return Statement[]
   */
  public Statement[] process(final Timestamp startDate) {  // PHL
    return invokeLRS(startDate);
  }

  // @Bean
  /**
   *
   * @param startDate
   * @return Statement[]
   */
  private Statement[] invokeLRS(final Timestamp startDate) {  // PHL
    Statement[] statements = null;  // PHL
    try {
      String lastReadDate = formatDate(startDate) + "T00:00:00Z";
      log.info("lastReadDate " + lastReadDate);

      HttpHeaders httpHeaders = new HttpHeaders();
      httpHeaders.add("Cookie",lrsCookie);
      httpHeaders.add("X-Forwarded-Proto", "https");
      httpHeaders.add("Content-Type", "application/json");

      String completeURL = lrsURL + "/api/lrsdata?lastReadDate=" + lastReadDate;

      HttpEntity<String> entity = new HttpEntity<>("body", httpHeaders);
      ResponseEntity<String> json = restTemplate.exchange(completeURL, HttpMethod.GET, entity, String.class);

      ObjectMapper mapper = Mapper.getMapper();  // PHL
      statements = mapper.readValue(json.getBody(), Statement[].class);  // PHL
      
    } catch (Exception e) {
      e.printStackTrace();
    }

    return statements;
  }
  
  /**
   *
   * @param timestamp
   * @return String
   */
  private String formatDate(final Timestamp timestamp) {
    DateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");

    return formatter.format(timestamp);
  }
}
