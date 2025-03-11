package com.deloitte.elrr.datasync.producer;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import lombok.extern.slf4j.Slf4j;

@Service
@Slf4j
public class KafkaProducer {

  @Value("${kafka.topic}")
  private String kafkatopic;

  @Autowired private KafkaTemplate<String, String> kafkaTemplate;

  private ObjectMapper mapper = new ObjectMapper();

  /**
   * @param msg
   */
  public void sendMessage(final Object msg) {
    try {
      String payload = "";
      if (msg instanceof String) {
        payload = (String) msg;
      } else {
        payload = writeValueAsString(msg);
      }
      log.info("payload sent messsage to Kafka" + payload);
      kafkaTemplate.send(kafkatopic, payload);
      log.info("payload sent to kafka successfully to kafka topic " + kafkatopic);
    } catch (Exception e) {
      log.error("Exception while sending message to Kafka " + e.getMessage());
      e.getStackTrace();
    }
  }

  /**
   * @param data
   * @return String
   */
  public String writeValueAsString(final Object data) {
    String output = "";
    try {
      output = mapper.writeValueAsString(data);
    } catch (JsonProcessingException e) {
      log.error("Exception whille converting to JSON " + e.getMessage());
      e.fillInStackTrace();
    }
    return output;
  }
}
