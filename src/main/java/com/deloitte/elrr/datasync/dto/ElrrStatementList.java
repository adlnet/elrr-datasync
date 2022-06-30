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
public class ElrrStatementList implements Serializable {
  /**
   *
   */
  private transient List<ElrrStatement> elrrStatements;
}
