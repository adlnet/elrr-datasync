package com.deloitte.elrr.datasync.entity;

import java.sql.Timestamp;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;
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
  @GeneratedValue(strategy = GenerationType.AUTO)
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
