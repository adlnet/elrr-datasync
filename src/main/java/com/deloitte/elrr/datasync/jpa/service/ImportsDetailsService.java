package com.deloitte.elrr.datasync.jpa.service;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Service;

import com.deloitte.elrr.datasync.entity.Imports;
import com.deloitte.elrr.datasync.entity.ImportsDetails;
import com.deloitte.elrr.datasync.repository.ImportsDetailsRepository;
import com.deloitte.elrr.datasync.repository.ImportsRepository;
 
@Service
public class ImportsDetailsService implements CommonSvc<ImportsDetails, Long>{

	private final ImportsDetailsRepository importsDetailsRepository;

	public ImportsDetailsService(final ImportsDetailsRepository importsDetailsRepository) {
		this.importsDetailsRepository = importsDetailsRepository;
	}

	@Override
	public CrudRepository<ImportsDetails, Long> getRepository() {
		return this.importsDetailsRepository;
	}
 
	@Override
	public Long getId(ImportsDetails entity) {
		// TODO Auto-generated method stub
		return null;
	}


}
