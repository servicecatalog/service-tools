/*******************************************************************************
 *                                                                              
 *  Copyright FUJITSU LIMITED 2017                                           
 *                                                                                                                                 
 *  Creation Date: Jun 2, 2017                                                      
 *                                                                              
 *******************************************************************************/

package org.oscm.common.rest.filters;

import java.io.IOException;
import java.lang.reflect.Method;

import javax.annotation.Priority;
import javax.inject.Inject;
import javax.ws.rs.Priorities;
import javax.ws.rs.container.ContainerRequestContext;
import javax.ws.rs.container.ContainerRequestFilter;
import javax.ws.rs.container.ResourceInfo;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MultivaluedMap;
import javax.ws.rs.ext.Provider;

import org.oscm.common.interfaces.enums.Messages;
import org.oscm.common.interfaces.exceptions.InternalException;
import org.oscm.common.interfaces.exceptions.NotFoundException;
import org.oscm.common.interfaces.keys.ActivityKey;
import org.oscm.common.interfaces.keys.ActivityKey.Type;
import org.oscm.common.rest.ServiceRequestContext;
import org.oscm.common.rest.interfaces.Activity;
import org.oscm.common.rest.provider.ExceptionProvider;
import org.oscm.common.util.ConfigurationManager;

/**
 * @author miethaner
 *
 */
@Provider
@Activity
@Priority(Priorities.AUTHENTICATION)
public class ActivityFilter implements ContainerRequestFilter {

    public static final String PARAM_ACTIVITY = "activity";

    @Context
    private ResourceInfo resourceInfo;

    public void setResourceInfo(ResourceInfo resourceInfo) {
        this.resourceInfo = resourceInfo;
    }

    @Inject
    private ServiceRequestContext context;

    public void setContext(ServiceRequestContext context) {
        this.context = context;
    }

    @Override
    public void filter(ContainerRequestContext request) throws IOException {

        MultivaluedMap<String, String> params = request.getUriInfo()
                .getPathParameters();

        Method method = resourceInfo.getResourceMethod();

        if (method == null || params == null
                || !params.containsKey(PARAM_ACTIVITY)
                || params.get(PARAM_ACTIVITY).isEmpty()) {
            NotFoundException nfe = new NotFoundException(
                    Messages.ERROR_INVALID_ACTIVITY);

            throw new ExceptionProvider().toWebException(nfe);
        }

        String activityString = params.get(PARAM_ACTIVITY).get(0);

        Activity annotation = method.getAnnotation(Activity.class);

        if (annotation == null || annotation.value() == null) {
            InternalException ie = new InternalException(Messages.ERROR, "");

            throw new ExceptionProvider().toWebException(ie);
        }

        ActivityKey activity = validateActivity(activityString,
                annotation.value());

        context.setActivity(activity);
    }

    private ActivityKey validateActivity(String actvityString, Type type) {

        if (actvityString == null) {
            NotFoundException nfe = new NotFoundException(
                    Messages.ERROR_INVALID_ACTIVITY);

            throw new ExceptionProvider().toWebException(nfe);
        }

        ConfigurationManager cm = ConfigurationManager.getInstance();

        ActivityKey activity = cm.getActivityForName(actvityString);

        if (activity == null || activity.getType() != type
                || !activity.getApplication().equals(cm.getSelf())) {
            NotFoundException nfe = new NotFoundException(
                    Messages.ERROR_INVALID_ACTIVITY);

            throw new ExceptionProvider().toWebException(nfe);
        }

        return activity;
    }
}
