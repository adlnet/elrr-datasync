package com.deloitte.elrr.datasync.svc;

import com.deloitte.elrr.datasync.dto.ImportDTO;
import com.deloitte.elrr.datasync.dto.ImportDetailDTO;
import com.deloitte.elrr.datasync.entity.Import;
import com.deloitte.elrr.datasync.entity.ImportDetail;
import com.deloitte.elrr.datasync.jpa.service.ImportDetailService;
import com.deloitte.elrr.datasync.jpa.service.ImportService;
import java.util.ArrayList;
import java.util.List;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
@Slf4j
public class ImportCreatorImpl implements ImportsCreatorSvc {
  /**
   *
   */
  @Autowired
  private ImportService importService;
  /**
   *
   */
  @Autowired
  private ImportDetailService importDetailService;
  /**
   *
   * @return ImportDTO
   */
  @Override
  public ImportDTO getImports(final String name) {
    List<ImportDetailDTO> importDTOList = new ArrayList<>();
    Import imports = importService.findByName(name);
    List<ImportDetail> detailsList = importDetailService.findByImportId(
      imports.getImportId()
    );

    for (ImportDetail details : detailsList) {
      ImportDetailDTO dto = getDto(details, imports);
      importDTOList.add(dto);
    }
    ImportDTO dto = new ImportDTO();
    dto.setDetailsList(importDTOList);
    dto.setImportsName(imports.getImportName());
    return dto;
  }
  /**
   *
   *@return List<ImportDTO>
   */
  @Override
  public List<ImportDTO> getAllImports() {
    List<ImportDTO> list = new ArrayList<>();

    Iterable<Import> importsList = importService.findAll();

    for (Import imports : importsList) {
      log.info("inside imports " + imports.getImportName());
      ImportDTO dto = new ImportDTO();
      List<ImportDetail> detailsList = importDetailService.findByImportId(
        imports.getImportId()
      );
      List<ImportDetailDTO> detailsDTOList = new ArrayList<>();
      for (ImportDetail details : detailsList) {
        ImportDetailDTO detailDto = getDto(details, imports);
        detailsDTOList.add(detailDto);
      }
      dto.setImportsName(imports.getImportName());
      dto.setDetailsList(detailsDTOList);
      log.info("adding Dto for " + imports.getImportName());
      list.add(dto);
    }
    return list;
  }
  /**
   *
   * @param details
   * @param imports
   * @return ImportDetailDTO
   */
  private ImportDetailDTO getDto(final ImportDetail details,
          final Import imports) {
    ImportDetailDTO dto = new ImportDetailDTO();
    dto.setImportsDate(details.getImportBeginTime());
    dto.setFailedRecords(details.getFailedRecords());
    dto.setImportsName(imports.getImportName());
    dto.setStatus(details.getRecordStatus());
    dto.setSuccessRecords(details.getSuccessRecords());
    dto.setTotalRecords(details.getTotalRecords());
    dto.setImportsEndPoint(imports.getImportName() + " End Point");
    return dto;
  }
}
