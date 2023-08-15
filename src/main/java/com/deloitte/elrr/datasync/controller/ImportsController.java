/**
 *
 */

package com.deloitte.elrr.datasync.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.deloitte.elrr.datasync.dto.ImportDTO;
import com.deloitte.elrr.datasync.svc.ImportsCreatorSvc;

import lombok.extern.slf4j.Slf4j;


@CrossOrigin(origins = {
        "http://ec2-18-116-20-188.us-east-2.compute.amazonaws.com:3001",
        "http://ec2-18-116-20-188.us-east-2.compute.amazonaws.com:5000" })
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

    String regex = "^[a-zA-Z0-9-]+$";
    if (!importsName.matches(regex))
    {
      return null;
    }

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
