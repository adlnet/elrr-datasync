package com.deloitte.elrr.datasync.entity;

import io.hypersistence.utils.hibernate.type.json.JsonType;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import jakarta.persistence.Convert;
import jakarta.persistence.Id;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Column;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;

@Entity
@Table(name = "SYNCRECORDDETAIL")
//@Convert(converter  = JsonType.class)
@RequiredArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class SyncRecordDetail extends Auditable<String> {
  /**
   *
   */
  @Id
  @GeneratedValue(strategy = GenerationType.AUTO)
  @Column(name = "SYNCRECORDDETAILID")
  private Long syncRecordDetailId;
  /**
   *
   */
  @Column(name = "SYNCRECORDID")
  private long syncRecordId;
  /**
   *
   */
//  @Convert(converter  = JsonType.class)
  @Column(columnDefinition = "jsonb")
  private String payload;
  /**
   *
   */
//  @Convert(converter  = JsonType.class)
  @Column(columnDefinition = "jsonb")
  private String learner;
  /**
   *
   */
  @Column(name = "RECORDSTATUS")
  private String recordStatus;
}

