package com.deloitte.elrr.datasync.dto;

import java.io.Serializable;
import java.util.List;
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
public class ImportDTO implements Serializable {
  /**
   *
   */
  private String importsName;
  /**
   *
   * String importsEndPoint; Timestamp importsDate; int totalRecords; int.
   * failedRecords; int successRecords; String status;
   * @return transient
   */
  private transient List<ImportDetailDTO> detailsList;
}
