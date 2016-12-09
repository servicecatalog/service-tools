/*******************************************************************************
 *                                                                              
 *  Copyright FUJITSU LIMITED 2016                                           
 *                                                                                                                                 
 *  Creation Date: Jul 4, 2016                                                      
 *                                                                              
 *******************************************************************************/

package org.oscm.common.hibernate;

import java.util.List;
import java.util.Map;

import javax.persistence.EntityExistsException;
import javax.persistence.EntityManager;
import javax.persistence.EntityNotFoundException;
import javax.persistence.EntityTransaction;
import javax.persistence.NoResultException;
import javax.persistence.NonUniqueResultException;
import javax.persistence.TypedQuery;

import org.oscm.common.interfaces.enums.Operation;
import org.oscm.common.interfaces.event.GenericPublisher;
import org.oscm.common.interfaces.exceptions.CacheException;
import org.oscm.common.interfaces.exceptions.ComponentException;
import org.oscm.common.interfaces.exceptions.InternalException;
import org.oscm.common.interfaces.exceptions.NotFoundException;

/**
 * Super class for all data persistence classes.
 * 
 * @author miethaner
 */
public abstract class DataPersistence<D extends DataObject> {

    private static final Long ETAG_INIT = new Long(0);

    private EntityManager entityManager;
    private Class<D> clazz;
    private GenericPublisher<? super D> publisher;

    /**
     * Initializes the super class with the entity manager and others. The super
     * class takes care of closing the entity manager when the object is
     * destroyed.
     * 
     * @param entityManager
     *            the entity manager
     * @param clazz
     *            the class of the entity
     */
    protected void init(EntityManager entityManager, Class<D> clazz) {
        init(entityManager, clazz, null);
    }

    /**
     * Initializes the super class with the entity manager and others. The super
     * class takes care of closing the entity manager when the object is
     * destroyed.
     * 
     * @param entityManager
     *            the entity manager
     * @param clazz
     *            the class of the entity
     * @param publisher
     *            the publisher resource
     */
    protected void init(EntityManager entityManager, Class<D> clazz,
            GenericPublisher<? super D> publisher) {
        this.entityManager = entityManager;
        this.clazz = clazz;
        this.publisher = publisher;
    }

    @Override
    protected void finalize() throws Throwable {
        entityManager.close();
    }

    /**
     * Creates a new entity in the persistence and publishes it.
     * 
     * @param entity
     *            the entity to create
     * @return the entity id
     * @throws ComponentException
     */
    protected D createData(D entity) throws ComponentException {

        EntityTransaction transaction = entityManager.getTransaction();
        try {
            transaction.begin();
            entity.setId(null);
            entity.setETag(ETAG_INIT);
            entity.setLastOperation(Operation.CREATED);
            entity.setPublished(
                    publisher != null ? Boolean.FALSE : Boolean.TRUE);
            entityManager.persist(entity);
            entityManager.flush();
            transaction.commit();

            if (publisher != null) {
                publisher.publish(entity, () -> confirm(entity));
            }

            return entity;

        } catch (EntityExistsException | IllegalArgumentException e) {
            if (transaction.isActive()) {
                transaction.rollback();
            }
            throw new InternalException(null, "", e); // TODO add error message
        }
    }

    /**
     * Reads the data entity from the persistence.
     * 
     * @param id
     *            the entity id
     * @return the entity
     * @throws ComponentException
     */
    protected D readData(Long id) throws ComponentException {
        return readData(id, null);
    }

    /**
     * Reads the data entity from the persistence. Checks for modification with
     * given etag and throws exception if not modified.
     * 
     * @param id
     *            the entity id
     * @param etag
     *            the etag to compare (ignored if null)
     * @return the entity
     * @throws ComponentException
     */
    protected D readData(Long id, Long etag) throws ComponentException {

        try {
            D entity = entityManager.getReference(clazz, id);

            if (entity.getLastOperation() == Operation.DELETED) {
                throw new NotFoundException(null, "");
                // TODO add error message
            }

            if (etag != null && etag.equals(entity.getETag())) {
                throw new CacheException(null, "");
                // TODO add error message
            }

            return entity;

        } catch (EntityNotFoundException e) {
            throw new NotFoundException(null, "", e); // TODO add error message
        }
    }

    /**
     * Reads all entities for the given query with the given parameters.
     * 
     * @param id
     *            the entity id
     * @param params
     *            the map of parameters
     * @return the entity
     * @throws ComponentException
     */
    protected List<D> readAllData(String namedQuery,
            Map<String, Object> params) {
        return readAllData(namedQuery, params, null, null);
    }

    /**
     * Reads all entities for the given query with the given parameters and
     * pagination.
     * 
     * @param id
     *            the entity id
     * @param params
     *            the map of parameters
     * @param limit
     *            the maximum number of results (can be null)
     * @param offset
     *            the position of the first result (can be null)
     * @return the entity
     * @throws ComponentException
     */
    protected List<D> readAllData(String namedQuery, Map<String, Object> params,
            Long limit, Long offset) {

        TypedQuery<D> query = entityManager.createNamedQuery(namedQuery, clazz);

        for (String key : params.keySet()) {
            query.setParameter(key, params.get(key));
        }

        if (offset != null) {
            query.setFirstResult(offset.intValue());
        }
        if (limit != null) {
            query.setMaxResults(limit.intValue());
        }

        return query.getResultList();
    }

    /**
     * Reads one entity for the given query with the given parameters.
     * 
     * @param id
     *            the entity id
     * @param parameters
     *            the map of parameters
     * @return the entity
     * @throws ComponentException
     */
    protected D readSingleData(String namedQuery,
            Map<String, Object> parameters) throws ComponentException {

        TypedQuery<D> query = entityManager.createNamedQuery(namedQuery, clazz);

        for (String key : parameters.keySet()) {
            query.setParameter(key, parameters.get(key));
        }

        try {
            return query.getSingleResult();
        } catch (NoResultException | NonUniqueResultException e) {
            throw new NotFoundException(null, ""); // TODO add error message
        }
    }

    /**
     * Updates the given entity and publishes it.
     * 
     * @param entity
     *            the entity to update with
     * @throws ComponentException
     */
    protected D updateData(D entity) throws ComponentException {

        EntityTransaction transaction = entityManager.getTransaction();
        try {
            D old = entityManager.getReference(clazz, entity.getId());

            if (old.getLastOperation() == Operation.DELETED) {
                throw new NotFoundException(null, ""); // TODO add error message
            }

            if (entity.getETag() != null) {
                if (!entity.getETag().equals(old.getETag())) {
                    throw new CacheException(null, "");
                    // TODO add error message
                }
            } else {
                entity.setETag(old.getETag());
            }

            transaction.begin();
            if (old.getLastOperation() == Operation.CREATED
                    && old.isPublished() != null
                    && !old.isPublished().booleanValue()) {
                entity.setLastOperation(Operation.CREATED);
            } else {
                entity.setLastOperation(Operation.UPDATED);
            }
            entity.setPublished(
                    publisher != null ? Boolean.FALSE : Boolean.TRUE);

            D newEntity = entityManager.merge(entity);
            transaction.commit();

            if (publisher != null) {
                publisher.publish(newEntity, () -> confirm(newEntity));
            }

            return entity;
        } catch (EntityNotFoundException e) {
            if (transaction.isActive()) {
                transaction.rollback();
            }
            throw new NotFoundException(null, "", e); // TODO add error message
        }
    }

    /**
     * Deletes the given entity and publishes it.
     * 
     * @param id
     *            the entity id
     * @throws ComponentException
     */
    protected D deleteData(Long id) throws ComponentException {

        EntityTransaction transaction = entityManager.getTransaction();
        try {

            D entity = entityManager.getReference(clazz, id);

            if (entity.getLastOperation() == Operation.DELETED) {
                throw new NotFoundException(null, ""); // TODO add error message
            }

            transaction.begin();
            entity.setLastOperation(Operation.DELETED);
            entity.setPublished(
                    publisher != null ? Boolean.FALSE : Boolean.TRUE);
            transaction.commit();

            if (publisher != null) {
                publisher.publish(entity, () -> confirm(entity));
            }

            return entity;

        } catch (EntityNotFoundException e) {
            if (transaction.isActive()) {
                transaction.rollback();
            }
            throw new NotFoundException(null, "", e); // TODO add error message
        }
    }

    private void confirm(D entity) throws ComponentException {
        EntityTransaction transaction = entityManager.getTransaction();
        try {

            transaction.begin();
            entity.setPublished(Boolean.TRUE);
            transaction.commit();

        } catch (EntityNotFoundException e) {
            if (transaction.isActive()) {
                transaction.rollback();
            }
            throw new NotFoundException(null, "", e); // TODO add error message
        }
    }

    public abstract void publishUnpublished();

    public abstract void publishAll();
}
