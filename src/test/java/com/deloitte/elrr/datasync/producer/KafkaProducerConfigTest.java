package com.deloitte.elrr.datasync.producer;

import static org.junit.jupiter.api.Assertions.assertNotNull;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.kafka.core.ProducerFactory;
import org.springframework.test.util.ReflectionTestUtils;

import lombok.extern.slf4j.Slf4j;

@ExtendWith(MockitoExtension.class)
@Slf4j
class KafkaProducerConfigTest {

    @Test
    void test() {

        KafkaProducerConfig kafkaProducerConfig = new KafkaProducerConfig();
        ReflectionTestUtils.setField(kafkaProducerConfig, "brokerUrl",
                "localhost:9999");
        ProducerFactory<String, String> factory = kafkaProducerConfig
                .producerFactory();
        assertNotNull(kafkaProducerConfig);
        assertNotNull(factory);
    }
}
