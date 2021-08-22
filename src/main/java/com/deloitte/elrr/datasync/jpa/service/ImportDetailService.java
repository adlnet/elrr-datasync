package com.deloitte.elrr.datasync.jpa.service;

import java.util.List;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Service;

import com.deloitte.elrr.datasync.entity.Import;
import com.deloitte.elrr.datasync.entity.ImportDetail;
import com.deloitte.elrr.datasync.repository.ImportDetailRepository;
import com.deloitte.elrr.datasync.repository.ImportRepository;
 
 
@Service
public class ImportDetailService implements CommonSvc<ImportDetail, Long>{

	private final ImportDetailRepository importsDetailsRepository;

	public ImportDetailService(final ImportDetailRepository importsDetailsRepository) {
		this.importsDetailsRepository = importsDetailsRepository;
	}

	
	public List<ImportDetail> findByImportId(long id) {
		return importsDetailsRepository.findByImportId(id);
	}
	
	@Override
	public CrudRepository<ImportDetail, Long> getRepository() {
		return this.importsDetailsRepository;
	}
 


	@Override
	public Long getId(ImportDetail entity) {
		// TODO Auto-generated method stub
		return null;
	}

}
