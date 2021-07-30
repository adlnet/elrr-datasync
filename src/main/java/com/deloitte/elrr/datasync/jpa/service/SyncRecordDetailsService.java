package com.deloitte.elrr.datasync.jpa.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Service;

import com.deloitte.elrr.datasync.entity.SyncRecordDetails;
import com.deloitte.elrr.datasync.repository.SyncRecordDetailsRepository;

import lombok.extern.slf4j.Slf4j;

@Service
@Slf4j
public class SyncRecordDetailsService implements CommonSvc<SyncRecordDetails, Long>{

	@Autowired
	SyncRecordDetailsRepository syncRecordDetailsRepository;
	
	
	public void process(Object object) {
		syncRecordDetailsRepository.save((SyncRecordDetails)object);
	}

	public List<SyncRecordDetails> findUnprocessed() {
		return syncRecordDetailsRepository.findUnprocessed("inserted");
	}
	
	public List<SyncRecordDetails> findBySyncRecordId(long recordId) {
		return syncRecordDetailsRepository.findBySyncRecordId(recordId);
	}
	
	@Override
	public Long getId(SyncRecordDetails entity) {
		// TODO Auto-generated method stub
		return null;
	}


	@Override
	public CrudRepository<SyncRecordDetails, Long> getRepository() {
		return this.syncRecordDetailsRepository;
	}

	 	
}
