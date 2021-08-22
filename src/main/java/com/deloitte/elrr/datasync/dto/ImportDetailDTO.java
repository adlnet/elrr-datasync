package com.deloitte.elrr.datasync.dto;

import java.io.Serializable;
import java.sql.Timestamp;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;

@RequiredArgsConstructor
@AllArgsConstructor
@Data
@Getter
@Setter
public class ImportDetailDTO implements Serializable {

	String importsName;
	String importsEndPoint;
	Timestamp importsDate;
	int totalRecords;
	int failedRecords;
	int successRecords;
	String status;
}
