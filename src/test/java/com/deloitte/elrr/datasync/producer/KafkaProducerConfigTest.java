package com.deloitte.elrr.datasync.producer;

import static org.junit.jupiter.api.Assertions.assertNotNull;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;

import lombok.extern.slf4j.Slf4j;

@ExtendWith(MockitoExtension.class)
@Slf4j
class KafkaProducerConfigTest {

    @Test
    void test() {

        KafkaProducerConfig kafkaProducerConfig = new KafkaProducerConfig();
        assertNotNull(kafkaProducerConfig);
    }
}
