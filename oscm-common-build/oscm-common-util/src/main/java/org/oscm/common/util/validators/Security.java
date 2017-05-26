/*******************************************************************************
 *                                                                              
 *  Copyright FUJITSU LIMITED 2016                                           
 *                                                                                                                                 
 *  Creation Date: Aug 18, 2016                                                      
 *                                                                              
 *******************************************************************************/

package org.oscm.common.util.validators;

import java.util.Set;
import java.util.TreeSet;

import org.oscm.common.interfaces.config.ResourceKey;
import org.oscm.common.interfaces.enums.Messages;
import org.oscm.common.interfaces.exceptions.SecurityException;
import org.oscm.common.interfaces.exceptions.ServiceException;
import org.oscm.common.interfaces.security.SecurityToken;
import org.oscm.common.interfaces.security.SecurityToken.Restrictions;
import org.oscm.common.util.ServiceConfiguration;

/**
 * Common class for validating permissions and security tokens.
 * 
 * @author miethaner
 */
public class Security {

    /**
     * Validates the permissions with given security token through checking the
     * configuration for restrictions and roles.
     * 
     * @param resource
     *            the key of the calling resource
     * @param token
     *            the security token
     * @throws ServiceException
     */
    public static void validatePermission(ResourceKey resource,
            SecurityToken token) throws ServiceException {

        ServiceConfiguration config = ServiceConfiguration.getInstance();

        if (config.isResourceRestricted(resource)) {

            if (token == null) {
                throw new SecurityException(Messages.NOT_AUTHENTICATED);
            }

            Set<String> configRoles = config.getRolesForResource(resource);
            Set<String> userRoles = token.getRoles();

            if (configRoles != null && userRoles != null) {
                Set<String> intersection = new TreeSet<>(configRoles);
                intersection.retainAll(userRoles);

                if (intersection.isEmpty()) {
                    throw new SecurityException(Messages.NOT_AUTHORIZED);
                }
            } else {
                throw new SecurityException(Messages.NOT_AUTHORIZED);
            }
        }
    }

    /**
     * Validates the user id from the given token with the given user id.
     * 
     * @param token
     *            the security token
     * @param userId
     *            the user id
     * @throws ServiceException
     */
    public static void validateUserId(SecurityToken token, Long userId)
            throws ServiceException {
        if (token == null || token.getUserId() == null
                || userId != null && !token.getUserId().equals(userId)) {
            throw new SecurityException(null, ""); // TODO add error message
        }
    }

    /**
     * Validates the organization id from the given token with the given
     * organization id.
     * 
     * @param token
     *            the security token
     * @param organizationId
     *            the organization id
     * @throws ServiceException
     */
    public static void validateOrganizationId(SecurityToken token,
            Long organizationId) throws ServiceException {
        if (token == null || token.getOrganizationId() == null
                || organizationId != null
                        && !token.getOrganizationId().equals(organizationId)) {
            throw new SecurityException(null, ""); // TODO add error message
        }
    }

    /**
     * Validates the tenant id from the given token with the given tenant id.
     * 
     * @param token
     *            the security token
     * @param tenantId
     *            the tenant id
     * @throws ServiceException
     */
    public static void validateTenantId(SecurityToken token, Long tenantId)
            throws ServiceException {
        if (token == null || token.getTenantId() == null
                || tenantId != null && !token.getTenantId().equals(tenantId)) {
            throw new SecurityException(null, ""); // TODO add error message
        }
    }

    /**
     * Validates the given value with the one of the given restriction from the
     * given token.
     * 
     * @param token
     *            the security token
     * @param restriction
     *            the restriction key
     * @param value
     *            the restriction value
     * @throws ServiceException
     */
    public static void validateRestriction(SecurityToken token,
            Restrictions restriction, String value) throws ServiceException {
        if (token == null || restriction == null
                || token.getRestrictions() == null
                || token.getRestrictions().get(restriction.name()) == null
                || !token.getRestrictions().get(restriction.name())
                        .equals(value)) {
            throw new SecurityException(null, ""); // TODO add error message
        }
    }

    private Security() {
    }
}
