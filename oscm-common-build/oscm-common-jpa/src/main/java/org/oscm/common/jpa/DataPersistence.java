/*******************************************************************************
 *                                                                              
 *  Copyright FUJITSU LIMITED 2016                                           
 *                                                                                                                                 
 *  Creation Date: Jul 4, 2016                                                      
 *                                                                              
 *******************************************************************************/

package org.oscm.common.jpa;

import javax.persistence.EntityExistsException;
import javax.persistence.EntityManager;
import javax.persistence.EntityNotFoundException;
import javax.persistence.EntityTransaction;

import org.oscm.common.interfaces.enums.Operation;
import org.oscm.common.interfaces.exceptions.CacheException;
import org.oscm.common.interfaces.exceptions.ComponentException;
import org.oscm.common.interfaces.exceptions.InternalException;
import org.oscm.common.interfaces.exceptions.NotFoundException;

/**
 * Super class for all data persistence classes
 * 
 * @author miethaner
 */
public abstract class DataPersistence<D extends DataObject> {

    private static final Long ETAG_INIT = new Long(0);

    private EntityManager entityManager;
    private Class<D> clazz;

    protected void init(EntityManager entityManager, Class<D> clazz) {
        this.entityManager = entityManager;
        this.clazz = clazz;
    }

    /**
     * Creates a new entity in the persistence and publishes it
     * 
     * @param content
     *            the content to create
     * @param publisher
     *            the publisher
     * @return the entity id
     * @throws ComponentException
     */
    protected D createData(D content) throws ComponentException {

        EntityTransaction transaction = entityManager.getTransaction();
        try {
            transaction.begin();
            content.setId(null);
            content.setETag(ETAG_INIT);
            content.setLastOperation(Operation.CREAT);
            content.setPublished(Boolean.FALSE);
            entityManager.persist(content);
            entityManager.flush();
            transaction.commit();

            return content;

        } catch (EntityExistsException | IllegalArgumentException e) {
            if (transaction.isActive()) {
                transaction.rollback();
            }
            throw new InternalException(null, "", e); // TODO add error message
        }
    }

    /**
     * Reads the data entity from the persistence
     * 
     * @param id
     *            the entity id
     * @return the entity
     * @throws ComponentException
     */
    protected D readData(Long id, Long etag) throws ComponentException {

        try {
            D data = entityManager.getReference(clazz, id);

            if (data.getLastOperation() == Operation.DELET) {
                throw new NotFoundException(null, "");
                // TODO add error message
            }

            if (etag != null && etag.equals(data.getETag())) {
                throw new CacheException(null, "");
                // TODO add error message
            }

            return data;

        } catch (EntityNotFoundException e) {
            throw new NotFoundException(null, "", e); // TODO add error message
        }
    }

    /**
     * Updates the given entity and publishes it
     * 
     * @param content
     *            the content to update with
     * @throws ComponentException
     */
    protected D updateData(D content) throws ComponentException {

        EntityTransaction transaction = entityManager.getTransaction();
        try {
            D old = entityManager.getReference(clazz, content.getId());

            if (old.getLastOperation() == Operation.DELET) {
                throw new NotFoundException(null, ""); // TODO add error message
            }

            if (content.getETag() != null) {
                if (!content.getETag().equals(old.getETag())) {
                    throw new CacheException(null, "");
                    // TODO add error message
                }
            } else {
                content.setETag(old.getETag());
            }

            transaction.begin();
            if (old.getLastOperation() == Operation.CREAT && old.isPublished() != null
                    && !old.isPublished().booleanValue()) {
                content.setLastOperation(Operation.CREAT);
            } else {
                content.setLastOperation(Operation.UPDAT);
            }
            content.setPublished(Boolean.FALSE);

            content = entityManager.merge(content);
            transaction.commit();

            return content;
        } catch (EntityNotFoundException e) {
            if (transaction.isActive()) {
                transaction.rollback();
            }
            throw new NotFoundException(null, "", e); // TODO add error message
        }
    }

    /**
     * Deletes the given entity and publishes it
     * 
     * @param id
     *            the entity id
     * @param publisher
     *            the publisher
     * @throws ComponentException
     */
    protected D deleteData(Long id) throws ComponentException {

        EntityTransaction transaction = entityManager.getTransaction();
        try {

            D entity = entityManager.getReference(clazz, id);

            if (entity.getLastOperation() == Operation.DELET) {
                throw new NotFoundException(null, ""); // TODO add error message
            }

            transaction.begin();
            entity.setLastOperation(Operation.DELET);
            entity.setPublished(Boolean.FALSE);
            transaction.commit();

            return entity;

        } catch (EntityNotFoundException e) {
            if (transaction.isActive()) {
                transaction.rollback();
            }
            throw new NotFoundException(null, "", e); // TODO add error message
        }
    }

    protected void confirm(Long id) throws ComponentException {
        EntityTransaction transaction = entityManager.getTransaction();
        try {

            D entity = entityManager.getReference(clazz, id);

            transaction.begin();
            if (entity.getLastOperation() == Operation.DELET) {
                entityManager.remove(entity);
            } else {
                entity.setPublished(Boolean.TRUE);
            }
            transaction.commit();

        } catch (EntityNotFoundException e) {
            if (transaction.isActive()) {
                transaction.rollback();
            }
            throw new NotFoundException(null, "", e); // TODO add error message
        }
    }
}
