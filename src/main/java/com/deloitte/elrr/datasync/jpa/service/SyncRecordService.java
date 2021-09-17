package com.deloitte.elrr.datasync.jpa.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Service;

import com.deloitte.elrr.datasync.entity.SyncRecord;
import com.deloitte.elrr.datasync.repository.SyncRecordDetailRepository;
import com.deloitte.elrr.datasync.repository.SyncRecordRepository;

import lombok.extern.slf4j.Slf4j;

@Service
@Slf4j
public class SyncRecordService implements CommonSvc<SyncRecord, Long> {

	private static String inserted = "INSERTED";

	@Autowired
	SyncRecordRepository syncRecordRepository;

	@Autowired
	SyncRecordDetailRepository syncRecordDetailsRepository;

	public List<SyncRecord> findUnprocessed() {
		return syncRecordRepository.findUnprocessed("inserted");
	}

	public SyncRecord createSyncRecord(String key, long importDetailsId) {
		SyncRecord syncRecord = new SyncRecord();
		syncRecord.setSyncKey(key);
		syncRecord.setRecordStatus(inserted);
		syncRecord.setImportdetailId(importDetailsId);
		syncRecordRepository.save(syncRecord);
		return syncRecord;
	}

	public SyncRecord findExistingRecord(String key) {
		return syncRecordRepository.findExisting(inserted, key);
	}

	@Override
	public Long getId(SyncRecord entity) {
		return entity.getSyncRecordId();
	}

	@Override
	public CrudRepository<SyncRecord, Long> getRepository() {
		return this.syncRecordRepository;
	}

}
