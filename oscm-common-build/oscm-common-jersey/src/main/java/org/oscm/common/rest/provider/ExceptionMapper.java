/*******************************************************************************
 *                                                                              
 *  Copyright FUJITSU LIMITED 2016                                           
 *                                                                                                                                 
 *  Creation Date: Jul 8, 2016                                                      
 *                                                                              
 *******************************************************************************/

package org.oscm.common.rest.provider;

import java.util.Date;

import javax.ws.rs.WebApplicationException;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.Status;
import javax.ws.rs.ext.Provider;

import org.oscm.common.interfaces.data.Failure;
import org.oscm.common.interfaces.data.Result;
import org.oscm.common.interfaces.enums.Reason;
import org.oscm.common.interfaces.exceptions.CacheException;
import org.oscm.common.interfaces.exceptions.NotFoundException;
import org.oscm.common.interfaces.exceptions.SecurityException;
import org.oscm.common.interfaces.exceptions.ServiceException;
import org.oscm.common.interfaces.exceptions.TimeoutException;
import org.oscm.common.interfaces.exceptions.TokenException;
import org.oscm.common.interfaces.exceptions.ValidationException;
import org.oscm.common.interfaces.keys.VersionKey;
import org.oscm.common.util.ConfigurationManager;
import org.oscm.common.util.logger.ServiceLogger;

/**
 * Exception mapper for all exceptions. Builds result entities with failure that
 * are returned within the response.
 * 
 * @author miethaner
 */
@Provider
public class ExceptionMapper
        implements javax.ws.rs.ext.ExceptionMapper<Exception> {

    private static final ServiceLogger LOGGER = ServiceLogger
            .getLogger(ExceptionMapper.class);

    @Override
    public Response toResponse(Exception exception) {

        VersionKey version = ConfigurationManager.getInstance()
                .getCurrentVersion();

        Result result = new Result();
        Failure fail = null;

        try {
            throw exception;

        } catch (ValidationException | NotFoundException | TimeoutException e) {
            LOGGER.debug(e);
            fail = e.getAsFailure();

        } catch (CacheException e) {
            LOGGER.debug(e);
            fail = e.getAsFailure();
            result.setStatus(org.oscm.common.interfaces.enums.Status.CACHED);

        } catch (TokenException | SecurityException e) {
            LOGGER.warning(e);
            fail = e.getAsFailure();

        } catch (ServiceException e) {
            LOGGER.error(e);
            fail = e.getAsFailure();

        } catch (Exception e) {
            LOGGER.exception(e);
            fail = new Failure();
            fail.setReason(Reason.INTERNAL);
        }

        result.setStatus(org.oscm.common.interfaces.enums.Status.FAILED);
        result.setFailure(fail);
        result.setTimestamp(new Date());
        result.setVersion(version);

        fail.setMessage(exception.getMessage());

        return Response.status(getStatusForReason(fail.getReason()))
                .entity(result).type(MediaType.APPLICATION_JSON_TYPE).build();
    }

    /**
     * Converts a exception to a web application exception
     * 
     * @param exception
     *            the service exception
     * @return the web application exception
     */
    public WebApplicationException toWebException(Exception exception) {
        return new WebApplicationException(toResponse(exception));
    }

    public static int getStatusForReason(Reason reason) {
        switch (reason) {
        case CACHE:
            return Status.NOT_MODIFIED.getStatusCode();
        case CONCURRENCY:
            return Status.CONFLICT.getStatusCode();
        case CONFLICT:
            return Status.CONFLICT.getStatusCode();
        case CONNECTION:
            return Status.BAD_GATEWAY.getStatusCode();
        case INTERNAL:
            return Status.INTERNAL_SERVER_ERROR.getStatusCode();
        case NOT_FOUND:
            return Status.NOT_FOUND.getStatusCode();
        case SECURITY:
            return Status.FORBIDDEN.getStatusCode();
        case TIMEOUT:
            return Status.SERVICE_UNAVAILABLE.getStatusCode();
        case TOKEN:
            return Status.UNAUTHORIZED.getStatusCode();
        case VALIDATION:
            return Status.BAD_REQUEST.getStatusCode();
        default:
            return Status.INTERNAL_SERVER_ERROR.getStatusCode();
        }
    }

}
