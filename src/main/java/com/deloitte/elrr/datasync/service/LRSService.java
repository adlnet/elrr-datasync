package com.deloitte.elrr.datasync.service;

import java.sql.Timestamp;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.GregorianCalendar;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.HttpServerErrorException;
import org.springframework.web.client.RestTemplate;

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
      // PHL  
      // Call LRS - first time get everything after midnight of import.startdate (2000-12-30 13:08:54.193) 
      String lastReadDate = formatDateOnly(startDate) + "T00:00:00Z";
      
      Calendar calendar = new GregorianCalendar();
      calendar.setTime(startDate);
      int year = calendar.get(Calendar.YEAR);
      int hour = calendar.get(Calendar.HOUR);
      int minute = calendar.get(Calendar.MINUTE);
      int second = calendar.get(Calendar.SECOND);
      
      log.info("==> Year = " + year);
      
      // If not first time (2000-12-30 13:08:54.193) get everything after import.startdate
      if (year > 2000) {
          // Call LRS - get everything after import.startdate
          lastReadDate = formatDateOnly(startDate) + "T" + hour + ":" + minute + ":" + second + "Z";
      }
      
      log.info("==> lastReadDate = " + lastReadDate);

      try {
         
          HttpHeaders httpHeaders = new HttpHeaders();
          httpHeaders.add("Cookie",lrsCookie);
          httpHeaders.add("X-Forwarded-Proto", "https");
          httpHeaders.add("Content-Type", "application/json");

          String completeURL = lrsURL + "/api/lrsdata?lastReadDate=" + lastReadDate;

          HttpEntity<String> entity = new HttpEntity<>("body", httpHeaders);
          ResponseEntity<String> json = restTemplate.exchange(completeURL, HttpMethod.GET, entity, String.class);

          ObjectMapper mapper = Mapper.getMapper();  // PHL
          statements = mapper.readValue(json.getBody(), Statement[].class);
          
      } catch (HttpClientErrorException | HttpServerErrorException | JsonProcessingException e) {
          // Nothing returned from LRS
          log.error("==> Error calling LRS " + e.getMessage());
      }
      
      return statements;
  }
  
  /**
   *
   * @param timestamp
   * @return String
   */
  private String formatDateOnly(final Timestamp timestamp) {
      DateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");

      return formatter.format(timestamp);
    }
}
