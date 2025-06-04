package com.deloitte.elrr.datasync.jpa.service;

import java.io.Serializable;
import java.util.Optional;

import org.springframework.data.repository.CrudRepository;

import com.deloitte.elrr.datasync.exception.ResourceNotFoundException;
import com.deloitte.elrr.datasync.util.Generated;

/**
 * @author mnelakurti
 * @param <T>
 * @param <newId>
 */
public interface CommonSvc<T, newId extends Serializable> {

    /**
     * @return Iterable<T>
     */
    default Iterable<T> findAll() {
        return getRepository().findAll();
    }

    /**
     * @param id
     * @return Optional<T>
     */
    default Optional<T> get(newId id) {
        return getRepository().findById(id);
    }

    /**
     * @param entity
     * @return T
     */
    default T save(T entity) {
        return getRepository().save(entity);
    }

    /**
     * @param entities
     * @return Iterable<T>
     */
    @Generated
    default Iterable<T> saveAll(Iterable<T> entities) {
        return getRepository().saveAll(entities);
    }

    /**
     * @param id
     * @throws ResourceNotFoundException
     */
    @Generated
    default void delete(newId id) throws ResourceNotFoundException {
        try {
            if (getRepository().existsById(id)) {
                getRepository().deleteById(id);
            } else {
                throw new ResourceNotFoundException(
                        " Id not found for delete : " + id);
            }
        } catch (IllegalArgumentException e) {
            throw new ResourceNotFoundException("Record to update not found: "
                    + id);
        }
    }

    /**
     * @author phleven
     */
    default void deleteAll() {
        getRepository().deleteAll();
    }

    /**
     * @param entity
     * @throws ResourceNotFoundException
     */
    @Generated
    default void update(T entity) {
        try {

            if (getRepository().existsById(getId(entity))) {
                getRepository().save(entity);
            } else {
                throw new ResourceNotFoundException(
                        "Record to update not found: " + entity);
            }

        } catch (IllegalArgumentException e) {
            throw new ResourceNotFoundException("Record to update not found: "
                    + entity);
        }
    }

    /**
     * @param entity
     * @return NewId
     */

    newId getId(T entity);

    /**
     * @return CrudRepository<T, NewId>
     */
    @Generated
    CrudRepository<T, newId> getRepository();
}
