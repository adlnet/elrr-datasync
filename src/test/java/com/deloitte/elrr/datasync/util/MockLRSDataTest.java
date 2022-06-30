/**
 *
 */
package com.deloitte.elrr.datasync.util;

import static org.junit.Assert.assertNotNull;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;

/**
 * @author mnelakurti
 *
 */
@ExtendWith(MockitoExtension.class)
class MockLRSDataTest {

    /**
     *
     */
    @Test
    void test() {
        assertNotNull(MockLRSData.getLearnerStatements());
    }

}
