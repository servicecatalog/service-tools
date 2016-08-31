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
import org.oscm.common.interfaces.exceptions.ComponentException;
import org.oscm.common.interfaces.exceptions.InternalException;
import org.oscm.common.interfaces.exceptions.NotFoundException;

/**
 * Super class for all data persistence classes
 * 
 * @author miethaner
 */
public abstract class DataPersistence {

    /**
     * Creates a new entity in the persistence and publishes it
     * 
     * @param entityManager
     *            the entity manager
     * @param content
     *            the content to create
     * @param publisher
     *            the publisher
     * @return the entity id
     * @throws ComponentException
     */
    protected <D extends DataObject> D create(EntityManager entityManager,
            D content) throws ComponentException {

        EntityTransaction transaction = entityManager.getTransaction();
        try {
            transaction.begin();
            content.setId(null);
            content.setLastOperation(Operation.CREATE);
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
     * @param entityManager
     *            the entity manager
     * @param clazz
     *            the data entity class
     * @param id
     *            the entity id
     * @return the entity
     * @throws ComponentException
     */
    protected <D extends DataObject> D read(EntityManager entityManager,
            Class<D> clazz, Long id) throws ComponentException {

        try {
            D data = entityManager.getReference(clazz, id);

            if (data.getLastOperation() == Operation.DELETE) {
                throw new NotFoundException(null, "");
                // TODO add error message
            }

            return data;

        } catch (EntityNotFoundException e) {
            throw new NotFoundException(null, "", e); // TODO add error message
        }
    }

    /**
     * Reads the proxy entity from the persistence
     * 
     * @param entityManager
     *            the entity manager
     * @param clazz
     *            the proxy entity class
     * @param id
     *            the entity id
     * @return the entity
     * @throws ComponentException
     */
    protected <D extends ProxyObject> D readProxy(EntityManager entityManager,
            Class<D> clazz, Long id) throws ComponentException {

        try {
            D data = entityManager.getReference(clazz, id);

            if (data.getLastOperation() == Operation.DELETE) {
                throw new NotFoundException(null, "");
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
     * @param entityManager
     *            the entity manager
     * @param clazz
     *            the entity class
     * @param content
     *            the content to update with
     * @param publisher
     *            the publisher
     * @throws ComponentException
     */
    protected <D extends DataObject> D update(EntityManager entityManager,
            Class<D> clazz, D content) throws ComponentException {

        EntityTransaction transaction = entityManager.getTransaction();
        try {
            D old = entityManager.getReference(clazz, content.getId());

            if (old.getLastOperation() == Operation.DELETE) {
                throw new NotFoundException(null, ""); // TODO add error message
            }

            transaction.begin();
            if (old.getLastOperation() == Operation.CREATE
                    && old.isPublished() != null
                    && !old.isPublished().booleanValue()) {
                content.setLastOperation(Operation.CREATE);
            } else {
                content.setLastOperation(Operation.UPDATE);
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
     * @param entityManager
     *            the entity manager
     * @param clazz
     *            the entity class
     * @param id
     *            the entity id
     * @param publisher
     *            the publisher
     * @throws ComponentException
     */
    protected <D extends DataObject> D delete(EntityManager entityManager,
            Class<D> clazz, Long id) throws ComponentException {

        EntityTransaction transaction = entityManager.getTransaction();
        try {

            D entity = entityManager.getReference(clazz, id);

            if (entity.getLastOperation() == Operation.DELETE) {
                throw new NotFoundException(null, ""); // TODO add error message
            }

            transaction.begin();
            entity.setLastOperation(Operation.DELETE);
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

    protected <D extends DataObject> void confirmPublish(
            EntityManager entityManager, Class<D> clazz, Long id)
            throws ComponentException {
        EntityTransaction transaction = entityManager.getTransaction();
        try {

            D entity = entityManager.getReference(clazz, id);

            transaction.begin();
            if (entity.getLastOperation() == Operation.DELETE) {
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
