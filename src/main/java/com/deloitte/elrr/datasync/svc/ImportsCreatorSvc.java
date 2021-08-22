package com.deloitte.elrr.datasync.svc;

import java.util.List;

import org.springframework.stereotype.Service;

import com.deloitte.elrr.datasync.dto.ImportDTO;

@Service
public interface ImportsCreatorSvc {

	public ImportDTO  getImports(String name);
	public List<ImportDTO> getAllImports();
}

