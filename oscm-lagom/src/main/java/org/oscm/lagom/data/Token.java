/*******************************************************************************
 *
 *  Copyright FUJITSU LIMITED 2016                                           
 *
 *  Creation Date: Aug 24, 2016                                                      
 *
 *******************************************************************************/

package org.oscm.common.interfaces.data;

import com.google.gson.annotations.SerializedName;

import java.util.*;

/**
 * Entity class for the security token.
 *
 * @author miethaner
 */
public class Token {

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

    /**
     * Gets the user id. Returns null if not set.
     *
     * @return the user id
     */
    public UUID getUserId() {
        return userId;
    }

    /**
     * Gets the user id as string. Returns null if not set.
     *
     * @return the id string
     */
    public String getUserIdAsString() {
        if (userId != null) {
            return userId.toString();
        } else {
            return null;
        }
    }

    /**
     * Sets the user id.
     *
     * @param userId the user id
     */
    public void setUserId(UUID userId) {
        this.userId = userId;
    }

    /**
     * Sets the user id as string.
     *
     * @param userIdString the id string
     */
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

    /**
     * Gets the organization id. Returns null if not set.
     *
     * @return the organization id.
     */
    public UUID getOrganizationId() {
        return organizationId;
    }

    /**
     * Gets the organization id as string. Returns null if not set.
     *
     * @return the id string
     */
    public String getOrganizationIdAsString() {
        if (organizationId != null) {
            return organizationId.toString();
        } else {
            return null;
        }
    }

    /**
     * Sets the organization id.
     *
     * @param organizationId the organization id
     */
    public void setOrganizationId(UUID organizationId) {
        this.organizationId = organizationId;
    }

    /**
     * Sets the organization id as string.
     *
     * @param organizationIdString
     */
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

    /**
     * Gets the tenant id. Returns null if not set.
     *
     * @return the tenant id
     */
    public UUID getTenantId() {
        return tenantId;
    }

    /**
     * Gets the tenant id as string. Returns null if not set.
     *
     * @return the id string
     */
    public String getTenantIdAsString() {
        if (tenantId != null) {
            return tenantId.toString();
        } else {
            return null;
        }
    }

    /**
     * Sets the tenant id.
     *
     * @param tenantId the tenant id
     */
    public void setTenantId(UUID tenantId) {
        this.tenantId = tenantId;
    }

    /**
     * Sets the tenant id as string.
     *
     * @param tenantIdString the id string
     */
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

    /**
     * Gets the set of restrictions. Returns null if not set.
     *
     * @return the set of restrictions
     */
    public Set<String> getRestrictions() {
        if (restrictions != null) {
            return Collections.unmodifiableSet(restrictions);
        } else {
            return Collections.emptySet();
        }
    }

    /**
     * Gets the array of restrictions. Returns null if not set.
     *
     * @return the array of restrictions
     */
    public String[] getRestrictionsAsArray() {
        if (restrictions != null) {
            return restrictions.toArray(new String[] {});
        } else {
            return new String[] {};
        }
    }

    /**
     * Sets the set of restrictions.
     *
     * @param restrictions the set of restrictions
     */
    public void setRestrictions(Set<String> restrictions) {
        this.restrictions = restrictions;
    }

    /**
     * Set the array of restrictions.
     *
     * @param restrictionsArray the array of restrictions
     */
    public void setRestrictions(String[] restrictionsArray) {
        if (restrictionsArray != null) {
            restrictions = new HashSet<>(Arrays.asList(restrictionsArray));
        } else {
            restrictions = null;
        }
    }

    /**
     * Gets the set of roles. Returns null if not set.
     *
     * @return the set of roles
     */
    public Set<String> getRoles() {
        if (roles != null) {
            return Collections.unmodifiableSet(roles);
        } else {
            return Collections.emptySet();
        }
    }

    /**
     * Gets the array of roles. Returns null if not set.
     *
     * @return the array of roles
     */
    public String[] getRolesAsArray() {
        if (roles != null) {
            return roles.toArray(new String[] {});
        } else {
            return new String[] {};
        }
    }

    /**
     * Sets the set of roles.
     *
     * @param roles the set of roles
     */
    public void setRoles(Set<String> roles) {
        this.roles = roles;
    }

    /**
     * Sets the array of roles.
     *
     * @param rolesArray the array of roles
     */
    public void setRoles(String[] rolesArray) {
        if (rolesArray != null) {
            roles = new HashSet<>(Arrays.asList(rolesArray));
        } else {
            roles = null;
        }
    }
}
