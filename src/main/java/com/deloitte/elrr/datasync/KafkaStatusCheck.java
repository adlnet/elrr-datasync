package com.deloitte.elrr.datasync;

import java.util.Properties;
import java.util.Set;

import org.apache.kafka.clients.admin.AdminClient;
import org.apache.kafka.clients.admin.ListTopicsOptions;
import org.springframework.stereotype.Component;

@Component
public class KafkaStatusCheck {

  public boolean isKafkaRunning() {

    Properties properties = new Properties();
    properties.put("bootstrap.servers", "localhost:9092");

    try (AdminClient adminClient = AdminClient.create(properties)) {
      Set<String> topics = adminClient.listTopics(new ListTopicsOptions()).names().get();
      System.out.println("Kafka is running. Available topics: " + topics);
      return true;
    } catch (Exception e) {
      System.out.println("Kafka is not running: " + e.getMessage());
      return false;
    }
  }
}
