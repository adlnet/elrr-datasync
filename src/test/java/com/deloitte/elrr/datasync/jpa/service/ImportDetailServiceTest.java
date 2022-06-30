package com.deloitte.elrr.datasync.jpa.service;

import static org.junit.Assert.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;

import java.util.ArrayList;
import java.util.List;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.mockito.junit.jupiter.MockitoSettings;
import org.mockito.quality.Strictness;
import org.springframework.test.util.ReflectionTestUtils;

import com.deloitte.elrr.datasync.entity.ImportDetail;
import com.deloitte.elrr.datasync.repository.ImportDetailRepository;

/**
 *
 * @author mnelakurti
 *
 */
@ExtendWith(MockitoExtension.class)
@MockitoSettings(strictness = Strictness.LENIENT)
class ImportDetailServiceTest {

    /**
    *
    */
    @Mock
    private ImportDetailRepository importDetailRepository;

    /**
    *
    */
    @Mock
    private ImportDetail mockImportDetail;

    /**
     * @throws Exception
     *
     */
    @Test
    public void testGetName() throws Exception {
        ImportDetailService importDetailService
        = new ImportDetailService(importDetailRepository);
        ReflectionTestUtils.setField(importDetailService,
                "importDetailRepository", importDetailRepository);
        Mockito.doReturn(getImportDetails())
        .when(importDetailRepository).findByImportId(1L);
        assertNotNull(importDetailService.findByImportId(1L));
    }

    /**
     * @throws Exception
     *
     */
    @Test
    public void testGetId() throws Exception {
        ImportDetailService importDetailService
        = new ImportDetailService(importDetailRepository);
        ReflectionTestUtils.setField(importDetailService,
                "importDetailRepository", importDetailRepository);
        Mockito.doReturn(getImportDetails())
        .when(importDetailRepository).findByImportId(1L);
        assertNotNull(importDetailService.findAll());
    }

    /**
     * @throws Exception
     *
     */
    @Test
    public void testFindByImportId() throws Exception {
        ImportDetailService importDetailService
        = new ImportDetailService(importDetailRepository);
        ReflectionTestUtils.setField(importDetailService,
                "importDetailRepository", importDetailRepository);
        Mockito.doReturn(getImportDetails())
        .when(importDetailRepository).findByImportId(1L);
        assertNotNull(importDetailService.findByImportId(1L));
    }

    /**
     * @throws Exception
     *
     */
    @Test
    public void testSave() throws Exception {
        ImportDetailService importDetailService
        = new ImportDetailService(importDetailRepository);
        assertNull(importDetailService.save(mockImportDetail));
    }

    /**
     * @throws Exception
     *
     */
    @Test
    public void testUpdate() throws Exception {
        ImportDetailService importDetailService
        = new ImportDetailService(importDetailRepository);
        //importDetailService.update(mockImportDetail);
        importDetailService.findAll();
        importDetailService.findByImportId(1L);
        importDetailService.getId(mockImportDetail);
        importDetailService.getId(null);
        importDetailService.save(mockImportDetail);
        importDetailService.deleteAll();
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

}
