package com.deloitte.elrr.datasync.jpa.service;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Service;

import com.deloitte.elrr.datasync.entity.Import;
import com.deloitte.elrr.datasync.repository.ImportRepository;
 
@Service
public class ImportService implements CommonSvc<Import, Long>{

	private final ImportRepository importsRepository;

	public ImportService(final ImportRepository importsRepository) {
		this.importsRepository = importsRepository;
	}

	public Import findByName(String name) {
		return importsRepository.findByName(name);
	}
	@Override
	public CrudRepository<Import, Long> getRepository() {
		return this.importsRepository;
	}

  	@Override
	public Long getId(Import entity) {
		return null;
	}


}
