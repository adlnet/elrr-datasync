package com.deloitte.elrr.datasync.exception;

import static org.junit.jupiter.api.Assertions.assertEquals;

import org.junit.jupiter.api.Test;

/**
 * @author mnelakurti
 */
@SuppressWarnings("checkstyle:linelength")
public class ResourceNotFoundExceptionTest {

    private final String message = "ResourceNotFoundException"
            + "Exception Message";

    private ResourceNotFoundException resourceNotFoundException = new ResourceNotFoundException(
            message);

    /**
     * @author phleven
     */
    @Test
    public void testTipExceptionWithMessage() {
        assertEquals(resourceNotFoundException.getMessage(), message);
    }
}
