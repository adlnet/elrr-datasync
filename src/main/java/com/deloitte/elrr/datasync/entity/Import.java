package com.deloitte.elrr.datasync.entity;

import java.sql.Timestamp;
import java.util.UUID;

import org.hibernate.annotations.GenericGenerator;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
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
  private Timestamp importStartDate;

  @Column(name = "IMPORTENDDATE")
  private Timestamp importEndDate;

  @Column(name = "RECORDSTATUS")
  private String recordStatus;

  @Column(name = "RETRIES")
  private int retries;
}
