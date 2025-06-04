package com.deloitte.elrr.datasync.producer;

import static org.assertj.core.api.Assertions.fail;

import java.io.File;
import java.io.IOException;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.kafka.core.KafkaTemplate;

import com.deloitte.elrr.datasync.exception.DatasyncException;
import com.deloitte.elrr.datasync.util.TestFileUtil;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.yetanalytics.xapi.model.Statement;
import com.yetanalytics.xapi.util.Mapper;

import lombok.extern.slf4j.Slf4j;

@ExtendWith(MockitoExtension.class)
@Slf4j
class KafkaProducerTest {

    @Mock
    private KafkaTemplate<String, String> kafkaTemplate;

    @InjectMocks
    private KafkaProducer kafkaProducer;

    @Test
    void testWriteValueAsString() {

        try {

            kafkaProducer.writeValueAsString("");

        } catch (DatasyncException | JsonProcessingException e) {
            e.printStackTrace();
        }
    }

    @Test
    void test() {

        try {

            File testFile = TestFileUtil.getJsonTestFile("completed.json");

            Statement[] stmts = Mapper.getMapper().readValue(testFile,
                    Statement[].class);

            kafkaProducer.sendMessage(stmts[0]);

        } catch (DatasyncException | IOException e) {
            fail("Should not have thrown any exception");
        }
    }

}
