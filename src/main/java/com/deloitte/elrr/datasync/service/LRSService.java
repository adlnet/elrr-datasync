package com.deloitte.elrr.datasync.service;

import java.sql.Timestamp;
import java.text.DateFormat;
import java.text.SimpleDateFormat;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import com.deloitte.elrr.datasync.dto.ElrrStatement;
import com.fasterxml.jackson.core.JsonProcessingException;
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
      String json = restTemplate.getForObject(
        lrsURL + "/api/lrsdata?lastReadDate=" + lastReadDate,
        String.class
      );
      log.info("number of statements received " + json);
      ObjectMapper mapper = new ObjectMapper();
      statements = mapper.readValue(json, ElrrStatement[].class);
      log.info("parsed successfully " + statements.length);
    } catch (JsonProcessingException e) {
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
