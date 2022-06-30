/**
 *
 */
package com.deloitte.elrr.datasync.producer;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.kafka.core.KafkaTemplate;

/**
 * @author mnelakurti
 *
 */
@ExtendWith(MockitoExtension.class)
class KafkaProducerTest {

    /**
     *
     */
    @Mock
    private KafkaProducer mockKafkaProducer;

    /**
     *
     */
    @Mock
    private KafkaTemplate<String, String> kafkaTemplate;


    @Test
    void test() {
        mockKafkaProducer.sendMessage(new Object());
    }

}
