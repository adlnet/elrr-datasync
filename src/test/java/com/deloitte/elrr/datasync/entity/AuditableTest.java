/** */
package com.deloitte.elrr.datasync.entity;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;

import com.deloitte.elrr.datasync.util.ValueObjectTestUtility;

/**
 * @author mnelakurti
 */
@ExtendWith(MockitoExtension.class)
class AuditableTest {

    @Test
    void test() {
        ValueObjectTestUtility.validateAccessors(Auditable.class);
    }
}
