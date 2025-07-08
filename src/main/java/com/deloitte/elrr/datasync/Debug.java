package com.deloitte.elrr.datasync;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import jakarta.annotation.PostConstruct;
import lombok.extern.slf4j.Slf4j;

@Component
@Slf4j
public class Debug {


    @Value("${spring.datasource.url}")
    private String jdbcUrl;

    @Value("${spring.jpa.properties.hibernate.default_schema}")
    private String schema;

    /**
     *
     */
    @PostConstruct
    public void printConnectionDebug() {
        log.warn("############ DB Properties #######");
        log.warn(String.format("JDBC Connection String: %s", jdbcUrl));
        log.warn(String.format("Default Schema: %s", schema));
    }

}
