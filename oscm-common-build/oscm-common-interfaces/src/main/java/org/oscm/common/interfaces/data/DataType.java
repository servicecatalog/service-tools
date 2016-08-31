/*******************************************************************************
 *                                                                              
 *  Copyright FUJITSU LIMITED 2016                                           
 *                                                                                                                                 
 *  Creation Date: Jun 15, 2016                                                      
 *                                                                              
 *******************************************************************************/

package org.oscm.common.interfaces.data;

import org.oscm.common.interfaces.enums.Operation;

/**
 * Base interface for all data interfaces.
 * 
 * @author miethaner
 */
public interface DataType {

    public static final String PROPERTY_ID = "id";
    public static final String PROPERTY_ETAG = "etag";
    public static final String PROPERTY_LAST_OPERATION = "last operation";

    /**
     * Gets the entity identifier. Returns null if not set.
     * 
     * @return the entity ID or null
     */
    public Long getId();

    /**
     * Gets the ETag of the current instance of this data type. Returns null if
     * not set.
     * 
     * @return the etag or null
     */
    public Long getETag();

    /**
     * Gets the last operation executed with this data. Returns null if not set.
     * 
     * @return the last operation or null
     */
    public Operation getLastOperation();
}
