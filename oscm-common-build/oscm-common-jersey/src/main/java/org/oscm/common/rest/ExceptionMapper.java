/*******************************************************************************
 *                                                                              
 *  Copyright FUJITSU LIMITED 2016                                           
 *                                                                                                                                 
 *  Creation Date: Jul 8, 2016                                                      
 *                                                                              
 *******************************************************************************/

package org.oscm.common.rest;

import javax.ws.rs.WebApplicationException;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.Status;
import javax.ws.rs.ext.Provider;

import org.oscm.common.interfaces.exceptions.CacheException;
import org.oscm.common.interfaces.exceptions.ComponentException;
import org.oscm.common.interfaces.exceptions.ConcurrencyException;
import org.oscm.common.interfaces.exceptions.ConflictException;
import org.oscm.common.interfaces.exceptions.NotFoundException;
import org.oscm.common.interfaces.exceptions.SecurityException;
import org.oscm.common.interfaces.exceptions.TokenException;
import org.oscm.common.interfaces.exceptions.ValidationException;

import com.google.gson.annotations.SerializedName;

/**
 * Exception mapper for component exceptions to format them in the correct json
 * format
 * 
 * @author miethaner
 */
@Provider
public class ExceptionMapper
        implements javax.ws.rs.ext.ExceptionMapper<ComponentException> {

    @SuppressWarnings("unused")
    private static class ExceptionBody extends Representation {

        private static final String FIELD_CODE = "code";
        private static final String FIELD_ERROR = "error";
        private static final String FIELD_PROPERTY = "property";
        private static final String FIELD_MESSAGE = "message";
        private static final String FIELD_MORE_INFO = "more_info";

        @SerializedName(FIELD_CODE)
        private int code;

        @SerializedName(FIELD_ERROR)
        private Integer error;

        @SerializedName(FIELD_PROPERTY)
        private String property;

        @SerializedName(FIELD_MESSAGE)
        private String message;

        @SerializedName(FIELD_MORE_INFO)
        private String moreInfo;

        public int getCode() {
            return code;
        }

        public void setCode(int code) {
            this.code = code;
        }

        public Integer getError() {
            return error;
        }

        public void setError(Integer error) {
            this.error = error;
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

        public String getMoreInfo() {
            return moreInfo;
        }

        public void setMoreInfo(String moreInfo) {
            this.moreInfo = moreInfo;
        }

        @Override
        public void validateCreate() throws WebApplicationException {
        }

        @Override
        public void validateUpdate() throws ComponentException {
        }
    }

    @Override
    public Response toResponse(ComponentException exception) {

        ExceptionBody body = new ExceptionBody();

        try {
            throw exception;
        } catch (ValidationException e) {
            body.setCode(Status.BAD_REQUEST.getStatusCode());
            body.setProperty(e.getProperty());
        } catch (NotFoundException e) {
            body.setCode(Status.NOT_FOUND.getStatusCode());
        } catch (CacheException e) {
            body.setCode(Status.NOT_MODIFIED.getStatusCode());
        } catch (ConcurrencyException | ConflictException e) {
            body.setCode(Status.CONFLICT.getStatusCode());
        } catch (TokenException e) {
            body.setCode(Status.UNAUTHORIZED.getStatusCode());
        } catch (SecurityException e) {
            body.setCode(Status.FORBIDDEN.getStatusCode());
        } catch (ComponentException e) {
            body.setCode(Status.INTERNAL_SERVER_ERROR.getStatusCode());
        }

        body.setError(exception.getError());
        body.setMessage(exception.getMessage());
        body.setMoreInfo(exception.getMoreInfo());

        return Response.status(body.getCode()).entity(body)
                .type(MediaType.APPLICATION_JSON_TYPE).build();
    }

    /**
     * Converts a component exception to a web application exception
     * 
     * @param exception
     *            the component exception
     * @return the web application exception
     */
    public WebApplicationException toWebException(
            ComponentException exception) {
        return new WebApplicationException(toResponse(exception));
    }

}
