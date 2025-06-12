/** */
package com.deloitte.elrr.datasync.exception;

public class DatasyncException extends RuntimeException {

    private static final long serialVersionUID = 1L;

    /**
     * @param message
     * @param e
     */
    public DatasyncException(final String message, Exception e) {
        super(message);
    }

    /**
     * @param message
     */
    public DatasyncException(final String message) {
        super(message);
    }
}
