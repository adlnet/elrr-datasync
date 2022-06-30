package com.deloitte.elrr.datasync.dto;

import java.io.Serializable;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;

/**
 * @author mnelakurti
 *
 */
@RequiredArgsConstructor
@AllArgsConstructor
@Data
@Getter
@Setter
public class ElrrStatement implements Serializable {

  /**
   *
   *
   */
  private static final long serialVersionUID = 1L;
  /**
   *
   */
  private String actor;
  /**
   *
   */
  private String actorName;
  /**
   *
   */
  private String verb;
  /**
   *
   */
  private String activity;
  /**
   *
   */
  private String courseName;
  /**
   *
   */
  private String statementjson;
}
