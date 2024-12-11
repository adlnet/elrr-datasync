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

import com.deloitte.elrr.datasync.dto.ElrrStatement;
import com.fasterxml.jackson.databind.ObjectMapper;

import lombok.extern.slf4j.Slf4j;

@Service
@Slf4j
public class LRSService {
   /**
    *
    */
  @Autowired
  private RestTemplate restTemplate;
  /**
   *
   */
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
   * @return ElrrStatement[]
   */
  public ElrrStatement[] process(final Timestamp startDate) {
    return invokeLRS(startDate);
  }

  // @Bean
  /**
   *
   * @param startDate
   * @return ElrrStatement[]
   */
  private ElrrStatement[] invokeLRS(final Timestamp startDate) {
    ElrrStatement[] statements = null;
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

      ObjectMapper mapper = new ObjectMapper();
      statements = mapper.readValue(json.getBody(), ElrrStatement[].class);
      
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
