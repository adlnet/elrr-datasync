package com.deloitte.elrr.datasync.producer;

import java.util.concurrent.CompletableFuture;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.kafka.KafkaException;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.support.SendResult;
import org.springframework.stereotype.Service;

import com.deloitte.elrr.datasync.dto.MessageVO;
import com.deloitte.elrr.datasync.exception.DatasyncException;
import com.deloitte.elrr.datasync.util.PrettyJson;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.yetanalytics.xapi.util.Mapper;

import lombok.extern.slf4j.Slf4j;

@Service
@Slf4j
public class KafkaProducer {

    @Value("${kafka.topic}")
    private String kafkatopic;

    @Autowired
    private KafkaTemplate<String, String> kafkaTemplate;

    private ObjectMapper mapper = Mapper.getMapper();

    /**
     * @param msg
     * @throws DataSyncException
     */
    public void sendMessage(final MessageVO msg) {

        try {

            log.debug("\n\n ==========sent messsage to Kafka========== \n"
                    + PrettyJson.prettyJson(writeValueAsString(msg)));

            kafkaTemplate.send(kafkatopic, writeValueAsString(msg));

            log.debug("\n Kafka message successfully sent to kafka topic "
                    + kafkatopic + "\n\n");

        } catch (KafkaException | DatasyncException e) {
            throw new DatasyncException("Exception while sending Kafka message",
                    e);
        }
    }

    /**
     * NOT USED - for future optimization.
     * @param msg
     */
    public void sendAsyncMessage(final MessageVO msg) {

        CompletableFuture<SendResult<String, String>> future = kafkaTemplate
                .send(kafkatopic, writeValueAsString(msg));

        future.whenComplete((result, throwable) -> {
            if (throwable != null) {
                // handle failure
                log.info("Unable to send message=[" + PrettyJson.prettyJson(
                        writeValueAsString(msg)) + "] due to : " + throwable
                                .getMessage());
            } else {
                // handle success
                log.debug("\n\n ==========sent messsage to Kafka========== \n"
                        + PrettyJson.prettyJson(writeValueAsString(msg)));
            }
        });
    }

    /**
     * @param data
     * @return String
     * @throws DatasyncException
     */
    public String writeValueAsString(final Object data)
            throws DatasyncException {

        String output = "";

        try {
            mapper.configure(SerializationFeature.FAIL_ON_EMPTY_BEANS, false);
            output = mapper.writeValueAsString(data);
        } catch (JsonProcessingException e) {
            throw new DatasyncException("Unable to convert to json", e);
        }

        return output;
    }
}
