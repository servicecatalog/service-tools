/*******************************************************************************
 *                                                                              
 *  Copyright FUJITSU LIMITED 2016                                           
 *                                                                                                                                 
 *  Creation Date: Jul 6, 2016                                                      
 *                                                                              
 *******************************************************************************/

package org.oscm.common.hibernate;

import java.lang.reflect.ParameterizedType;
import java.util.List;
import java.util.Map;

import javax.persistence.EntityManager;
import javax.persistence.EntityNotFoundException;
import javax.persistence.EntityTransaction;
import javax.persistence.NoResultException;
import javax.persistence.NonUniqueResultException;
import javax.persistence.TypedQuery;

import org.oscm.common.interfaces.enums.Messages;
import org.oscm.common.interfaces.enums.Operation;
import org.oscm.common.interfaces.exceptions.CacheException;
import org.oscm.common.interfaces.exceptions.NotFoundException;
import org.oscm.common.interfaces.exceptions.ServiceException;

/**
 * Super class for all proxy persistence classes
 * 
 * @author miethaner
 */
public class ProxyPersistence<P extends ProxyObject> {

    private EntityManager entityManager;
    private Class<P> clazz;

    @SuppressWarnings("unchecked")
    public ProxyPersistence() {
        this.entityManager = HibernateManager.getInstance().getEntityManager();

        this.clazz = (Class<P>) ((ParameterizedType) this.getClass()
                .getGenericSuperclass()).getActualTypeArguments()[0];
    }

    @Override
    protected void finalize() throws Throwable {
        entityManager.close();
    }

    /**
     * Reads the proxy entity from the persistence. Checks for modification with
     * given etag and throws exception if not modified.
     * 
     * @param id
     *            the entity id
     * @param etag
     *            the etag to compare (ignored if null)
     * @return the entity
     * @throws ServiceException
     */
    protected P readProxy(Long id, Long etag) throws ServiceException {

        try {
            P entity = entityManager.getReference(clazz, id);

            if (entity.getLastOperation() == Operation.DELETED) {
                throw new NotFoundException(Messages.ERROR, "");
                // TODO add error message
            }

            if (etag != null && etag.equals(entity.getETag())) {
                throw new CacheException(Messages.ERROR, "");
                // TODO add error message
            }

            return entity;

        } catch (EntityNotFoundException e) {
            throw new NotFoundException(Messages.ERROR, e); // TODO add error
                                                            // message
        }
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
     * @throws ServiceException
     */
    protected List<P> readAllProxy(String namedQuery,
            Map<String, Object> params, Long limit, Long offset) {

        TypedQuery<P> query = entityManager.createNamedQuery(namedQuery, clazz);

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
     * @throws ServiceException
     */
    protected P readSingleProxy(String namedQuery,
            Map<String, Object> parameters) throws ServiceException {

        TypedQuery<P> query = entityManager.createNamedQuery(namedQuery, clazz);

        for (String key : parameters.keySet()) {
            query.setParameter(key, parameters.get(key));
        }

        try {
            return query.getSingleResult();
        } catch (NoResultException | NonUniqueResultException e) {
            throw new NotFoundException(Messages.ERROR, ""); // TODO add error
                                                             // message
        }
    }

    /**
     * Merges the given proxy entity into the persistence
     * 
     * @param entity
     *            the entity to merge
     * @throws ServiceException
     */
    protected void mergeProxy(P entity) throws ServiceException {

        EntityTransaction transaction = entityManager.getTransaction();
        transaction.begin();
        entityManager.merge(entity);
        transaction.commit();
    }

}
