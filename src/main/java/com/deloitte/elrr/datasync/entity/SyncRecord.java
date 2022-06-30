package com.deloitte.elrr.datasync.entity;

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
@Table(name = "SYNCRECORD")
@RequiredArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class SyncRecord extends Auditable<String> {
  /**
   *
   */
  @Id
  @GeneratedValue(strategy = GenerationType.AUTO)
  @Column(name = "SYNCRECORDID")
  private long syncRecordId;
  /**
   *
   */
  @Column(name = "IMPORTDETAILID")
  private long importdetailId;
  /**
   *
   */
  @Column(name = "SYNCKEY")
  private String syncKey;
  /**
   *
   */
  @Column(name = "RECORDSTATUS")
  private String recordStatus;
}
