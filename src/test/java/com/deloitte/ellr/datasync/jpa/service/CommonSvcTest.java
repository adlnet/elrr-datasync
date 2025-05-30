package com.deloitte.ellr.datasync.jpa.service;

import static org.assertj.core.api.Assertions.fail;

import java.util.UUID;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import com.deloitte.elrr.datasync.entity.Import;
import com.deloitte.elrr.datasync.jpa.service.CommonSvc;

import lombok.extern.slf4j.Slf4j;

@ExtendWith(MockitoExtension.class)
@Slf4j
class CommonSvcTest {

    @Mock
    CommonSvc commonSvc;

    @Test
    void test() {

        try {

            Import imp = new Import();
            imp.setId(UUID.randomUUID());
            imp.setRetries(0);
            commonSvc.save(imp);

            imp.setRetries(1);
            commonSvc.update(imp);

            commonSvc.findAll();
            commonSvc.deleteAll();

        } catch (Exception e) {
            fail("Should not have thrown any exception");
        }
    }
}
