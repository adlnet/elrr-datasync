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
@Table(name = "IMPORTS")
@RequiredArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class Imports  extends Auditable<String> {

	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	private long importid;
	
	@Column(name = "IMPORTNAME")
	private String importName;

	@Column(name = "IMPORTSTARTDATE")
	private Timestamp importStartDate;

	@Column(name = "IMPORTENDDATE")
	private Timestamp importEndDate;

	@Column(name = "RECORDSTATUS")
	private String recordStatus;
	


}