/*******************************************************************************
 *                                                                              
 *  Copyright FUJITSU LIMITED 2016                                           
 *                                                                                                                                 
 *  Creation Date: May 9, 2016                                                      
 *                                                                              
 *******************************************************************************/

package org.oscm.common.rest.filters;

import javax.annotation.Priority;
import javax.inject.Inject;
import javax.ws.rs.Priorities;
import javax.ws.rs.WebApplicationException;
import javax.ws.rs.container.ContainerRequestContext;
import javax.ws.rs.container.ContainerRequestFilter;
import javax.ws.rs.core.MultivaluedMap;
import javax.ws.rs.ext.Provider;

import org.oscm.common.interfaces.data.Version;
import org.oscm.common.interfaces.enums.Messages;
import org.oscm.common.interfaces.exceptions.NotFoundException;
import org.oscm.common.rest.ServiceRequestContext;
import org.oscm.common.rest.interfaces.Versioned;
import org.oscm.common.rest.provider.ExceptionProvider;
import org.oscm.common.util.ConfigurationManager;

/**
 * Request filter for validating the requested version and comparing with
 * endpoint annotations (Since and Until).
 * 
 * @author miethaner
 */
@Provider
@Versioned
@Priority(Priorities.AUTHENTICATION)
public class VersionFilter implements ContainerRequestFilter {

    public static final String PARAM_VERSION = "version";
    public static final String PATTERN_VERSION = "v[0-9]+";
    public static final int OFFSET_VERSION = 1;

    @Inject
    private ServiceRequestContext context;

    public void setContext(ServiceRequestContext context) {
        this.context = context;
    }

    @Override
    public void filter(ContainerRequestContext request)
            throws WebApplicationException {

        MultivaluedMap<String, String> params = request.getUriInfo()
                .getPathParameters();

        if (params == null || !params.containsKey(PARAM_VERSION)
                || params.get(PARAM_VERSION).isEmpty()) {
            NotFoundException nfe = new NotFoundException(
                    Messages.ERROR_INVALID_VERSION);

            throw new ExceptionProvider().toWebException(nfe);
        }

        String versionString = params.get(PARAM_VERSION).get(0);

        Version version = validateVersion(versionString);

        context.setVersion(version);
    }

    /**
     * Validates the version string and compares it with the existing version
     * numbers. Throws a NotFoundException if not valid.
     * 
     * @param versionString
     *            the version string
     * @return the corresponding version key
     * @throws WebApplicationException
     */
    private Version validateVersion(String versionString)
            throws WebApplicationException {

        if (versionString == null) {
            NotFoundException nfe = new NotFoundException(
                    Messages.ERROR_INVALID_VERSION);

            throw new ExceptionProvider().toWebException(nfe);
        }

        if (!versionString.matches(PATTERN_VERSION)) {
            NotFoundException nfe = new NotFoundException(
                    Messages.ERROR_INVALID_VERSION);

            throw new ExceptionProvider().toWebException(nfe);
        }

        int vnr;
        try {
            vnr = Integer.parseInt(versionString.substring(OFFSET_VERSION));
        } catch (NumberFormatException e) {
            NotFoundException nfe = new NotFoundException(
                    Messages.ERROR_INVALID_VERSION);

            throw new ExceptionProvider().toWebException(nfe);
        }

        Version version = new Version(vnr);

        ConfigurationManager cm = ConfigurationManager.getInstance();

        if (version.compare(cm.getCompatibleVersion()) < 0
                || version.compare(cm.getCurrentVersion()) > 0) {
            NotFoundException nfe = new NotFoundException(
                    Messages.ERROR_INVALID_VERSION);

            throw new ExceptionProvider().toWebException(nfe);
        }

        return version;
    }

}
