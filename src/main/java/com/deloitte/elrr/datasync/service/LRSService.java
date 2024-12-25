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
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
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
      // Format import.startdate date (yyyy-MM-DDThh:mm:ssZ)
      String lastReadDate = formatStoredDate(startDate);
      log.info("==> lastReadDate = " + lastReadDate);

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

          int statusCode = json.getStatusCode().value();
          
          if (statusCode != 200) {
        	  log.info("==> LRS statusCode = " + statusCode);
        	  return statements;
          }
          
          ObjectMapper mapper = Mapper.getMapper();  // PHL
          statements = mapper.readValue(json.getBody(), Statement[].class);
          
          log.info("==> statements size = " + statements.length);
          
      } catch (HttpClientErrorException | HttpServerErrorException | JsonProcessingException e) {
          log.error("==> Error calling LRS " + e.getMessage());
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
  
  /**
  *
  * @param startDate
  * @return lastReadDate
  */
  private String formatStoredDate(Timestamp startDate) {
	  
	  // Call LRS passing import.startdate = stored date - first time get everything after midnight of import.startdate
	  String lastReadDate = formatDate(startDate) + "T00:00:00Z";
	  
      Calendar calendar = new GregorianCalendar();
      calendar.setTime(startDate);
      int year = calendar.get(Calendar.YEAR);
    
      // If not first time (year > 2000) get everything after import.startdate
      if (year > 2000) {
    	  
    	int hour = calendar.get(Calendar.HOUR);
        String hr = "";
        if (hour < 10) {
        	hr = "0" + Integer.toString(hour);
        } else {
        	hr = Integer.toString(hour);
        }
          
        int minute = calendar.get(Calendar.MINUTE);
        String min = "";
        if (minute < 10) {
        	min = "0" + Integer.toString(minute);
        } else {
        	min = Integer.toString(minute);
        }
          
        int second = calendar.get(Calendar.SECOND);
        String sec = "";
        if (second < 10) {
        	sec = "0" + Integer.toString(second);
        } else {
        	sec = Integer.toString(second);
        }
          
        // Format import.startdate date (yyyy-MM-DDThh:mm:ssZ)
        lastReadDate = formatDate(startDate) + "T" + hr + ":" + min + ":" + sec + "Z";
        
      }

	  return lastReadDate;
  }
}
