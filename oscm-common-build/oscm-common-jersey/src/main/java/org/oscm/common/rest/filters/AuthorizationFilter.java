/*******************************************************************************
 *                                                                              
 *  Copyright FUJITSU LIMITED 2017                                           
 *                                                                                                                                 
 *  Creation Date: Jun 7, 2017                                                      
 *                                                                              
 *******************************************************************************/

package org.oscm.common.rest.filters;

import java.io.IOException;
import java.util.Set;
import java.util.TreeSet;

import javax.annotation.Priority;
import javax.ws.rs.Priorities;
import javax.ws.rs.container.ContainerRequestContext;
import javax.ws.rs.container.ContainerRequestFilter;
import javax.ws.rs.ext.Provider;

import org.oscm.common.interfaces.data.Token;
import org.oscm.common.interfaces.enums.Messages;
import org.oscm.common.interfaces.exceptions.InternalException;
import org.oscm.common.interfaces.exceptions.SecurityException;
import org.oscm.common.interfaces.keys.ActivityKey;
import org.oscm.common.rest.ExceptionMapper;
import org.oscm.common.rest.interfaces.Activity;
import org.oscm.common.rest.interfaces.Secure;
import org.oscm.common.util.ConfigurationManager;

/**
 * @author miethaner
 *
 */
@Provider
@Activity
@Secure
@Priority(Priorities.AUTHORIZATION)
public class AuthorizationFilter implements ContainerRequestFilter {

    @Override
    public void filter(ContainerRequestContext request) throws IOException {

        ActivityKey activityKey = (ActivityKey) request
                .getProperty(ActivityFilter.PROPERTY_ACTIVITY);

        Token token = (Token) request
                .getProperty(AuthenticationFilter.PROPERTY_TOKEN);

        if (activityKey == null || token == null) {
            InternalException ie = new InternalException(Messages.ERROR, "");

            throw new ExceptionMapper().toWebException(ie);
        }

        ConfigurationManager config = ConfigurationManager.getInstance();

        Set<String> configRoles = config.getRolesForActivity(activityKey);
        Set<String> userRoles = token.getRoles();

        if (configRoles != null && userRoles != null) {
            Set<String> intersection = new TreeSet<>(configRoles);
            intersection.retainAll(userRoles);

            if (intersection.isEmpty()) {
                SecurityException se = new SecurityException(
                        Messages.NOT_AUTHORIZED);

                throw new ExceptionMapper().toWebException(se);
            }
        } else {
            SecurityException se = new SecurityException(
                    Messages.NOT_AUTHORIZED);

            throw new ExceptionMapper().toWebException(se);
        }
    }

}
