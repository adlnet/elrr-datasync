package com.deloitte.elrr.datasync.producer;

import java.util.HashMap;
import java.util.Map;

import org.apache.kafka.clients.producer.ProducerConfig;
import org.apache.kafka.common.serialization.StringSerializer;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.core.DefaultKafkaProducerFactory;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.core.ProducerFactory;

import lombok.extern.slf4j.Slf4j;

@Configuration
@Slf4j
public class KafkaProducerConfig {

    @Value("${brokerUrl}")
    private String brokerUrl;

    @Value("${kafka.batch.size}")
    private int batchSize;

    @Value("${kafka.linger}")
    private int linger;

    /**
     * @return ProducerFactory<String, String>
     */
    @Bean
    public ProducerFactory<String, String> producerFactory() {
        log.info("Start building Kafka Producer factory");
        Map<String, Object> configProps = new HashMap<>();
        configProps.put(ProducerConfig.BOOTSTRAP_SERVERS_CONFIG, brokerUrl);
        configProps.put(ProducerConfig.KEY_SERIALIZER_CLASS_CONFIG,
                StringSerializer.class);
        configProps.put(ProducerConfig.VALUE_SERIALIZER_CLASS_CONFIG,
                StringSerializer.class);

        // Increase batch size and linger
        configProps.put(ProducerConfig.BATCH_SIZE_CONFIG, batchSize);
        configProps.put(ProducerConfig.LINGER_MS_CONFIG, linger);

        // Enable compression
        configProps.put(ProducerConfig.COMPRESSION_TYPE_CONFIG, "gzip");

        return new DefaultKafkaProducerFactory<>(configProps);
    }

    /**
     * @return KafkaTemplate<String, String>
     */
    @Bean
    public KafkaTemplate<String, String> kafkaTemplate() {
        return new KafkaTemplate<>(producerFactory());
    }
}
