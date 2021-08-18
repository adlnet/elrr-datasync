package com.deloitte.elrr.datasync.service;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.sql.Timestamp;
import java.util.Calendar;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import com.deloitte.elrr.datasync.util.MockLRSData;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import gov.adlnet.xapi.client.StatementClient;
import gov.adlnet.xapi.model.Activity;
import gov.adlnet.xapi.model.Statement;
import gov.adlnet.xapi.model.StatementResult;
import gov.adlnet.xapi.model.Verb;
import lombok.extern.slf4j.Slf4j;

import com.deloitte.elrr.datasync.dto.ElrrStatement;
import com.deloitte.elrr.datasync.dto.ElrrStatementList;

@Service
@Slf4j
public class LRSService {
	
	@Autowired
	RestTemplate restTemplate;
	
	@Value("${lrsservice.url}")
	private String lrsURL;
	/*
	 * This process is to get the deltas 
	 */
	//06/01 -- 06/22
	public ElrrStatement[] process(Timestamp startDate, Timestamp endDate) {
		
		//StatementResult result = MockLRSData.getLearnerStatements();
		//StatementResult result = testLRS();
		return invokeLRS();
		
	}
	
	@Bean
	private ElrrStatement[] invokeLRS() {
		ElrrStatement[] statements = null;
		String json = restTemplate.getForObject(
				lrsURL+"/api/lrsdata?lastReadDate=2021-05-04T00:00:00Z",String.class);
				//"http://ec2-13-59-16-179.us-east-2.compute.amazonaws.com:8088/api/lrsdata?lastReadDate=2021-05-04T00:00:00Z", String.class);
		log.info("number of statements received "+json);
		ObjectMapper mapper = new ObjectMapper();
		try {
			statements = mapper.readValue(json, ElrrStatement[].class);
			log.info("parsed successfully "+statements.length);
		} catch (JsonProcessingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	
		return statements;

	}
 }
