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
public class ConnectionManager {

    private static ConnectionManager cm;

    /**
     * Returns the singleton instance of the connection manager.
     * 
     * @return the connection manager
     */
    public static ConnectionManager getInstance() {
        if (cm == null) {
            cm = new ConnectionManager();
        }

        return cm;
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
        cm = new ConnectionManager(unit, properties);
    }

    private EntityManagerFactory emf;

    private ConnectionManager() {
    }

    private ConnectionManager(String unit, Map<String, String> properties) {

        emf = Persistence.createEntityManagerFactory(unit, properties);
    }

    /**
     * Gets the entity manager for the hibernate connected database.
     * 
     * @return the entity manager
     */
    public EntityManager getEntityManager() {
        if (emf != null) {
            return emf.createEntityManager();
        } else {
            throw new RuntimeException("Database connection not initialized");
        }
    }

}
