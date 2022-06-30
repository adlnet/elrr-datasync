package com.deloitte.elrr.datasync.svc;

import com.deloitte.elrr.datasync.dto.ImportDTO;
import java.util.List;
import org.springframework.stereotype.Service;

@Service
public interface ImportsCreatorSvc {
  /**
   *
   * @param name
   * @return ImportDTO
   */
  ImportDTO getImports(String name);
  /**
   *
   * @return List<ImportDTO>
   */
  List<ImportDTO> getAllImports();
}
