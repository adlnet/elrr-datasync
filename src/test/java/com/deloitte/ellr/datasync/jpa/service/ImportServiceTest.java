package com.deloitte.ellr.datasync.jpa.service;

import static org.assertj.core.api.Assertions.fail;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import com.deloitte.elrr.datasync.entity.Import;
import com.deloitte.elrr.datasync.exception.DatasyncException;
import com.deloitte.elrr.datasync.jpa.service.ImportService;
import com.deloitte.elrr.datasync.repository.ImportRepository;

import lombok.extern.slf4j.Slf4j;

@ExtendWith(MockitoExtension.class)
@Slf4j
class ImportServiceTest {

    @Mock
    ImportRepository importRepository;

    @InjectMocks
    private ImportService importService;

    @Test
    void test() {

        try {

            Import imprt = new Import();

            importService.findByName("name");
            importService.getId(imprt);

        } catch (DatasyncException e) {
            fail("Should not have thrown any exception");
        }
    }
}
