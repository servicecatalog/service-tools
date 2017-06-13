/*******************************************************************************
 *                                                                              
 *  Copyright FUJITSU LIMITED 2017                                           
 *                                                                                                                                 
 *  Creation Date: Jun 2, 2017                                                      
 *                                                                              
 *******************************************************************************/

package org.oscm.common.rest;

import java.util.Date;
import java.util.UUID;

import javax.ws.rs.Consumes;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.WebApplicationException;
import javax.ws.rs.container.AsyncResponse;
import javax.ws.rs.container.ContainerRequestContext;
import javax.ws.rs.container.Suspended;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import org.oscm.common.interfaces.data.Command;
import org.oscm.common.interfaces.data.Event;
import org.oscm.common.interfaces.data.Result;
import org.oscm.common.interfaces.data.Token;
import org.oscm.common.interfaces.events.CommandPublisher;
import org.oscm.common.interfaces.events.ResultHandler;
import org.oscm.common.interfaces.exceptions.ServiceException;
import org.oscm.common.interfaces.keys.ActivityKey;
import org.oscm.common.interfaces.keys.ActivityKey.Type;
import org.oscm.common.interfaces.services.QueryService;
import org.oscm.common.rest.filters.ActivityFilter;
import org.oscm.common.rest.filters.AuthenticationFilter;
import org.oscm.common.rest.filters.VersionFilter;
import org.oscm.common.rest.interfaces.Activity;
import org.oscm.common.rest.interfaces.Secure;
import org.oscm.common.rest.interfaces.Versioned;
import org.oscm.common.util.ServiceManager;

/**
 * @author miethaner
 *
 */
@Path("/{" + VersionFilter.PARAM_VERSION + "}")
public class Frontend {

    private static final String PATH_CMD = "/commands/{"
            + ActivityFilter.PARAM_ACTIVITY + "}";
    private static final String PATH_QUERY = "/queries/{"
            + ActivityFilter.PARAM_ACTIVITY + "}";

    private CommandPublisher publisher;
    private QueryService queryService;

    public Frontend() {
        publisher = ServiceManager.getInstance()
                .getService(CommandPublisher.SERVICE_PUBLISHER);

        queryService = ServiceManager.getInstance()
                .getService(QueryService.SERVICE_QUERY);
    }

    @Activity(Type.COMMAND)
    @Secure
    @Versioned
    @POST
    @Path(PATH_CMD)
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public void command(@Context ContainerRequestContext request, Event event,
            @Suspended final AsyncResponse asyncResponse)
            throws WebApplicationException, ServiceException {

        ActivityKey activityKey = (ActivityKey) request
                .getProperty(ActivityFilter.PROPERTY_ACTIVITY);

        Token token = (Token) request
                .getProperty(AuthenticationFilter.PROPERTY_TOKEN);

        event.validateFor(activityKey);

        Command command = new Command();
        command.setId(UUID.randomUUID());
        command.setCommand(activityKey);
        command.setEvent(event);
        command.setToken(token);
        command.setTimestamp(new Date());

        publisher.publish(command, new ResultHandler() {

            @Override
            public void handle(Result result) {
                asyncResponse.resume(createResponseFromResult(result));
            }

            @Override
            public void handle(Throwable thrown) {
                asyncResponse.resume(thrown);
            }

            @Override
            public boolean isAlive() {
                return asyncResponse.isSuspended();
            }
        });

    }

    @Activity(Type.QUERY)
    @Secure
    @Versioned
    @POST
    @Path(PATH_QUERY)
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Response query(@Context ContainerRequestContext request, Event event)
            throws WebApplicationException, ServiceException {

        ActivityKey activityKey = (ActivityKey) request
                .getProperty(ActivityFilter.PROPERTY_ACTIVITY);

        Token token = (Token) request
                .getProperty(AuthenticationFilter.PROPERTY_TOKEN);

        event.validateFor(activityKey);

        Result result = queryService.query(event, token);

        return createResponseFromResult(result);
    }

    private Response createResponseFromResult(Result result) {
        if (result == null) {
            return Response.noContent().build();
        }

        switch (result.getStatus()) {
        case SUCCESS:
            return Response.ok(result).build();
        case FAILURE:
        case CACHED:
            if (result.getFailure() != null) {
                return Response
                        .status(ExceptionMapper.getStatusForReason(
                                result.getFailure().getReason()))
                        .entity(result).build();
            } else {
                return Response.serverError().build();
            }
        case PENDING:
            return Response.accepted(result).build();
        default:
            return Response.noContent().build();
        }
    }
}
