/*******************************************************************************
 *                                                                              
 *  Copyright FUJITSU LIMITED 2016                                           
 *                                                                                                                                 
 *  Creation Date: Aug 24, 2016                                                      
 *                                                                              
 *******************************************************************************/

package org.oscm.common.rest;

import java.util.Collections;
import java.util.HashSet;
import java.util.Set;

import org.oscm.common.interfaces.security.SecurityToken;

/**
 * Implementation for the security token.
 * 
 * @author miethaner
 */
public class Token implements SecurityToken {

    private Long userId;
    private Long organizationId;
    private Set<String> roles = new HashSet<>();

    @Override
    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }

    @Override
    public Long getOrganizationId() {
        return organizationId;
    }

    public void setOrganizationId(Long organizationId) {
        this.organizationId = organizationId;
    }

    @Override
    public Set<String> getRoles() {
        return Collections.unmodifiableSet(roles);
    }

    public void setRoles(Set<String> roles) {
        if (roles != null) {
            this.roles = roles;
        } else {
            this.roles = new HashSet<>();
        }
    }
}
