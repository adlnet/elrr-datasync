package com.deloitte.elrr.datasync.producer;

import java.util.HashMap;
import java.util.Map;
import org.apache.kafka.clients.producer.ProducerConfig;
import org.apache.kafka.common.serialization.StringSerializer;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.core.DefaultKafkaProducerFactory;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.core.ProducerFactory;

@Configuration
public class KafkaProducerConfig {
  /**
   *
   */
  @Value("${brokerUrl}")
  private String brokerUrl;
  /**
   *
   */
  @Value("${sasl.jaas.config}")
  private String jaasConfig;
  /**
   *
   * @return ProducerFactory<String, String>
   */
  @Bean
  public ProducerFactory<String, String> producerFactory() {
    Map<String, Object> configProps = new HashMap<>();
    configProps.put(ProducerConfig.BOOTSTRAP_SERVERS_CONFIG, brokerUrl);
    configProps.put(
      ProducerConfig.KEY_SERIALIZER_CLASS_CONFIG,
      StringSerializer.class
    );
    configProps.put(
      ProducerConfig.VALUE_SERIALIZER_CLASS_CONFIG,
      StringSerializer.class
    );
    configProps.put("security.protocol", "SASL_PLAINTEXT");
    configProps.put("sasl.mechanism", "PLAIN");

    configProps.put("sasl.jaas.config", jaasConfig);
    return new DefaultKafkaProducerFactory<>(configProps);
  }
  /**
   *
   * @return KafkaTemplate<String, String>
   */
  @Bean
  public KafkaTemplate<String, String> kafkaTemplate() {
    return new KafkaTemplate<>(producerFactory());
  }
}
