/*******************************************************************************
 *                                                                              
 *  Copyright FUJITSU LIMITED 2016                                           
 *                                                                                                                                 
 *  Creation Date: Jul 13, 2016                                                      
 *                                                                              
 *******************************************************************************/

package org.oscm.common.interfaces.security;

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
    public static final String PROPERTY_ROLES = "roles";

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
     * Gets the roles of the token owner in an immutable set.
     * 
     * @return the set of roles
     */
    public Set<String> getRoles();
}
