package com.deloitte.elrr.datasync;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.EnableAspectJAutoProxy;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.transaction.annotation.EnableTransactionManagement;
import org.springframework.web.client.RestTemplate;

import com.ulisesbocchio.jasyptspringboot.annotation.EnableEncryptableProperties;

@SpringBootApplication
@EnableScheduling
@EnableTransactionManagement
// Use the class based proxy
@EnableAspectJAutoProxy(proxyTargetClass = true)
@ComponentScan({"com.deloitte.elrr"})
@EnableEncryptableProperties
public class DatasyncApplication {

  /**
   * @param args
   */
  public static void main(final String[] args) {
    SpringApplication.run(DatasyncApplication.class, args);
  }

  /**
   * This for returning the Rest Template.
   *
   * @param builder
   * @return RestTemplate
   */
  @Bean
  public RestTemplate restTemplate(final RestTemplateBuilder builder) {
    return builder.build();
  }
}
