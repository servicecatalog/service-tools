/*******************************************************************************
 *                                                                              
 *  Copyright FUJITSU LIMITED 2016                                           
 *                                                                                                                                 
 *  Creation Date: Aug 24, 2016                                                      
 *                                                                              
 *******************************************************************************/

package org.oscm.common.interfaces.data;

import java.util.Arrays;
import java.util.Collections;
import java.util.HashSet;
import java.util.Set;
import java.util.UUID;

import com.google.gson.annotations.SerializedName;

/**
 * Implementation for the security token.
 * 
 * @author miethaner
 */
public class Token extends VersionedEntity {

    public static final String FIELD_USER_ID = "user_id";
    public static final String FIELD_ORGANIZATION_ID = "organization_id";
    public static final String FIELD_TENANT_ID = "tenant_id";
    public static final String FIELD_RESTRICTIONS = "restrictions";
    public static final String FIELD_ROLES = "roles";

    @SerializedName(FIELD_USER_ID)
    private UUID userId;

    @SerializedName(FIELD_ORGANIZATION_ID)
    private UUID organizationId;

    @SerializedName(FIELD_TENANT_ID)
    private UUID tenantId;

    @SerializedName(FIELD_RESTRICTIONS)
    private Set<String> restrictions;

    @SerializedName(FIELD_ROLES)
    private Set<String> roles;

    public UUID getUserId() {
        return userId;
    }

    public String getUserIdAsString() {
        if (userId != null) {
            return userId.toString();
        } else {
            return null;
        }
    }

    public void setUserId(UUID userId) {
        this.userId = userId;
    }

    public void setUserId(String userIdString) {
        if (userIdString != null) {
            try {
                userId = UUID.fromString(userIdString);
            } catch (IllegalArgumentException e) {
                userId = null;
            }
        } else {
            userId = null;
        }
    }

    public UUID getOrganizationId() {
        return organizationId;
    }

    public String getOrganizationIdAsString() {
        if (organizationId != null) {
            return organizationId.toString();
        } else {
            return null;
        }
    }

    public void setOrganizationId(UUID organizationId) {
        this.organizationId = organizationId;
    }

    public void setOrganizationId(String organizationIdString) {
        if (organizationIdString != null) {
            try {
                organizationId = UUID.fromString(organizationIdString);
            } catch (IllegalArgumentException e) {
                organizationId = null;
            }
        } else {
            organizationId = null;
        }
    }

    public UUID getTenantId() {
        return tenantId;
    }

    public String getTenantIdAsString() {
        if (tenantId != null) {
            return tenantId.toString();
        } else {
            return null;
        }
    }

    public void setTenantId(UUID tenantId) {
        this.tenantId = tenantId;
    }

    public void setTenantId(String tenantIdString) {
        if (tenantIdString != null) {
            try {
                tenantId = UUID.fromString(tenantIdString);
            } catch (IllegalArgumentException e) {
                tenantId = null;
            }
        } else {
            tenantId = null;
        }
    }

    public Set<String> getRestrictions() {
        if (restrictions != null) {
            return Collections.unmodifiableSet(restrictions);
        } else {
            return Collections.emptySet();
        }
    }

    public String[] getRestrictionsAsArray() {
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

    public Set<String> getRoles() {
        if (roles != null) {
            return Collections.unmodifiableSet(roles);
        } else {
            return Collections.emptySet();
        }
    }

    public String[] getRolesAsArray() {
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
