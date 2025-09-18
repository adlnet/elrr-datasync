package com.deloitte.elrr.datasync.producer;

import static org.assertj.core.api.Assertions.fail;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;

import java.io.File;
import java.io.IOException;
import java.util.UUID;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.Spy;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.kafka.core.KafkaTemplate;

import com.deloitte.elrr.datasync.dto.MessageVO;
import com.deloitte.elrr.datasync.entity.Import;
import com.deloitte.elrr.datasync.exception.DatasyncException;
import com.deloitte.elrr.datasync.jpa.service.ImportService;
import com.deloitte.elrr.datasync.util.PrettyJson;
import com.deloitte.elrr.datasync.util.TestFileUtil;
import com.yetanalytics.xapi.model.Statement;
import com.yetanalytics.xapi.util.Mapper;

import lombok.extern.slf4j.Slf4j;

@ExtendWith(MockitoExtension.class)
@Slf4j
class KafkaProducerTest {

    @Mock
    private ImportService importService;

    @Mock
    private KafkaTemplate<String, String> kafkaTemplate;

    @Spy
    private PrettyJson prettyJson;

    @InjectMocks
    private KafkaProducer kafkaProducer;

    @Test
    void testWriteValueAsString() {

        try {

            kafkaProducer.writeValueAsString("test");

        } catch (DatasyncException e) {
            fail("Should not have thrown any exception");
        }
    }

    @Test
    void testImportWriteValueAsString() {

        try {

            Import imp = new Import();
            imp.setId(UUID.randomUUID());
            imp.setImportName("name");
            imp.setRetries(0);
            importService.save(imp);

            kafkaProducer.writeValueAsString(imp);

        } catch (DatasyncException e) {
            fail("Should not have thrown any exception");
        }
    }

    @Test
    @SuppressWarnings("checkstyle:linelength")
    void testWriteBadValueAsString() {

        try {

            ObjectWithNoToString objectWithNoToString = new ObjectWithNoToString();
            kafkaProducer.writeValueAsString(objectWithNoToString);

        } catch (DatasyncException e) {
            fail("Should not have thrown any exception");
        }
    }

    @Test
    void testSendMessage() {

        try {

            File testFile = TestFileUtil.getJsonTestFile("completed.json");

            Statement[] stmts = Mapper.getMapper().readValue(testFile,
                    Statement[].class);

            MessageVO kafkaMessage = new MessageVO();
            kafkaMessage.setStatement(stmts[0]);
            kafkaProducer.sendMessage(kafkaMessage);

        } catch (DatasyncException | IOException e) {
            fail("Should not have thrown any exception");
        }
    }

    @Test
    void testSendMessageFail() {

        try {

            File testFile = TestFileUtil.getJsonTestFile("completed.json");

            Statement[] stmts = Mapper.getMapper().readValue(testFile,
                    Statement[].class);

            Mockito.doThrow(new DatasyncException(
                    "Exception while sending Kafka message")).when(
                            kafkaTemplate).send(any(), any());

            MessageVO kafkaMessage = new MessageVO();
            kafkaMessage.setStatement(stmts[0]);
            kafkaProducer.sendAsyncMessage(kafkaMessage);

        } catch (DatasyncException | IOException e) {
            assertEquals(e.getMessage(),
                    "Exception while sending Kafka message");
        }
    }

}
