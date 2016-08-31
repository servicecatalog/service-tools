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
import javax.ws.rs.ext.Provider;

import org.oscm.common.interfaces.enums.Messages;
import org.oscm.common.interfaces.exceptions.SecurityException;
import org.oscm.common.interfaces.exceptions.TokenException;

/**
 * Request filter for checking the security context.
 * 
 * @author miethaner
 */
@Provider
public class SecurityFilter implements ContainerRequestFilter {

    /**
     * Check if the connection is secure and the user authenticated.
     */
    @Override
    public void filter(ContainerRequestContext request)
            throws WebApplicationException {

        if (!request.getSecurityContext().isSecure()) {
            SecurityException se = new SecurityException(
                    Messages.NOT_SECURE.error(), Messages.NOT_SECURE.message());

            throw new ExceptionMapper().toWebException(se);
        }

        if (request.getSecurityContext().getUserPrincipal() == null) {
            TokenException te = new TokenException(
                    Messages.NOT_AUTHENTICATED.error(),
                    Messages.NOT_AUTHENTICATED.message());

            throw new ExceptionMapper().toWebException(te);
        }
    }
}
