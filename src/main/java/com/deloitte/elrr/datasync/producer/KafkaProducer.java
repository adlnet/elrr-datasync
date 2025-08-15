package com.deloitte.elrr.datasync.producer;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.kafka.KafkaException;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

import com.deloitte.elrr.datasync.exception.DatasyncException;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.yetanalytics.xapi.util.Mapper;

import lombok.extern.slf4j.Slf4j;

@Service
@Slf4j
@SuppressWarnings("checkstyle:linelength")
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
    public void sendMessage(final Object msg) {

        String payload = "";

        try {

            if (msg instanceof String) {
                payload = (String) msg;
            } else {
                payload = writeValueAsString(msg);
            }

            log.info(
                    "\n\n ===============sent messsage to Kafka=============== \n"
                            + payload);
            kafkaTemplate.send(kafkatopic, payload);

            log.info("\n Kafka message successfully sent to kafka topic "
                    + kafkatopic + "\n\n");

        } catch (KafkaException | DatasyncException e) {
            throw new DatasyncException("Exception while sending Kafka message",
                    e);
        }
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
