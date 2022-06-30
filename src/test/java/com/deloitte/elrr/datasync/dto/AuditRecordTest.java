/**
 *
 */
package com.deloitte.elrr.datasync.dto;

import static org.junit.jupiter.api.Assertions.assertNotNull;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;

import com.deloitte.elrr.datasync.util.ValueObjectTestUtility;



/**
 * @author mnelakurti
 *
 */
@ExtendWith(MockitoExtension.class)
class AuditRecordTest {

    /**
    *
    */
    @Test
    void test() {
        ValueObjectTestUtility.validateAccessors(AuditRecord.class);
    }

    /**
     *
     */
    @Test
    void testToString() {
        assertNotNull(new AuditRecord().toString());
    }


}
