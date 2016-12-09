/*******************************************************************************
 *                                                                              
 *  Copyright FUJITSU LIMITED 2016                                           
 *                                                                                                                                 
 *  Creation Date: Dec 6, 2016                                                      
 *                                                                              
 *******************************************************************************/

package org.oscm.common.util;

import java.lang.reflect.InvocationTargetException;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * Singleton class to manage stateless resources of modules via interfaces.
 * 
 * @author miethaner
 */
public class ResourceManager {

    private static ResourceManager rm;

    /**
     * Returns the singleton instance of the resource manager.
     * 
     * @return the resource manager
     */
    public static ResourceManager getInstance() {
        if (rm == null) {
            rm = new ResourceManager();
        }

        return rm;
    }

    private Map<Class<?>, Class<?>> resources;

    private ResourceManager() {
        resources = new ConcurrentHashMap<>();
    }

    /**
     * Gets the resource instance for the given resource type.
     * 
     * @param type
     *            the resource type
     * @return the resource instance
     */
    @SuppressWarnings("unchecked")
    public <T> T getResource(Class<T> type) {
        if (type != null && resources.containsKey(type)) {
            try {
                return (T) resources.get(type).getConstructor().newInstance();
            } catch (InstantiationException | IllegalAccessException
                    | IllegalArgumentException | InvocationTargetException
                    | NoSuchMethodException | SecurityException e) {
                throw new RuntimeException(
                        "Unable to invoke default constructor of resource "
                                + type.getSimpleName());
            }
        } else {
            String name = type != null ? type.getSimpleName() : "Unknown";
            throw new RuntimeException(name + " resource not found");
        }
    }

    /**
     * Sets the resource class as value with the given resource type as key.
     * 
     * @param type
     *            the resource interface as key
     * @param impl
     *            the resource implemented class as value
     */
    public <T, I extends T> void setResource(Class<T> type, Class<I> impl) {
        if (type != null && impl != null) {
            resources.put(type, impl);
        } else {
            String name = type != null ? type.getSimpleName() : "unknown";
            throw new RuntimeException("Unable to set " + name + " resource");
        }
    }
}
