/*******************************************************************************
 *                                                                              
 *  Copyright FUJITSU LIMITED 2016                                           
 *                                                                                                                                 
 *  Creation Date: Jun 22, 2016                                                      
 *                                                                              
 *******************************************************************************/

package org.oscm.common.interfaces.data;

import org.oscm.common.interfaces.security.SecurityToken;

/**
 * Base interface for all parameter interfaces.
 * 
 * @author miethaner
 */
public interface ParameterType extends Versionable {

    public static final String PROPERTY_ID = "id";
    public static final String PROPERTY_ETAG = "etag";
    public static final String PROPERTY_LIMIT = "limit";
    public static final String PROPERTY_OFFSET = "offset";
    public static final String PROPERTY_TOKEN = "token";

    /**
     * Gets the entity identifier from the parameters. Returns null if not set.
     * 
     * @return the entity ID or null
     */
    public Long getId();

    /**
     * Gets the ETag from the parameters. Returns null if not set.
     * 
     * @return the etag or null
     */
    public Long getETag();

    /**
     * Gets the limit for paging. Returns null if not set.
     * 
     * @return the limit or null
     */
    public Long getLimit();

    /**
     * Gets the offset for paging. Returns null if not set.
     * 
     * @return the offset or null
     */
    public Long getOffset();

    /**
     * Gets the security token for the logged in user. Returns null if the user
     * is not authenticated.
     * 
     * @return the security token or null
     */
    public SecurityToken getSecurityToken();
}
