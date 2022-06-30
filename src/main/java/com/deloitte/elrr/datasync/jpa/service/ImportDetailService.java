package com.deloitte.elrr.datasync.jpa.service;

import com.deloitte.elrr.datasync.entity.ImportDetail;
import com.deloitte.elrr.datasync.repository.ImportDetailRepository;
import java.util.List;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Service;

@Service
public class ImportDetailService implements CommonSvc<ImportDetail, Long> {
    /**
     *
     */
    private final ImportDetailRepository importDetailRepository;

    /**
     *
     * @param newimportDetailRepository
     */
    public ImportDetailService(
            final ImportDetailRepository newimportDetailRepository) {
        this.importDetailRepository = newimportDetailRepository;
    }

    /**
     *
     * @param id
     * @return List<ImportDetail>
     */
    public List<ImportDetail> findByImportId(final long id) {
        return importDetailRepository.findByImportId(id);
    }

    /**
     *
     * @return CrudRepository<ImportDetail, Long>
     */
    @Override
    public CrudRepository<ImportDetail, Long> getRepository() {
        return this.importDetailRepository;
    }

    /**
     *
     */
    @Override
    public Long getId(final ImportDetail entity) {

        if (entity != null) {
            return entity.getImportdetailId();
        } else {
            return null;
        }
    }
}
