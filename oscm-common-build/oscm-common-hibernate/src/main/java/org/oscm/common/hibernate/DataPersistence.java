/*******************************************************************************
 *                                                                              
 *  Copyright FUJITSU LIMITED 2016                                           
 *                                                                                                                                 
 *  Creation Date: Jul 4, 2016                                                      
 *                                                                              
 *******************************************************************************/

package org.oscm.common.hibernate;

import java.lang.reflect.Field;
import java.util.Collections;
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
import org.oscm.common.interfaces.events.GenericPublisher;
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
     * @param namedQuery
     *            the identifier of the named query to read all entities
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
     * @param namedQuery
     *            the identifier of the named query to read all entities
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
     * @param namedQuery
     *            the identifier of the named query to read a single entities
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
        return updateData(entity, null);
    }

    /**
     * Updates the given entity and publishes it.
     * 
     * @param entity
     *            the entity to update with
     * @throws ComponentException
     */
    protected D updateData(D entity, Long etag) throws ComponentException {

        EntityTransaction transaction = entityManager.getTransaction();
        try {
            D old = entityManager.getReference(clazz, entity.getId());

            if (old.getLastOperation() == Operation.DELETED) {
                throw new NotFoundException(null, ""); // TODO add error message
            }

            if (etag != null) {
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
     * Deletes (soft) the given entity and publishes it.
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

    /**
     * Confirm the publishing of the given entity.
     * 
     * @param entity
     *            the published entity
     * @throws ComponentException
     */
    protected void confirm(D entity) throws ComponentException {
        EntityTransaction transaction = entityManager.getTransaction();

        transaction.begin();
        entity.setPublished(Boolean.TRUE);
        transaction.commit();
    }

    /**
     * Read the foreign data entity of the given class with the given id.
     * 
     * @param foreign
     *            the class of the entity
     * @param id
     *            the id of the entity
     * @return the foreign data entity
     * @throws ComponentException
     */
    protected <F extends DataObject> F readForeignData(Class<F> foreign,
            Long id) throws ComponentException {

        try {
            F entity = entityManager.getReference(foreign, id);

            if (entity.getLastOperation() == Operation.DELETED) {
                throw new NotFoundException(null, "");
                // TODO add error message
            }

            return entity;

        } catch (EntityNotFoundException e) {
            throw new NotFoundException(null, "", e); // TODO add error message
        }
    }

    /**
     * Read the foreign proxy entity of the given class with the given id.
     * 
     * @param foreign
     *            the class of the entity
     * @param id
     *            the id of the entity
     * @return the foreign proxy entity
     * @throws ComponentException
     */
    protected <F extends ProxyObject> F readForeignProxy(Class<F> foreign,
            Long id) throws ComponentException {

        try {
            F entity = entityManager.getReference(foreign, id);

            if (entity.getLastOperation() == Operation.DELETED) {
                throw new NotFoundException(null, "");
                // TODO add error message
            }

            return entity;

        } catch (EntityNotFoundException e) {
            throw new NotFoundException(null, "", e); // TODO add error message
        }
    }

    /**
     * Publish all unpublished entities.
     * 
     * @throws ComponentException
     */
    public void publishUnpublished() throws ComponentException {
        if (publisher != null) {

            String queryName;
            try {
                Field f = clazz.getField("QUERY_READ_UNPUBLISHED");
                queryName = (String) f.get(null);
            } catch (NoSuchFieldException | SecurityException
                    | IllegalArgumentException | IllegalAccessException e) {
                throw new RuntimeException(
                        "Unable to find query name to read all unpublished entities");
            }

            List<D> list = readAllData(queryName, Collections.emptyMap());

            for (D action : list) {
                publisher.publish(action, () -> confirm(action));
            }
        }
    }

    /**
     * (Re-)Publish all entities.
     * 
     * @throws ComponentException
     */
    public void publishAll() throws ComponentException {
        if (publisher != null) {

            String queryName;
            try {
                Field f = clazz.getField("QUERY_READ_ALL");
                queryName = (String) f.get(null);
            } catch (NoSuchFieldException | SecurityException
                    | IllegalArgumentException | IllegalAccessException e) {
                throw new RuntimeException(
                        "Unable to find query name to read all entities");
            }

            List<D> list = readAllData(queryName, Collections.emptyMap());

            EntityTransaction transaction = entityManager.getTransaction();

            transaction.begin();
            for (D action : list) {
                action.setPublished(Boolean.TRUE);
            }
            transaction.commit();

            for (D action : list) {
                publisher.publish(action, () -> confirm(action));
            }
        }
    }
}
