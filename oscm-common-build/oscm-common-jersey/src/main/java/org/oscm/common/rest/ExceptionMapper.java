/*******************************************************************************
 *                                                                              
 *  Copyright FUJITSU LIMITED 2016                                           
 *                                                                                                                                 
 *  Creation Date: Jul 8, 2016                                                      
 *                                                                              
 *******************************************************************************/

package org.oscm.common.rest;

import java.util.UUID;

import javax.ws.rs.WebApplicationException;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.Status;
import javax.ws.rs.ext.Provider;

import org.oscm.common.interfaces.exceptions.CacheException;
import org.oscm.common.interfaces.exceptions.ConcurrencyException;
import org.oscm.common.interfaces.exceptions.ConflictException;
import org.oscm.common.interfaces.exceptions.NotFoundException;
import org.oscm.common.interfaces.exceptions.SecurityException;
import org.oscm.common.interfaces.exceptions.ServiceException;
import org.oscm.common.interfaces.exceptions.TokenException;
import org.oscm.common.interfaces.exceptions.ValidationException;

import com.google.gson.annotations.SerializedName;

/**
 * Exception mapper for service exceptions to format them in the correct json
 * format
 * 
 * @author miethaner
 */
@Provider
public class ExceptionMapper
        implements javax.ws.rs.ext.ExceptionMapper<ServiceException> {

    @SuppressWarnings("unused")
    private static class ExceptionBody extends Representation {

        private static final String FIELD_STATUS = "status";
        private static final String FIELD_REFERENCE_ID = "ref_id";
        private static final String FIELD_CODE = "code";
        private static final String FIELD_PROPERTY = "property";
        private static final String FIELD_MESSAGE = "message";

        @SerializedName(FIELD_STATUS)
        private int status;

        @SerializedName(FIELD_REFERENCE_ID)
        private UUID referenceId;

        @SerializedName(FIELD_CODE)
        private Integer code;

        @SerializedName(FIELD_PROPERTY)
        private String property;

        @SerializedName(FIELD_MESSAGE)
        private String message;

        public int getStatus() {
            return status;
        }

        public void setStatus(int status) {
            this.status = status;
        }

        public UUID getReferenceId() {
            return referenceId;
        }

        public void setReferenceId(UUID referenceId) {
            this.referenceId = referenceId;
        }

        public Integer getCode() {
            return code;
        }

        public void setCode(Integer code) {
            this.code = code;
        }

        public String getProperty() {
            return property;
        }

        public void setProperty(String property) {
            this.property = property;
        }

        public String getMessage() {
            return message;
        }

        public void setMessage(String message) {
            this.message = message;
        }

        @Override
        public void validateCreate() throws WebApplicationException {
        }

        @Override
        public void validateUpdate() throws ServiceException {
        }
    }

    @Override
    public Response toResponse(ServiceException exception) {

        ExceptionBody body = new ExceptionBody();

        try {
            throw exception;
        } catch (ValidationException e) {
            body.setStatus(Status.BAD_REQUEST.getStatusCode());
            body.setProperty(e.getProperty());
        } catch (NotFoundException e) {
            body.setStatus(Status.NOT_FOUND.getStatusCode());
        } catch (CacheException e) {
            body.setStatus(Status.NOT_MODIFIED.getStatusCode());
        } catch (ConcurrencyException | ConflictException e) {
            body.setStatus(Status.CONFLICT.getStatusCode());
        } catch (TokenException e) {
            body.setStatus(Status.UNAUTHORIZED.getStatusCode());
        } catch (SecurityException e) {
            body.setStatus(Status.FORBIDDEN.getStatusCode());
        } catch (ServiceException e) {
            body.setStatus(Status.INTERNAL_SERVER_ERROR.getStatusCode());
        }

        body.setReferenceId(exception.getId());
        body.setCode(exception.getCode());
        body.setMessage(exception.getMessage());

        return Response.status(body.getStatus()).entity(body)
                .type(MediaType.APPLICATION_JSON_TYPE).build();
    }

    /**
     * Converts a service exception to a web application exception
     * 
     * @param exception
     *            the service exception
     * @return the web application exception
     */
    public WebApplicationException toWebException(ServiceException exception) {
        return new WebApplicationException(toResponse(exception));
    }

}
