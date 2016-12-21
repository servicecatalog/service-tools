/*******************************************************************************
 *                                                                              
 *  Copyright FUJITSU LIMITED 2016                                           
 *                                                                                                                                 
 *  Creation Date: May 9, 2016                                                      
 *                                                                              
 *******************************************************************************/

package org.oscm.common.rest;

import java.lang.annotation.Annotation;
import java.lang.reflect.Method;

import javax.ws.rs.WebApplicationException;
import javax.ws.rs.container.ContainerRequestContext;
import javax.ws.rs.container.ContainerRequestFilter;
import javax.ws.rs.container.ResourceInfo;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MultivaluedMap;
import javax.ws.rs.ext.Provider;

import org.oscm.common.interfaces.config.VersionKey;
import org.oscm.common.interfaces.enums.Messages;
import org.oscm.common.interfaces.exceptions.NotFoundException;
import org.oscm.common.util.ServiceConfiguration;

/**
 * Request filter for validating the requested version and comparing with
 * endpoint annotations (Since and Until).
 * 
 * @author miethaner
 */
@Provider
public class VersionFilter implements ContainerRequestFilter {

    public static final String PATTERN_VERSION = "v[0-9]+";
    public static final int OFFSET_VERSION = 1;

    @Context
    private ResourceInfo resourceInfo;

    public void setResourceInfo(ResourceInfo resourceInfo) {
        this.resourceInfo = resourceInfo;
    }

    @Override
    public void filter(ContainerRequestContext request)
            throws WebApplicationException {

        MultivaluedMap<String, String> params = request.getUriInfo()
                .getPathParameters();

        if (params != null
                && params.containsKey(RequestParameters.PARAM_VERSION)
                && !params.get(RequestParameters.PARAM_VERSION).isEmpty()) {

            String version = params.get(RequestParameters.PARAM_VERSION).get(0);

            VersionKey versionKey = validateVersion(version);

            Method method = resourceInfo.getResourceMethod();

            if (method.isAnnotationPresent(Since.class)) {

                Annotation annotation = method.getAnnotation(Since.class);
                Since since = (Since) annotation;

                if (versionKey.compareVersion(since.major(), since.minor(),
                        since.fix()) < 0) {
                    NotFoundException nfe = new NotFoundException(
                            Messages.METHOD_VERSION);

                    throw new ExceptionMapper().toWebException(nfe);
                }
            }
            if (method.isAnnotationPresent(Until.class)) {

                Annotation annotation = method.getAnnotation(Until.class);
                Until until = (Until) annotation;

                if (versionKey.compareVersion(until.major(), until.minor(),
                        until.fix()) >= 0) {
                    NotFoundException nfe = new NotFoundException(
                            Messages.METHOD_VERSION);

                    throw new ExceptionMapper().toWebException(nfe);
                }
            }

            request.setProperty(RequestParameters.PARAM_VERSION, versionKey);

        } else {
            NotFoundException nfe = new NotFoundException(
                    Messages.INVALID_VERSION);

            throw new ExceptionMapper().toWebException(nfe);
        }
    }

    /**
     * Validates the version string and compares it with the existing version
     * numbers. Throws a NotFoundException if not valid.
     * 
     * @param version
     *            the version string
     * @return the corresponding version key
     * @throws WebApplicationException
     */
    private VersionKey validateVersion(String version)
            throws WebApplicationException {

        if (version == null) {
            NotFoundException nfe = new NotFoundException(
                    Messages.INVALID_VERSION);

            throw new ExceptionMapper().toWebException(nfe);
        }

        if (!version.matches(PATTERN_VERSION)) {
            NotFoundException nfe = new NotFoundException(
                    Messages.INVALID_VERSION);

            throw new ExceptionMapper().toWebException(nfe);
        }

        int vnr = Integer.parseInt(version.substring(OFFSET_VERSION));

        VersionKey versionKey = null;
        for (VersionKey key : ServiceConfiguration.getInstance()
                .getVersions()) {
            if (key.getCompiledVersion() == vnr) {
                versionKey = key;
                break;
            }
        }

        if (versionKey == null) {
            NotFoundException nfe = new NotFoundException(
                    Messages.INVALID_VERSION);

            throw new ExceptionMapper().toWebException(nfe);
        }

        return versionKey;
    }

}
