/*******************************************************************************
 *                                                                              
 *  Copyright FUJITSU LIMITED 2016                                           
 *                                                                                                                                 
 *  Creation Date: May 2, 2016                                                      
 *                                                                              
 *******************************************************************************/

package org.oscm.common.rest;

import javax.ws.rs.WebApplicationException;
import javax.ws.rs.container.ContainerRequestContext;
import javax.ws.rs.container.ContainerRequestFilter;
import javax.ws.rs.core.HttpHeaders;
import javax.ws.rs.ext.Provider;

import org.oscm.common.interfaces.enums.Messages;
import org.oscm.common.interfaces.exceptions.SecurityException;
import org.oscm.common.interfaces.exceptions.ServiceException;
import org.oscm.common.interfaces.exceptions.TokenException;

/**
 * Request filter for checking the security context.
 * 
 * @author miethaner
 */
@Provider
public class SecurityFilter implements ContainerRequestFilter {

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

        if (!header.startsWith(RequestParameters.AUTHORIZATION_PREFIX)) {
            TokenException te = new TokenException(Messages.NOT_AUTHENTICATED);

            throw new ExceptionMapper().toWebException(te);
        }

        String tokenString = header
                .substring(RequestParameters.AUTHORIZATION_PREFIX.length());

        Token token;
        try {
            token = TokenManager.getInstance()
                    .decryptAndVerifyToken(tokenString);
        } catch (ServiceException e) {
            throw new ExceptionMapper().toWebException(e);
        }

        request.setProperty(RequestParameters.PARAM_TOKEN, token);

    }
}
