package com.deloitte.elrr.datasync.entity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

import org.hibernate.annotations.Type;
import org.hibernate.annotations.TypeDef;

import com.vladmihalcea.hibernate.type.json.JsonType;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
 
 
@Entity
@Table(name = "SYNCRECORDDETAILS")
@TypeDef(
	    name = "json",
	    typeClass = JsonType.class)
@RequiredArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class SyncRecordDetails  extends Auditable<String> {

	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	private Long syncRecordDetailsId;
	
	@Column(name = "SYNCRECORDID")
	private long syncRecordId;

    @Type(type = "json")
    @Column(columnDefinition = "jsonb")
	private String   payload;

    @Type(type = "json")
    @Column(columnDefinition = "jsonb")
	private String   learner;
	
	@Column(name = "SYNCDETAILSSTATUS")
	private String syncDetailsStatus;

 }