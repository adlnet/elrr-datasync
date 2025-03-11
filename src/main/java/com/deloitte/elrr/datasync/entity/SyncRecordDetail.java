package com.deloitte.elrr.datasync.entity;

import io.hypersistence.utils.hibernate.type.json.JsonType;
import jakarta.persistence.Column;
import jakarta.persistence.Convert;
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
@Table(name = "SYNCRECORDDETAIL")
@Convert(converter = JsonType.class, attributeName = "SYNCRECORDDETAIL")
@RequiredArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class SyncRecordDetail extends Auditable<String> {

  @Id
  @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "syncrecorddetail_SEQ")
  @SequenceGenerator(
      name = "syncrecorddetail_SEQ",
      sequenceName = "syncrecorddetail_SEQ",
      allocationSize = 1)
  @Column(name = "SYNCRECORDDETAILID")
  private Long syncRecordDetailId;

  @Column(name = "SYNCRECORDID")
  private long syncRecordId;

  @Column(name = "RECORDSTATUS")
  private String recordStatus;
}
