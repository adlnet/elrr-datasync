package com.deloitte.elrr.datasync;

import org.junit.jupiter.api.Test;

public class KafkaStatusCheckTest {

    @Test
    void test() {
        try {

            KafkaStatusCheck kafkaStatusCheck = new KafkaStatusCheck();
            boolean isKafkaRunning = kafkaStatusCheck.isKafkaRunning();

        } catch (NullPointerException e) {

        }
    }
}
