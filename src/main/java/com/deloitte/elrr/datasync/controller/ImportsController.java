/**
 *
 */

package com.deloitte.elrr.datasync.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.deloitte.elrr.datasync.dto.ImportDTO;
import com.deloitte.elrr.datasync.svc.ImportsCreatorSvc;

import lombok.extern.slf4j.Slf4j;


@RestController
@RequestMapping("api")
@Slf4j
public class ImportsController {

  /**
   *
   */
  @Autowired
  private ImportsCreatorSvc svc;

  // @CrossOrigin(origins = {reactUrl1})
  /**
   *
   * @param importsName
   * @return ImportDTO
   */
  @GetMapping("/getImports")
  public ImportDTO getImports(
    @RequestParam(value = "name", required = true) final String importsName) {
    return svc.getImports(importsName);
  }

  /**
   *
   * @return List<ImportDTO>
   */
  @GetMapping("/getAllImports")
  public List<ImportDTO> getAllImports() {
    return svc.getAllImports();
  }
}
