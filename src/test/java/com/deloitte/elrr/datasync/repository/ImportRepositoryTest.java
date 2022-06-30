/**
 *
 */
package com.deloitte.elrr.datasync.repository;

import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import com.deloitte.elrr.datasync.entity.Import;

/**
 * @author mnelakurti
 *
 */
@ExtendWith(MockitoExtension.class)
class ImportRepositoryTest {


    /**
    *
    */
   @Mock
   private ImportRepository mockImportRepository;

   /**
    *
    */
   @Mock
   private Import mockImport;

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
        mockImport.setImportId(1L);
        mockImportRepository.findAll();
        mockImportRepository.findById(1L);
        mockImportRepository.save(mockImport);
        mockImportRepository.delete(mockImport);
    }

}
