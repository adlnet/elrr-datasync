/**
 *
 */
package com.deloitte.elrr.datasync.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

/**
 * @author mnelakurti
 *
 */
@ResponseStatus(value = HttpStatus.NOT_FOUND)
public class RunTimeServiceException extends RuntimeException {

  private static final long serialVersionUID = 1L;
  /**
   *
   * @param message
   */
  public RunTimeServiceException(final String message) {
    super(message);
  }
}
