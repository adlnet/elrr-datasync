package com.deloitte.elrr.datasync.jpa.service;

import static org.assertj.core.api.Assertions.fail;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;

import java.util.List;
import java.util.UUID;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
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
    private ImportRepository importRepository;

    @InjectMocks
    private ImportService importService;

    @Test
    void test() {

        try {

            Import imp = new Import();
            UUID id = UUID.randomUUID();
            imp.setId(id);
            imp.setRetries(0);
            imp.setImportName("testName");
            importService.save(imp);

            Mockito.doReturn(true).when(importRepository).existsById(any());
            importService.update(imp);

            List<Import> imps = List.of(imp);
            importService.saveAll(imps);

            importService.get(id);
            importService.findAll();
            importService.findByName("testName");
            importService.getId(imp);
            importService.delete(id);
            importService.deleteAll();

        } catch (ResourceNotFoundException | DatasyncException e) {
            fail("Should not have thrown any exception");
        }
    }

    @Test
    void testResourceNotFound() {

        try {

            Import imp = new Import();
            UUID id = UUID.randomUUID();
            imp.setId(id);
            imp.setRetries(0);
            imp.setImportName("testName");
            importService.save(imp);

            Mockito.doReturn(false).when(importRepository).existsById(any());
            importService.update(imp);
        } catch (ResourceNotFoundException e) {
            assertTrue(e.getMessage().contains("Record to update not found"));
        }
    }
}
