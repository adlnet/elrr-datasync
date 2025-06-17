package com.deloitte.elrr.datasync;

import java.util.Properties;
import java.util.Set;

import org.apache.kafka.clients.admin.AdminClient;
import org.apache.kafka.clients.admin.ListTopicsOptions;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import lombok.extern.slf4j.Slf4j;

@Component
@Slf4j
public class KafkaStatusCheck {

    @Value("${brokerUrl}")
    private String brokerUrl;

    /**
     * @return boolean
     */
    public boolean isKafkaRunning() {

        Properties properties = new Properties();
        properties.put("bootstrap.servers", brokerUrl);

        try (AdminClient adminClient = AdminClient.create(properties)) {
            Set<String> topics = adminClient.listTopics(new ListTopicsOptions())
                    .names().get();
            log.info("Kafka is running. Available topics: " + topics);
            return true;
        } catch (Exception e) {
            log.error("Kafka is not running: " + e.getMessage());
            return false;
        }
    }
}
