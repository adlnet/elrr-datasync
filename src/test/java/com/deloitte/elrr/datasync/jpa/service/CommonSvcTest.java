/**
 *
 */
package com.deloitte.elrr.datasync.jpa.service;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class CommonSvcTest {

    /**
    *
    */
    @Mock
    private CommonSvc commonSvc;

    @Test
    void test() {
        commonSvc.findAll();
    }

}
