package com.deloitte.elrr.datasync.dto;

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
public class ElrrExcept {

  /**
   *
   */
  private FailureType failureType;

  /**
  *
  */
  private String fieldName;

  /**
  *
  */
  private String errorMessage;
}
