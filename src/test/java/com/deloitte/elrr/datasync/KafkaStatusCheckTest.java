package com.deloitte.elrr.datasync;

import static org.assertj.core.api.Assertions.fail;

import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import org.springframework.test.util.ReflectionTestUtils;

public class KafkaStatusCheckTest {

    @Disabled("Requires Kafka to be running")
    @Test
    void test() {
        try {

            KafkaStatusCheck kafkaStatusCheck = new KafkaStatusCheck();
            ReflectionTestUtils.setField(kafkaStatusCheck, "brokerUrl",
                    "localhost:9092");
            boolean isKafkaRunning = kafkaStatusCheck.isKafkaRunning();

        } catch (NullPointerException e) {
            fail("Should not have thrown any exception");
        }
    }
}
