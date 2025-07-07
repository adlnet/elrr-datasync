package com.deloitte.elrr.datasync;

import static org.assertj.core.api.Assertions.fail;
import static org.junit.Assert.assertTrue;

import org.junit.jupiter.api.Test;
import org.springframework.kafka.test.context.EmbeddedKafka;
import org.springframework.test.util.ReflectionTestUtils;

@EmbeddedKafka(partitions = 1, brokerProperties = {
        "listeners=PLAINTEXT://localhost:9092", "port=9092" })
public class KafkaStatusCheckTest {

    @Test
    void test() {
        try {

            KafkaStatusCheck kafkaStatusCheck = new KafkaStatusCheck();
            ReflectionTestUtils.setField(kafkaStatusCheck, "brokerUrl",
                    "localhost:9092");
            boolean isKafkaRunning = kafkaStatusCheck.isKafkaRunning();
            assertTrue(isKafkaRunning);

        } catch (NullPointerException e) {
            fail("Should not have thrown any exception");
        }
    }
}
