/**
 *
 */
package com.deloitte.elrr.datasync.svc;

import static org.junit.Assert.assertNotNull;

import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import com.deloitte.elrr.datasync.dto.ImportDTO;

/**
 * @author mnelakurti
 *
 */
@ExtendWith(MockitoExtension.class)
class ImportsCreatorSvcTest {

    /**
    *
    */
   @Mock
   private ImportsCreatorSvc importsCreatorSvc;

   /**
    *
    */
   @Mock
   private ImportDTO importDTO;

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
        assertNotNull(importsCreatorSvc.getAllImports());
        importsCreatorSvc.getImports("name");
    }

}
