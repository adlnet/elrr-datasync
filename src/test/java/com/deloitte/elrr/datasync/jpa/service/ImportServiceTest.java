package com.deloitte.elrr.datasync.jpa.service;

import static org.assertj.core.api.Assertions.fail;

import java.util.UUID;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import com.deloitte.elrr.datasync.entity.Import;
import com.deloitte.elrr.datasync.exception.DatasyncException;
import com.deloitte.elrr.datasync.exception.ResourceNotFoundException;
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

            Import imp = new Import();
            imp.setId(UUID.randomUUID());
            imp.setRetries(0);
            imp.setImportName("testName");
            importService.save(imp);

            importService.findByName("testName");
            importService.getId(imp);

        } catch (DatasyncException e) {
            fail("Should not have thrown any exception");
        }
    }

    @Test
    void testCommomSvc() {

        try {

            Import imp = new Import();
            UUID id = UUID.randomUUID();
            imp.setId(id);
            imp.setRetries(0);
            imp.setImportName("testName");
            importService.save(imp);

            importService.findAll();
            importService.findByName("testName");
            importService.getId(imp);
            importService.delete(id);
            importService.deleteAll();

        } catch (ResourceNotFoundException e) {
        }
    }
}
