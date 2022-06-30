/**
 *
 */
package com.deloitte.elrr.datasync.repository;

import static org.junit.Assert.assertEquals;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import com.deloitte.elrr.datasync.entity.ImportDetail;

/**
 * @author mnelakurti
 *
 */
@ExtendWith(MockitoExtension.class)
class ImportDetailRepositoryTest {

    /**
    *
    */
   @Mock
   private ImportDetailRepository mockImportDetailRepository;

   /**
    *
    */
   @Mock
   private ImportDetail importDetail;

    @Test
    void test() {
        importDetail.setImportId(1L);
        mockImportDetailRepository.findAll();
        mockImportDetailRepository.findById(1L);
        mockImportDetailRepository.save(importDetail);
        assertEquals(importDetail.getImportId(), 0);
        mockImportDetailRepository.delete(importDetail);
    }

}
