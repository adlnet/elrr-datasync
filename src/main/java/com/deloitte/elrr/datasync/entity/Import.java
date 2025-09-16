package com.deloitte.elrr.datasync.entity;

import java.time.ZonedDateTime;
import java.util.UUID;

import org.hibernate.annotations.GenericGenerator;
import org.hibernate.annotations.JdbcType;
import org.hibernate.dialect.PostgreSQLEnumJdbcType;

import com.deloitte.elrr.datasync.entity.types.RecordStatus;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;

@Entity
@Table(name = "IMPORT")
@RequiredArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class Import extends Auditable<String> {

    @Id
    @GeneratedValue(generator = "uuid2")
    @GenericGenerator(name = "uuid", strategy = "uuid2")
    @Column(name = "ID")
    private UUID id;

    @Column(name = "IMPORTNAME")
    private String importName;

    @Column(name = "IMPORTSTARTDATE")
    private ZonedDateTime importStartDate;

    @Column(name = "RECORDSTATUS", columnDefinition = "record_status")
    @Enumerated(EnumType.STRING)
    @JdbcType(PostgreSQLEnumJdbcType.class)
    private RecordStatus recordStatus;

    @Column(name = "RETRIES")
    private int retries;
}
