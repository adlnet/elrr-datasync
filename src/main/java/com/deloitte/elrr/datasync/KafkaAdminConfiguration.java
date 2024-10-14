package com.deloitte.elrr.datasync;

import org.apache.kafka.clients.admin.NewTopic;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.config.TopicBuilder;

/**
 *
 */
@Configuration
public class KafkaAdminConfiguration {

    /**
     *
     */
    //private static final int KAFKA_PARTITIONS = 6; // PHL
    private static final int KAFKA_PARTITIONS = 1;   // PHL

    /**
     *
     */
    //private static final int KAFKA_RIPLICAS = 6; // PHL
    private static final int KAFKA_RIPLICAS = 1;   // PHL

    /**
     *
     * @return NewTopic
     */
    @Bean
    public NewTopic topicExample() {
        return TopicBuilder.name("test-1")
                .partitions(KAFKA_PARTITIONS)
                .replicas(KAFKA_RIPLICAS)
                .build();
    }
}
