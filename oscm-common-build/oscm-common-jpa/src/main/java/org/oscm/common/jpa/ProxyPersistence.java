/*******************************************************************************
 *                                                                              
 *  Copyright FUJITSU LIMITED 2016                                           
 *                                                                                                                                 
 *  Creation Date: Jul 6, 2016                                                      
 *                                                                              
 *******************************************************************************/

package org.oscm.common.jpa;

import javax.persistence.EntityManager;
import javax.persistence.EntityNotFoundException;
import javax.persistence.EntityTransaction;

import org.oscm.common.interfaces.enums.Operation;
import org.oscm.common.interfaces.exceptions.ComponentException;
import org.oscm.common.interfaces.exceptions.NotFoundException;

/**
 * Super class for all proxy persistence classes
 * 
 * @author miethaner
 */
public class ProxyPersistence {

    /**
     * Reads the entity from the persistence
     * 
     * @param entityManager
     *            the entity manager
     * @param clazz
     *            the entity class
     * @param id
     *            the entity id
     * @return the entity
     * @throws ComponentException
     */
    protected <P extends ProxyObject> P read(EntityManager entityManager,
            Class<P> clazz, Long id) throws ComponentException {

        try {
            P data = entityManager.getReference(clazz, id);

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
     * Merges the given entity into the persistence
     * 
     * @param entityManager
     *            the entity manager
     * @param clazz
     *            the entity class
     * @param content
     *            the content to merge
     * @throws ComponentException
     */
    protected <P extends ProxyObject> void merge(EntityManager entityManager,
            Class<P> clazz, P content) throws ComponentException {

        EntityTransaction transaction = entityManager.getTransaction();
        transaction.begin();
        entityManager.merge(content);
        transaction.commit();
    }

}
