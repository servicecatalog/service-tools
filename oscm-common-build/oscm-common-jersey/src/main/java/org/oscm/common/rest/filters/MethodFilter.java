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
import javax.ws.rs.Priorities;
import javax.ws.rs.container.ContainerRequestContext;
import javax.ws.rs.container.ContainerRequestFilter;
import javax.ws.rs.ext.Provider;

import org.oscm.common.interfaces.enums.Messages;
import org.oscm.common.interfaces.exceptions.InternalException;
import org.oscm.common.interfaces.exceptions.NotFoundException;
import org.oscm.common.interfaces.keys.ActivityKey;
import org.oscm.common.interfaces.keys.VersionKey;
import org.oscm.common.rest.ExceptionMapper;
import org.oscm.common.rest.interfaces.Activity;
import org.oscm.common.rest.interfaces.Versioned;

/**
 * @author miethaner
 *
 */
@Provider
@Activity
@Versioned
@Priority(Priorities.AUTHORIZATION)
public class MethodFilter implements ContainerRequestFilter {

    @Override
    public void filter(ContainerRequestContext request) throws IOException {

        ActivityKey activityKey = (ActivityKey) request
                .getProperty(ActivityFilter.PARAM_ACTIVITY);

        VersionKey versionKey = (VersionKey) request
                .getProperty(VersionFilter.PROPERTY_VERSION);

        if (activityKey == null || versionKey == null) {
            InternalException ie = new InternalException(Messages.ERROR, "");

            throw new ExceptionMapper().toWebException(ie);
        }

        if (versionKey.compareVersion(activityKey.getSince()) < 0
                || versionKey.compareVersion(activityKey.getUntil()) > 0) {
            NotFoundException nfe = new NotFoundException(
                    Messages.METHOD_VERSION);

            throw new ExceptionMapper().toWebException(nfe);
        }
    }
}
