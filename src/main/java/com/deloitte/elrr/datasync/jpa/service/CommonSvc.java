package com.deloitte.elrr.datasync.jpa.service;

import java.io.Serializable;
import java.util.Optional;

import org.springframework.data.repository.CrudRepository;

import com.deloitte.elrr.datasync.exception.ResourceNotFoundException;

/**
 * @author mnelakurti
 * @param <T>
 * @param <I>
 */
public interface CommonSvc<T, I extends Serializable> {

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
    default Optional<T> get(I id) {
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
    default Iterable<T> saveAll(Iterable<T> entities) {
        return getRepository().saveAll(entities);
    }

    /**
     * @param id
     * @throws ResourceNotFoundException
     */
    default void delete(I id) throws ResourceNotFoundException {
        if (getRepository().existsById(id)) {
            getRepository().deleteById(id);
        } else {
            throw new ResourceNotFoundException(
                    " Id not found for delete : " + id);
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
    default void update(T entity) {
        if (getRepository().existsById(getId(entity))) {
            getRepository().save(entity);
        } else {
            throw new ResourceNotFoundException(
                    "Record to update not found: " + entity);
        }
    }

    /**
     * @param entity
     * @return I
     */
    I getId(T entity);

    /**
     * @return CrudRepository<T, I>
     */
    CrudRepository<T, I> getRepository();
}
