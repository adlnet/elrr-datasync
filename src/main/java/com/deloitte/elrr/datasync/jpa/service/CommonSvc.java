package com.deloitte.elrr.datasync.jpa.service;

import com.deloitte.elrr.datasync.exception.ResourceNotFoundException;

import java.io.Serializable;
import java.util.Optional;
import org.springframework.data.repository.CrudRepository;

/**
 * @author mnelakurti
 * @param <T>
 * @param <newId>
 */
public interface CommonSvc<T, newId extends Serializable> {

    /**
     *
     * @return Iterable<T>
     */
    default Iterable<T> findAll() {
        return getRepository().findAll();
    }
    /**
     *
     * @param id
     * @return Optional<T>
     */
    default Optional<T> get(newId id) {
        return getRepository().findById(id);
    }
    /**
     *
     * @param entity
     * @return T
     */
    default T save(T entity) {
        return getRepository().save(entity);
    }
    /**
     *
     * @param entities
     * @return  Iterable<T>
     */
    default Iterable<T> saveAll(Iterable<T> entities) {
        return getRepository().saveAll(entities);
    }
    /**
     *
     * @param id
     * @throws ResourceNotFoundException
     */
    default void delete(newId id) throws ResourceNotFoundException {
        if (getRepository().existsById(id)) {
            getRepository().deleteById(id);
        } else {
            throw new ResourceNotFoundException(
                    " Id not found for delete : " + id);
        }
    }
    /**
     *
     */
    default void deleteAll() {
        getRepository().deleteAll();
    }
    /**
     *
     * @param entity
     * @throws ResourceNotFoundException
     */
    default void update(T entity) throws ResourceNotFoundException {
        if (getRepository().existsById(getId(entity))) {
            getRepository().save(entity);
        } else {
            throw new ResourceNotFoundException(
                    "Not found record in DB to update: " + entity);
        }
    }
    /**
     *
     * @param entity
     * @return NewId
     */
    newId getId(T entity);
    /**
     *
     * @return CrudRepository<T, NewId>
     */
    CrudRepository<T, newId> getRepository();
}
