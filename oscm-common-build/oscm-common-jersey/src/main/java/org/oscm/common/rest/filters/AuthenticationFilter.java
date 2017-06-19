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
import org.oscm.common.rest.RestContext;
import org.oscm.common.rest.TokenManager;
import org.oscm.common.rest.interfaces.Secure;
import org.oscm.common.rest.provider.ExceptionMapper;

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

    public static final String AUTHORIZATION_PREFIX = "Bearer ";

    @Inject
    private RestContext context;

    public void setContext(RestContext context) {
        this.context = context;
    }

    /**
     * Check if the connection is secure and process the token.
     */
    @Override
    public void filter(ContainerRequestContext request)
            throws WebApplicationException {

        if (!request.getSecurityContext().isSecure()) {
            SecurityException se = new SecurityException(Messages.NOT_SECURE);

            throw new ExceptionMapper().toWebException(se);
        }

        String header = request.getHeaderString(HttpHeaders.AUTHORIZATION);

        if (!header.startsWith(AUTHORIZATION_PREFIX)) {
            TokenException te = new TokenException(Messages.NOT_AUTHENTICATED);

            throw new ExceptionMapper().toWebException(te);
        }

        String tokenString = header.substring(AUTHORIZATION_PREFIX.length());

        Token token;
        try {
            token = TokenManager.getInstance()
                    .decryptAndVerifyToken(tokenString);
        } catch (ServiceException e) {
            throw new ExceptionMapper().toWebException(e);
        }

        context.setToken(token);
    }
}
