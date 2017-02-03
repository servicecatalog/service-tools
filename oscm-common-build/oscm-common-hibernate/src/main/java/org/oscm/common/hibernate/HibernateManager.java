/*******************************************************************************
 *                                                                              
 *  Copyright FUJITSU LIMITED 2016                                           
 *                                                                                                                                 
 *  Creation Date: Dec 7, 2016                                                      
 *                                                                              
 *******************************************************************************/

package org.oscm.common.hibernate;

import java.util.Map;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;

/**
 * Singleton class for managing hibernate database connection.
 * 
 * @author miethaner
 */
public class HibernateManager {

    private static HibernateManager hm = null;

    /**
     * Returns the singleton instance of the hibernate manager.
     * 
     * @return the hibernate manager
     */
    public static HibernateManager getInstance() {
        if (hm == null) {
            throw new RuntimeException("Database connection not initialized");
        }

        return hm;
    }

    /**
     * Initializes the connection manager for the given persistence unit with
     * the given properties. This overwrites the previous connection manager
     * instance.
     * 
     * @param unit
     *            the persistence unit
     * @param properties
     *            the hibernate properties
     */
    public static void init(String unit, Map<String, String> properties) {
        hm = new HibernateManager(unit, properties);
    }

    /**
     * Initialization method for test purposes only.
     * 
     * @param emf
     *            the entity manager factory
     */
    public static void init(EntityManagerFactory emf) {
        hm = new HibernateManager(emf);
    }

    private EntityManagerFactory emf;

    private HibernateManager(String unit, Map<String, String> properties) {

        emf = Persistence.createEntityManagerFactory(unit, properties);
    }

    private HibernateManager(EntityManagerFactory emf) {
        this.emf = emf;
    }

    /**
     * Gets the entity manager for the hibernate connected database.
     * 
     * @return the entity manager
     */
    public EntityManager getEntityManager() {
        return emf.createEntityManager();
    }

    @Override
    protected void finalize() throws Throwable {
        emf.close();
    }
}
