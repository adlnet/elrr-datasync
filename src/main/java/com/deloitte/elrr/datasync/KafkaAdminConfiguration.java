package com.deloitte.elrr.datasync;

import org.apache.kafka.clients.admin.NewTopic;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.config.TopicBuilder;

@Configuration
public class KafkaAdminConfiguration {

    @Value("${kafka.partitions}")
    private int kafkaPartitions;

    @Value("${kafka.replicas}")
    private int kafkaReplicas;

    @Value("${kafka.topic}")
    private String topic;

    @Value("${kafka.dead.letter.topic}")
    private String deadLetterTopic;

    /**
     * @return NewTopic
     */
    @Bean
    public NewTopic topicExample() {
        return TopicBuilder.name(topic).partitions(kafkaPartitions).replicas(
                kafkaReplicas).build();
    }

    /**
     * @return NewTopic
     */
    @Bean
    public NewTopic topicDeadLetterQueue() {
        return TopicBuilder.name(deadLetterTopic).partitions(kafkaPartitions)
                .replicas(kafkaReplicas).build();
    }
}
