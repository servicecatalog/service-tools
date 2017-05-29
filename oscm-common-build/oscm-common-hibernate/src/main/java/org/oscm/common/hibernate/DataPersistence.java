/*******************************************************************************
 *                                                                              
 *  Copyright FUJITSU LIMITED 2016                                           
 *                                                                                                                                 
 *  Creation Date: Jul 4, 2016                                                      
 *                                                                              
 *******************************************************************************/

package org.oscm.common.hibernate;

import java.lang.reflect.Field;
import java.lang.reflect.ParameterizedType;
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

import org.hibernate.NonUniqueObjectException;
import org.oscm.common.interfaces.enums.Messages;
import org.oscm.common.interfaces.enums.Operation;
import org.oscm.common.interfaces.events.GenericPublisher;
import org.oscm.common.interfaces.exceptions.CacheException;
import org.oscm.common.interfaces.exceptions.ConflictException;
import org.oscm.common.interfaces.exceptions.NotFoundException;
import org.oscm.common.interfaces.exceptions.ServiceException;

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
     * Constructs new data persistence.
     */
    public DataPersistence() {
        this(null);
    }

    /**
     * Constructs new data persistence. Changes will be published with the given
     * publisher.
     * 
     * @param publisher
     *            the publisher to inform of changes
     */
    @SuppressWarnings("unchecked")
    public DataPersistence(GenericPublisher<? super D> publisher) {
        this.entityManager = HibernateManager.getInstance().getEntityManager();
        this.publisher = publisher;

        this.clazz = (Class<D>) ((ParameterizedType) this.getClass()
                .getGenericSuperclass()).getActualTypeArguments()[0];
    }

    @Override
    protected void finalize() throws Throwable {
        entityManager.close();
    }

    /**
     * Creates a new entity in the persistence and publishes it if necessary.
     * 
     * @param entity
     *            the entity to create
     * @param publish
     *            true if entity has to be published
     * @return the entity id
     * @throws ConflictException
     *             if the primary key already exists or a contraint is violated
     */
    protected D createData(D entity, boolean publish) throws ServiceException {

        EntityTransaction transaction = entityManager.getTransaction();
        try {
            transaction.begin();
            entity.setId(null);
            entity.setETag(ETAG_INIT);
            entity.setLastOperation(Operation.CREATED);
            entity.setPublished(new Boolean(!publish));
            entityManager.persist(entity);
            entityManager.flush();
            transaction.commit();

            if (publish && publisher != null) {
                publisher.publish(entity, () -> confirm(entity.getId()));
            }

            return entity;

        } catch (EntityExistsException | NonUniqueObjectException
                | IllegalArgumentException e) {
            if (transaction.isActive()) {
                transaction.rollback();
            }
            throw new ConflictException(Messages.ENTITY_ALREADY_EXISTS, e);
        }
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
     * @throws NotFoundException
     *             if the entity was not found
     * @throws CacheException
     *             if the entity was not modified
     */
    protected D readData(Long id, Long etag) throws ServiceException {

        try {
            D entity = entityManager.getReference(clazz, id);

            if (entity.getLastOperation() == Operation.DELETED) {
                throw new NotFoundException(Messages.ENTITY_NOT_FOUND);

            }

            if (etag != null && etag.equals(entity.getETag())) {
                throw new CacheException(Messages.ENTITY_NOT_MODIFIED);
            }

            return entity;

        } catch (EntityNotFoundException e) {
            throw new NotFoundException(Messages.ENTITY_NOT_FOUND, e);
        }
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
     * @throws ServiceException
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
     * @throws NotFoundException
     *             if the entity was not found
     */
    protected D readSingleData(String namedQuery,
            Map<String, Object> parameters) throws ServiceException {

        TypedQuery<D> query = entityManager.createNamedQuery(namedQuery, clazz);

        for (String key : parameters.keySet()) {
            query.setParameter(key, parameters.get(key));
        }

        try {
            return query.getSingleResult();
        } catch (NoResultException | NonUniqueResultException e) {
            throw new NotFoundException(Messages.NO_RESULT, e);
        }
    }

    /**
     * Updates the given entity and publishes it if necessary.
     * 
     * @param entity
     *            the entity to update with
     * @param publish
     *            true if entity has to be published
     * @throws NotFoundException
     *             if the entity to update was not found
     * @throws CacheException
     *             if the entity was modified by other source
     */
    protected D updateData(D entity, Long etag, boolean publish)
            throws ServiceException {

        EntityTransaction transaction = entityManager.getTransaction();
        try {
            D old = entityManager.getReference(clazz, entity.getId());

            if (old.getLastOperation() == Operation.DELETED) {
                throw new NotFoundException(Messages.ENTITY_NOT_FOUND);
            }

            if (etag != null) {
                if (!entity.getETag().equals(old.getETag())) {
                    throw new CacheException(Messages.ENTITY_WAS_MODIFIED);
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
            entity.setPublished(new Boolean(!publish));

            D newEntity = entityManager.merge(entity);
            transaction.commit();

            if (publish && publisher != null) {
                publisher.publish(newEntity, () -> confirm(newEntity.getId()));
            }

            return entity;
        } catch (EntityNotFoundException e) {
            if (transaction.isActive()) {
                transaction.rollback();
            }
            throw new NotFoundException(Messages.ENTITY_NOT_FOUND, e);
        }
    }

    /**
     * Deletes (soft) the given entity and publishes it if necessary.
     * 
     * @param id
     *            the entity id
     * @param publish
     *            true if entity has to be published
     * @throws NotFoundException
     *             if the entity to delete was not found
     */
    protected D deleteData(Long id, boolean publish) throws ServiceException {

        EntityTransaction transaction = entityManager.getTransaction();
        try {

            D entity = entityManager.getReference(clazz, id);

            if (entity.getLastOperation() == Operation.DELETED) {
                throw new NotFoundException(Messages.ENTITY_NOT_FOUND);
            }

            transaction.begin();
            entity.setLastOperation(Operation.DELETED);
            entity.setPublished(new Boolean(!publish));
            transaction.commit();

            if (publish && publisher != null) {
                publisher.publish(entity, () -> confirm(id));
            }

            return entity;

        } catch (EntityNotFoundException e) {
            if (transaction.isActive()) {
                transaction.rollback();
            }
            throw new NotFoundException(Messages.ENTITY_NOT_FOUND, e);
        }
    }

    /**
     * Confirm the publishing of the entity with the given id.
     * 
     * @param id
     *            the entity id
     * @throws ServiceException
     */
    protected void confirm(Long id) throws ServiceException {
        // new entitymanager needed because this method is used in callbacks
        // which can be called from different threads
        EntityManager localEm = HibernateManager.getInstance()
                .getEntityManager();
        EntityTransaction transaction = localEm.getTransaction();

        transaction.begin();
        D entity = localEm.getReference(clazz, id);
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
     * @throws NotFoundException
     *             if the entity was not found
     */
    protected <F extends DataObject> F readForeignData(Class<F> foreign,
            Long id) throws ServiceException {

        try {
            F entity = entityManager.getReference(foreign, id);

            if (entity.getLastOperation() == Operation.DELETED) {
                throw new NotFoundException(Messages.ENTITY_NOT_FOUND);
            }

            return entity;

        } catch (EntityNotFoundException e) {
            throw new NotFoundException(Messages.ENTITY_NOT_FOUND, e);
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
     * @throws NotFoundException
     *             if the entity was not found
     */
    protected <F extends ProxyObject> F readForeignProxy(Class<F> foreign,
            Long id) throws ServiceException {

        try {
            F entity = entityManager.getReference(foreign, id);

            if (entity.getLastOperation() == Operation.DELETED) {
                throw new NotFoundException(Messages.ENTITY_NOT_FOUND);
            }

            return entity;

        } catch (EntityNotFoundException e) {
            throw new NotFoundException(Messages.ENTITY_NOT_FOUND, e);
        }
    }

    /**
     * Publish all unpublished entities.
     * 
     * @throws ServiceException
     */
    public void publishUnpublished() throws ServiceException {
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

            List<D> list = readAllData(queryName, Collections.emptyMap(), null,
                    null);

            for (D entity : list) {
                publisher.publish(entity, () -> confirm(entity.getId()));
            }
        }
    }

    /**
     * (Re-)Publish all entities.
     * 
     * @throws ServiceException
     */
    public void publishAll() throws ServiceException {
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

            List<D> list = readAllData(queryName, Collections.emptyMap(), null,
                    null);

            EntityTransaction transaction = entityManager.getTransaction();

            transaction.begin();
            for (D action : list) {
                action.setPublished(Boolean.FALSE);
            }
            transaction.commit();

            for (D entity : list) {
                publisher.publish(entity, () -> confirm(entity.getId()));
            }
        }
    }
}
