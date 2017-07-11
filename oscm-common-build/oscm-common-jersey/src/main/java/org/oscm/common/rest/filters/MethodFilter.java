/*******************************************************************************
 *                                                                              
 *  Copyright FUJITSU LIMITED 2017                                           
 *                                                                                                                                 
 *  Creation Date: Jun 7, 2017                                                      
 *                                                                              
 *******************************************************************************/

package org.oscm.common.rest.filters;

import java.io.IOException;

import javax.annotation.Priority;
import javax.inject.Inject;
import javax.ws.rs.Priorities;
import javax.ws.rs.container.ContainerRequestContext;
import javax.ws.rs.container.ContainerRequestFilter;
import javax.ws.rs.ext.Provider;

import org.oscm.common.interfaces.data.Version;
import org.oscm.common.interfaces.enums.Messages;
import org.oscm.common.interfaces.exceptions.InternalException;
import org.oscm.common.interfaces.exceptions.NotFoundException;
import org.oscm.common.interfaces.keys.ActivityKey;
import org.oscm.common.rest.ServiceRequestContext;
import org.oscm.common.rest.interfaces.Activity;
import org.oscm.common.rest.interfaces.Versioned;
import org.oscm.common.rest.provider.ExceptionProvider;

/**
 * @author miethaner
 *
 */
@Provider
@Activity
@Versioned
@Priority(Priorities.AUTHORIZATION)
public class MethodFilter implements ContainerRequestFilter {

    @Inject
    private ServiceRequestContext context;

    public void setContext(ServiceRequestContext context) {
        this.context = context;
    }

    @Override
    public void filter(ContainerRequestContext request) throws IOException {

        ActivityKey activity = context.getActivity();

        Version version = context.getVersion();

        if (activity == null || version == null) {
            InternalException ie = new InternalException(Messages.ERROR, "");

            throw new ExceptionProvider().toWebException(ie);
        }

        if (version.compare(activity.getSince()) < 0
                || version.compare(activity.getUntil()) > 0) {
            NotFoundException nfe = new NotFoundException(
                    Messages.ERROR_METHOD_VERSION);

            throw new ExceptionProvider().toWebException(nfe);
        }
    }
}
