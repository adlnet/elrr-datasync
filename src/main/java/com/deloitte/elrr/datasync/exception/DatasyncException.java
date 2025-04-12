/** */
package com.deloitte.elrr.datasync.exception;

public class DatasyncException extends RuntimeException {

  private static final long serialVersionUID = 1L;

  /**
   * @param message
   */
  public DatasyncException(final String message) {
    super(message);
  }
}
