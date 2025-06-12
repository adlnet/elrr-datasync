package com.deloitte.elrr.datasync;

import static org.assertj.core.api.Assertions.fail;

import org.junit.jupiter.api.Test;

public class KafkaAdminConfigurationTest {

    @Test
    void test() {
        try {

            KafkaAdminConfiguration kc = new KafkaAdminConfiguration();
            kc.topicExample();
            kc.topicDeadLetterQueue();

        } catch (Exception e) {
            fail("Should not have thrown any exception");
        }
    }
}
