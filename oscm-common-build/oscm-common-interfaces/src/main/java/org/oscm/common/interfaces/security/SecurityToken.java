/*******************************************************************************
 *                                                                              
 *  Copyright FUJITSU LIMITED 2016                                           
 *                                                                                                                                 
 *  Creation Date: Jul 13, 2016                                                      
 *                                                                              
 *******************************************************************************/

package org.oscm.common.interfaces.security;

import java.util.Map;
import java.util.Set;

/**
 * Interface for security tokens.
 * 
 * @author miethaner
 */
public interface SecurityToken {

    public static final String TYPE_NAME = "SecurityToken";
    public static final String PROPERTY_USER_ID = "user id";
    public static final String PROPERTY_ORGANIZATION_ID = "organization id";
    public static final String PROPERTY_TENANT_ID = "tenant id";
    public static final String PROPERTY_ROLES = "roles";

    public enum Restrictions {
        SUBJECT_ID, SUBJECT_RESOURCE;
    }

    /**
     * Gets the user id of the token owner.
     * 
     * @return the user id
     */
    public Long getUserId();

    /**
     * Gets the organization id of the token owner.
     * 
     * @return the organization id
     */
    public Long getOrganizationId();

    /**
     * Get the tenant id of the token owner.
     * 
     * @return the tenant id
     */
    public Long getTenantId();

    /**
     * Gets all restrictions that have been defined for the token.
     * 
     * @return the map of restrictions
     */
    public Map<String, String> getRestrictions();

    /**
     * Gets the roles of the token owner in an immutable set.
     * 
     * @return the set of roles
     */
    public Set<String> getRoles();
}
