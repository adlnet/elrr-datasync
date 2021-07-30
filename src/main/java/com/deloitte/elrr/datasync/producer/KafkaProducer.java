package com.deloitte.elrr.datasync.producer;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import lombok.extern.slf4j.Slf4j;

@Service
@Slf4j
public class KafkaProducer {
	
	
	@Autowired
	private KafkaTemplate<String, String> kafkaTemplate;
	
	private ObjectMapper mapper = new ObjectMapper();

	public void sendMessage(Object msg) {
	   String payload = "";
	   if (msg instanceof String) {
		   payload = (String) msg;
	   } else {
		   payload = writeValueAsString(msg);
	   }
	   log.info("payload send to Kafka"+payload);
	   kafkaTemplate.send("test", payload);
	}   

	private String writeValueAsString(Object data) {
		String output = "";
		try {
			output = mapper.writeValueAsString(data);
		} catch (JsonProcessingException e) {
			log.error("Exception whille converting to JSON "+e.getMessage());
		}
		return output;
	}
}
