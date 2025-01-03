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

  /**
   * PHL
   *
   * @param startDate
   * @return Statement[]
   */
  public Statement[] process(final Timestamp startDate) {
    return invokeLRS(startDate);
  }

  // @Bean
  /**
   * PHL
   * @param startDate
   * @return Statement[]
   */
  private Statement[] invokeLRS(final Timestamp startDate) {
      Statement[] statements = null; 
      
      // Format import.startdate date (yyyy-MM-DDThh:mm:ssZ)
      String lastReadDate = formatStoredDate(startDate);
      
      try {
         
          HttpHeaders httpHeaders = new HttpHeaders();
          httpHeaders.add("Cookie",lrsCookie);
          httpHeaders.add("X-Forwarded-Proto", "https");
          httpHeaders.add("Content-Type", "application/json");

          // Call LRS passing import.startdate = stored date 
          String completeURL = lrsURL + "/api/lrsdata?lastReadDate=" + lastReadDate;
          log.info("==> URL = " + completeURL);

          HttpEntity<String> entity = new HttpEntity<>("body", httpHeaders);
          ResponseEntity<String> json = restTemplate.exchange(completeURL, HttpMethod.GET, entity, String.class);

          ObjectMapper mapper = Mapper.getMapper();
          statements = mapper.readValue(json.getBody(), Statement[].class);
          
          log.info("==> statements size = " + statements.length);
          
      } catch (HttpClientErrorException | HttpServerErrorException | JsonProcessingException e) {
          log.error("==> Error calling LRS " + e.getMessage());
          e.getStackTrace();
      }
      
      return statements;
  }
  
  /**
  * PHL
  * @param startDate
  * @return lastReadDate
  */
  private String formatStoredDate(Timestamp startDate) {

      DateFormat formatter = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'");  
      
      // Convert timestamp to Long (ms)
      long startDateLong = startDate.getTime();
      
      // Convert Long to Date
      Date date = new Date(startDateLong);
      
      // Convert to GMT
      formatter.setTimeZone(TimeZone.getTimeZone("GMT"));
      String lastReadDate = formatter.format(date);
      log.info("==> lastReadDate = " + lastReadDate);
	  
	  return lastReadDate;
  }
  
}
