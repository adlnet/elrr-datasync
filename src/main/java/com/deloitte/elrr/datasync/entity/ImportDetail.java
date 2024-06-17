package com.deloitte.elrr.datasync.entity;

import java.sql.Timestamp;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.SequenceGenerator;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;

@Entity
@Table(name = "IMPORTDETAIL")
@RequiredArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class ImportDetail {
  /**
   *
   */
  @Id
  @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "importdetail_SEQ")
  @SequenceGenerator(name = "importdetail_SEQ", sequenceName = "importdetail_SEQ", allocationSize = 1)
  @Column(name = "IMPORTDETAILID")
  private long importdetailId;
  /**
   *
   */
  @Column(name = "IMPORTID")
  private long importId;
  /**
   *
   */
  @Column(name = "IMPORTBEGINTIME")
  private Timestamp importBeginTime;
  /**
   *
   */
  @Column(name = "IMPORTENDTIME")
  private Timestamp importEndTime;
  /**
   *
   */
  @Column(name = "TOTALRECORDS")
  private int totalRecords;
  /**
   *
   */
  @Column(name = "SUCCESSRECORDS")
  private int successRecords;
  /**
   *
   */
  @Column(name = "FAILEDRECORDS")
  private int failedRecords;
  /**
   *
   */
  @Column(name = "RECORDSTATUS")
  private String recordStatus;
}
