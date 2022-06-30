/**
 *
 */
package com.deloitte.elrr.datasync.svc;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Iterator;
import java.util.List;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.mockito.junit.jupiter.MockitoSettings;
import org.mockito.quality.Strictness;
import org.springframework.test.util.ReflectionTestUtils;

import com.deloitte.elrr.datasync.entity.Import;
import com.deloitte.elrr.datasync.entity.ImportDetail;
import com.deloitte.elrr.datasync.jpa.service.ImportDetailService;
import com.deloitte.elrr.datasync.jpa.service.ImportService;

/**
 * @author mnelakurti
 *
 */
@ExtendWith(MockitoExtension.class)
@MockitoSettings(strictness = Strictness.LENIENT)
class ImportCreatorImplTest {

    /**
    *
    */
    @Mock
    private ImportService importService;

    /**
    *
    */
    @Mock
    private ImportDetailService importDetailService;

    /**
     *
     */
    private ImportCreatorImpl mockImportCreatorImpl;

    /**
     *
     */
    private static Import imports = null;

    /**
     *
     */
    private static Iterable<Import> importsItr = null;

    /**
     *
     */
    private static List<ImportDetail> importDetail = null;

    /**
     * @throws java.lang.Exception
     */
    @BeforeAll
    static void setUpBeforeClass() throws Exception {
        imports = getImport();
        importsItr = getImportList();
        importDetail = getImportDetails();
    }

    /**
     *
     */
    @Test
    void test() {
        mockImportCreatorImpl = new ImportCreatorImpl();
        ReflectionTestUtils.setField(mockImportCreatorImpl,
                "importService", importService);
        ReflectionTestUtils.setField(mockImportCreatorImpl,
                "importDetailService", importDetailService);
        Mockito.doReturn(imports).when(importService)
        .findByName("Deloitte LRS");
        Mockito.doReturn(importDetail).when(importDetailService)
        .findByImportId(imports.getImportId());
        mockImportCreatorImpl.getImports("Deloitte LRS");

        ReflectionTestUtils.setField(mockImportCreatorImpl,
                "importDetailService", importDetailService);
        Mockito.doReturn(importsItr).when(importService).findAll();
        Mockito.doReturn(importDetail).when(importDetailService)
        .findByImportId(imports.getImportId());
        mockImportCreatorImpl.getAllImports();

    }

    /**
     *
     * @return Import
     */
    private static Import getImport() {
        Import newimports = new Import();
        newimports.setImportName("Deloitte LRS");
        newimports.setImportId(1L);
        return newimports;
    }


    /**
    *
    * @return Iterable<Import>
    */
    private static Iterable<Import> getImportList() {
       Iterable<Import> importsIterable = Mockito.mock(Iterable.class);
       mockIterable(importsIterable, getImport());
       return importsIterable;
   }

    /**
    *
    * @return List <ImportDetail>
    */
   private static List<ImportDetail>  getImportDetails() {
       List<ImportDetail> importDetailList = new ArrayList<>();
       ImportDetail newimportDetail = new ImportDetail();
       newimportDetail.setImportId(1L);
       importDetailList.add(newimportDetail);
       return importDetailList;
   }

   /**
    *
    * @param <T>
    * @param iterable
    * @param values
    */
   public static <T> void mockIterable(final Iterable<T> iterable,
           final T... values) {
       Iterator<T> mockIterator = Mockito.mock(Iterator.class);
       Mockito.when(iterable.iterator()).thenReturn(mockIterator);

       if (values.length == 0) {
           Mockito.when(mockIterator.hasNext()).thenReturn(false);
           return;
       } else if (values.length == 1) {
           Mockito.when(mockIterator.hasNext()).thenReturn(true, false);
           Mockito.when(mockIterator.next()).thenReturn(values[0]);
       } else {
           // build boolean array for hasNext()
           Boolean[] hasNextResponses = new Boolean[values.length];
           for (int i = 0; i < hasNextResponses.length - 1; i++) {
               hasNextResponses[i] = true;
           }
           hasNextResponses[hasNextResponses.length - 1] = false;
           Mockito.when(mockIterator.hasNext())
           .thenReturn(true, hasNextResponses);
           T[] valuesMinusTheFirst = Arrays
                   .copyOfRange(values, 1, values.length);
           Mockito.when(mockIterator.next())
           .thenReturn(values[0], valuesMinusTheFirst);
       }
   }
}
