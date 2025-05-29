package com.deloitte.ellr.datasync.jpa.service;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import com.deloitte.elrr.datasync.exception.DatasyncException;
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

            commonSvc.findAll();
            commonSvc.deleteAll();

        } catch (DatasyncException e) {
            e.printStackTrace();
        }
    }
}
