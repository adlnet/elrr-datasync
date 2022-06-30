/**
 *
 */
package com.deloitte.elrr.datasync.jpa.service;

import static org.junit.Assert.assertNotNull;

import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.mockito.junit.jupiter.MockitoSettings;
import org.mockito.quality.Strictness;
import org.springframework.test.util.ReflectionTestUtils;

import com.deloitte.elrr.datasync.entity.Import;
import com.deloitte.elrr.datasync.repository.ImportRepository;

/**
 * @author mnelakurti
 *
 */
@ExtendWith(MockitoExtension.class)
@MockitoSettings(strictness = Strictness.LENIENT)
class ImportServiceTest {

    /**
    *
    */
    @Mock
    private ImportRepository importsRepository;

    /**
     * @throws java.lang.Exception
     */
    @BeforeAll
    static void setUpBeforeClass() throws Exception {
    }

    /**
     * @throws java.lang.Exception
     */
    @AfterAll
    static void tearDownAfterClass() throws Exception {
    }

    /**
     * @throws java.lang.Exception
     */
    @BeforeEach
    void setUp() throws Exception {
    }

    /**
     * @throws java.lang.Exception
     */
    @AfterEach
    void tearDown() throws Exception {
    }

    @Test
    void test() {
        ImportService mockImportService = new ImportService(importsRepository);
        assertNotNull(mockImportService.get(1L));
        mockImportService.getId(getImport());
        mockImportService.getId(null);
        mockImportService.findByName("Deloitte LRS");
        assertNotNull(mockImportService.findAll());
        ReflectionTestUtils.setField(mockImportService,
                "importsRepository", importsRepository);
        Mockito.doReturn(getImport())
        .when(importsRepository).findByName("Deloitte LRS");
        assertNotNull(mockImportService.findByName("Deloitte LRS"));
    }

    /**
    *
    * @return Import
    */
   private static Import  getImport() {
       Import tempimport  = new Import();
       tempimport.setImportId(1L);
       tempimport.setImportName("Deloitte LRS");
       return tempimport;
   }

}
