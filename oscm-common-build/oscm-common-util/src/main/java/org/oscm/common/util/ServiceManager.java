/*******************************************************************************
 *                                                                              
 *  Copyright FUJITSU LIMITED 2016                                           
 *                                                                                                                                 
 *  Creation Date: Dec 6, 2016                                                      
 *                                                                              
 *******************************************************************************/

package org.oscm.common.util;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.function.Supplier;

/**
 * Singleton class to manage service lookups.
 * 
 * @author miethaner
 */
public class ServiceManager {

    private static ServiceManager rm;

    /**
     * Returns the singleton instance of the service manager.
     * 
     * @return the resource manager
     */
    public static ServiceManager getInstance() {
        if (rm == null) {
            rm = new ServiceManager();
        }

        return rm;
    }

    private Map<String, Supplier<?>> services;

    private ServiceManager() {
        services = new ConcurrentHashMap<>();
    }

    /**
     * Gets the service instance for the given service name.
     * 
     * @param name
     *            the service name
     * @return the service instance
     */
    @SuppressWarnings("unchecked")
    public <T> T getService(String name) {
        if (name == null || !services.containsKey(name)) {
            throw new RuntimeException(name + " serivce not found");
        }

        return (T) services.get(name).get();
    }

    /**
     * Sets the service supplier as value with the given service name as key.
     * 
     * @param name
     *            the service name as key
     * @param supplier
     *            the service supplier as value
     */
    public void setService(String name, Supplier<?> supplier) {
        if (name == null || supplier == null) {
            throw new RuntimeException("Unable to set " + name + " resource");
        }

        services.put(name, supplier);
    }
}
