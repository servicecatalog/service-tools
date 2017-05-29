/*******************************************************************************
 *                                                                              
 *  Copyright FUJITSU LIMITED 2016                                           
 *                                                                                                                                 
 *  Creation Date: Aug 24, 2016                                                      
 *                                                                              
 *******************************************************************************/

package org.oscm.common.rest;

import java.util.Arrays;
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

    public static final String FIELD_USER_ID = "user_id";
    public static final String FIELD_ORGANIZATION_ID = "organization_id";
    public static final String FIELD_TENANT_ID = "tenant_id";
    public static final String FIELD_RESTRICTIONS = "restrictions";
    public static final String FIELD_ROLES = "roles";

    private Long userId;
    private Long organizationId;
    private Long tenantId;
    private Set<String> restrictions;
    private Set<String> roles;

    @Override
    public Long getUserId() {
        return userId;
    }

    public String getUserIdString() {
        if (userId != null) {
            return userId.toString();
        } else {
            return null;
        }
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }

    public void setUserId(String userIdString) {
        if (userIdString != null) {
            try {
                userId = Long.valueOf(userIdString);
            } catch (NumberFormatException e) {
                userId = null;
            }
        } else {
            userId = null;
        }
    }

    @Override
    public Long getOrganizationId() {
        return organizationId;
    }

    public String getOrganizationIdString() {
        if (organizationId != null) {
            return organizationId.toString();
        } else {
            return null;
        }
    }

    public void setOrganizationId(Long organizationId) {
        this.organizationId = organizationId;
    }

    public void setOrganizationId(String organizationIdString) {
        if (organizationIdString != null) {
            try {
                organizationId = Long.valueOf(organizationIdString);
            } catch (NumberFormatException e) {
                organizationId = null;
            }
        } else {
            organizationId = null;
        }
    }

    @Override
    public Long getTenantId() {
        return tenantId;
    }

    public String getTenantIdString() {
        if (tenantId != null) {
            return tenantId.toString();
        } else {
            return null;
        }
    }

    public void setTenantId(Long tenantId) {
        this.tenantId = tenantId;
    }

    public void setTenantId(String tenantIdString) {
        if (tenantIdString != null) {
            try {
                tenantId = Long.valueOf(tenantIdString);
            } catch (NumberFormatException e) {
                tenantId = null;
            }
        } else {
            tenantId = null;
        }
    }

    @Override
    public Set<String> getRestrictions() {
        if (restrictions != null) {
            return Collections.unmodifiableSet(restrictions);
        } else {
            return Collections.emptySet();
        }
    }

    public String[] getRestrictionsArray() {
        if (restrictions != null) {
            return restrictions.toArray(new String[] {});
        } else {
            return new String[] {};
        }
    }

    public void setRestrictions(Set<String> restrictions) {
        this.restrictions = restrictions;
    }

    public void setRestrictions(String[] restrictionsArray) {
        if (restrictionsArray != null) {
            restrictions = new HashSet<>(Arrays.asList(restrictionsArray));
        } else {
            restrictions = null;
        }
    }

    @Override
    public Set<String> getRoles() {
        if (roles != null) {
            return Collections.unmodifiableSet(roles);
        } else {
            return Collections.emptySet();
        }
    }

    public String[] getRolesArray() {
        if (roles != null) {
            return roles.toArray(new String[] {});
        } else {
            return new String[] {};
        }
    }

    public void setRoles(Set<String> roles) {
        this.roles = roles;
    }

    public void setRoles(String[] rolesArray) {
        if (rolesArray != null) {
            roles = new HashSet<>(Arrays.asList(rolesArray));
        } else {
            roles = null;
        }
    }
}
