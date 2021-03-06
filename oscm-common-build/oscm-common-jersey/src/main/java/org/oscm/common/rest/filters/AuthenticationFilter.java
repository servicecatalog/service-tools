/*******************************************************************************
 *                                                                              
 *  Copyright FUJITSU LIMITED 2016                                           
 *                                                                                                                                 
 *  Creation Date: May 2, 2016                                                      
 *                                                                              
 *******************************************************************************/

package org.oscm.common.rest.filters;

import javax.annotation.Priority;
import javax.inject.Inject;
import javax.ws.rs.Priorities;
import javax.ws.rs.WebApplicationException;
import javax.ws.rs.container.ContainerRequestContext;
import javax.ws.rs.container.ContainerRequestFilter;
import javax.ws.rs.core.HttpHeaders;
import javax.ws.rs.ext.Provider;

import org.oscm.common.interfaces.data.Token;
import org.oscm.common.interfaces.enums.Messages;
import org.oscm.common.interfaces.exceptions.SecurityException;
import org.oscm.common.interfaces.exceptions.ServiceException;
import org.oscm.common.interfaces.exceptions.TokenException;
import org.oscm.common.rest.ServiceRequestContext;
import org.oscm.common.rest.TokenManager;
import org.oscm.common.rest.interfaces.Secure;
import org.oscm.common.rest.provider.ExceptionProvider;

/**
 * Request filter for checking the security and comparing with endpoint
 * annotations (Secure).
 * 
 * @author miethaner
 */
@Provider
@Secure
@Priority(Priorities.AUTHENTICATION)
public class AuthenticationFilter implements ContainerRequestFilter {

    @Inject
    private ServiceRequestContext context;

    public void setContext(ServiceRequestContext context) {
        this.context = context;
    }

    /**
     * Check if the connection is secure and process the token.
     */
    @Override
    public void filter(ContainerRequestContext request)
            throws WebApplicationException {

        if (!request.getSecurityContext().isSecure()) {
            SecurityException se = new SecurityException(
                    Messages.ERROR_NOT_SECURE);

            throw new ExceptionProvider().toWebException(se);
        }

        String header = request.getHeaderString(HttpHeaders.AUTHORIZATION);

        if (!header.startsWith(TokenManager.AUTHORIZATION_PREFIX)) {
            TokenException te = new TokenException(
                    Messages.ERROR_NOT_AUTHENTICATED);

            throw new ExceptionProvider().toWebException(te);
        }

        String tokenString = header
                .substring(TokenManager.AUTHORIZATION_PREFIX.length());

        Token token;
        try {
            token = TokenManager.getInstance()
                    .decodeAndVerifyToken(tokenString);
        } catch (ServiceException e) {
            throw new ExceptionProvider().toWebException(e);
        }

        context.setToken(token);
    }
}
