/*******************************************************************************
 *                                                                              
 *  Copyright FUJITSU LIMITED 2016                                           
 *                                                                                                                                 
 *  Creation Date: May 9, 2016                                                      
 *                                                                              
 *******************************************************************************/

package org.oscm.common.rest.filters;

import javax.annotation.Priority;
import javax.ws.rs.Priorities;
import javax.ws.rs.WebApplicationException;
import javax.ws.rs.container.ContainerRequestContext;
import javax.ws.rs.container.ContainerRequestFilter;
import javax.ws.rs.core.MultivaluedMap;
import javax.ws.rs.ext.Provider;

import org.oscm.common.interfaces.enums.Messages;
import org.oscm.common.interfaces.exceptions.NotFoundException;
import org.oscm.common.interfaces.keys.VersionKey;
import org.oscm.common.rest.ExceptionMapper;
import org.oscm.common.rest.interfaces.Versioned;
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
    public static final String PROPERTY_VERSION = "version";
    public static final String PATTERN_VERSION = "v[0-9]+";
    public static final int OFFSET_VERSION = 1;

    @Override
    public void filter(ContainerRequestContext request)
            throws WebApplicationException {

        MultivaluedMap<String, String> params = request.getUriInfo()
                .getPathParameters();

        if (params == null || !params.containsKey(PARAM_VERSION)
                || params.get(PARAM_VERSION).isEmpty()) {
            NotFoundException nfe = new NotFoundException(
                    Messages.INVALID_VERSION);

            throw new ExceptionMapper().toWebException(nfe);
        }

        String version = params.get(PARAM_VERSION).get(0);

        VersionKey versionKey = validateVersion(version);

        request.setProperty(PROPERTY_VERSION, versionKey);
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

        ConfigurationManager cm = ConfigurationManager.getInstance();

        VersionKey versionKey = cm.getVersionForCompiled(vnr);

        if (versionKey == null) {
            NotFoundException nfe = new NotFoundException(
                    Messages.INVALID_VERSION);

            throw new ExceptionMapper().toWebException(nfe);
        }

        if (versionKey.compareVersion(cm.getCompatibleVersion()) < 0
                || versionKey.compareVersion(cm.getCurrentVersion()) > 0) {
            NotFoundException nfe = new NotFoundException(
                    Messages.INVALID_VERSION);

            throw new ExceptionMapper().toWebException(nfe);
        }

        return versionKey;
    }

}
