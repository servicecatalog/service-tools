/*******************************************************************************
 *                                                                              
 *  Copyright FUJITSU LIMITED 2016                                           
 *                                                                                                                                 
 *  Creation Date: Aug 18, 2016                                                      
 *                                                                              
 *******************************************************************************/

package org.oscm.common.util;

import java.util.Set;
import java.util.TreeSet;

import org.oscm.common.interfaces.config.ServiceKey;
import org.oscm.common.interfaces.enums.Messages;
import org.oscm.common.interfaces.exceptions.ComponentException;
import org.oscm.common.interfaces.exceptions.SecurityException;
import org.oscm.common.interfaces.security.SecurityToken;

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
     * @param service
     *            the key of the calling service
     * @param token
     *            the security token
     * @throws ComponentException
     */
    public static void validatePermission(ServiceKey service,
            SecurityToken token) throws ComponentException {

        ServiceConfiguration config = ServiceConfiguration.getInstance();

        if (config.isServiceRestricted(service)) {

            if (token == null) {
                throw new SecurityException(Messages.NOT_AUTHENTICATED.error(),
                        Messages.NOT_AUTHENTICATED.message());
            }

            Set<String> configRoles = config.getRolesForService(service);
            Set<String> userRoles = token.getRoles();

            if (configRoles != null && userRoles != null) {
                Set<String> intersection = new TreeSet<>(configRoles);
                intersection.retainAll(userRoles);

                if (intersection.isEmpty()) {
                    throw new SecurityException(Messages.NOT_AUTHORIZED.error(),
                            Messages.NOT_AUTHORIZED.message());
                }
            } else {
                throw new SecurityException(Messages.NOT_AUTHORIZED.error(),
                        Messages.NOT_AUTHORIZED.message());
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
     * @throws ComponentException
     */
    public static void validateUserId(SecurityToken token, Long userId)
            throws ComponentException {
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
     * @throws ComponentException
     */
    public static void validateOrganizationId(SecurityToken token,
            Long organizationId) throws ComponentException {
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
     * @throws ComponentException
     */
    public static void validateTenantId(SecurityToken token, Long tenantId)
            throws ComponentException {
        if (token == null || token.getTenantId() == null
                || tenantId != null && !token.getTenantId().equals(tenantId)) {
            throw new SecurityException(null, ""); // TODO add error message
        }
    }

    private Security() {
    }
}
