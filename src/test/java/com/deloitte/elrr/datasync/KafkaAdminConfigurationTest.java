package com.deloitte.elrr.datasync;

import static org.assertj.core.api.Assertions.fail;

import org.junit.jupiter.api.Test;

public class KafkaAdminConfigurationTest {

    @Test
    void test() {
        try {

            KafkaAdminConfiguration kafkaAdminConfiguration = new KafkaAdminConfiguration();
            kafkaAdminConfiguration.topicExample();
            kafkaAdminConfiguration.topicDeadLetterQueue();

        } catch (Exception e) {
            fail("Should not have thrown any exception");
        }
    }
}
