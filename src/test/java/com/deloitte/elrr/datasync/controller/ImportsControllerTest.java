/**
 *
 */
package com.deloitte.elrr.datasync.controller;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.util.ArrayList;
import java.util.List;

import org.junit.jupiter.api.Test;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.web.client.RestTemplate;

import com.deloitte.elrr.datasync.dto.ImportDTO;
import com.deloitte.elrr.datasync.repository.ImportDetailRepository;
import com.deloitte.elrr.datasync.repository.ImportRepository;
import com.deloitte.elrr.datasync.repository.SyncRecordDetailRepository;
import com.deloitte.elrr.datasync.repository.SyncRecordRepository;
import com.deloitte.elrr.datasync.svc.ImportsCreatorSvc;

/**
 * @author mnelakurti
 *
 */
@WebMvcTest(ImportsController.class)
class ImportsControllerTest {

    /**
    *
    */
   @MockBean
   private ImportsCreatorSvc svc;

   /**
    *
    */
   @MockBean
   private ImportDetailRepository mockImportDetailRepository;

   /**
    *
    */
   @MockBean
   private ImportRepository mockImportRepository;


   /**
    *
    */
   @MockBean
   private SyncRecordDetailRepository mockSyncRecordDetailRepository;

   /**
   *
   */
   @MockBean
   private SyncRecordRepository mockSyncRecordRepository;

   /**
   *
   */
   @MockBean
   private RestTemplateBuilder mockRestTemplateBuilder;

   /**
   *
   */
   @MockBean
   private RestTemplate mockRestTemplate;
    /**
    *
    */
   @MockBean
   private ModelMapper mapper;


   /**
   *
   */
   @Autowired
   private MockMvc mockMvc;


   /**
    *
    * @throws Exception
    */
   @Test
   void getImportsAllTest() throws Exception {
       List<ImportDTO> importDTOList = new ArrayList<>();
       ImportDTO importDTO = new ImportDTO();
       importDTOList.add(importDTO);
       mockMvc.perform(get("/api/getImports"))
              .andExpect(status().isBadRequest());
   }

   /**
    *
    */
   @Test
   void getImportsTest() throws Exception {
       ImportDTO importDTO = new ImportDTO();
       mockMvc.perform(get("/api/getImports/{id}", 1L))
              .andExpect(status().isNotFound());
   }

}
