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
  /**
   *
   */
  private String importsName;
  /**
  *
  */
  private String importsEndPoint;
  /**
  *
  */
  private Timestamp importsDate;
  /**
  *
  */
  private int totalRecords;
  /**
  *
  */
  private int failedRecords;
  /**
  *
  */
  private int successRecords;
  /**
  *
  */
  private String status;
}
