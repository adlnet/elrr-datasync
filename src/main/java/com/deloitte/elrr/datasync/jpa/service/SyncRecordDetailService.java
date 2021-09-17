package com.deloitte.elrr.datasync.jpa.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Service;

import com.deloitte.elrr.datasync.entity.SyncRecordDetail;
import com.deloitte.elrr.datasync.repository.SyncRecordDetailRepository;

import lombok.extern.slf4j.Slf4j;

@Service
@Slf4j
public class SyncRecordDetailService implements CommonSvc<SyncRecordDetail, Long> {

	@Autowired
	SyncRecordDetailRepository syncRecordDetailsRepository;

	public void process(Object object) {
		syncRecordDetailsRepository.save((SyncRecordDetail) object);
	}

	public List<SyncRecordDetail> findUnprocessed() {
		return syncRecordDetailsRepository.findUnprocessed("inserted");
	}

	public List<SyncRecordDetail> findBySyncRecordId(long recordId) {
		return syncRecordDetailsRepository.findBySyncRecordId(recordId);
	}

	@Override
	public Long getId(SyncRecordDetail entity) {
		return null;
	}

	@Override
	public CrudRepository<SyncRecordDetail, Long> getRepository() {
		return this.syncRecordDetailsRepository;
	}

}
