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

    private Security() {
    }
}
