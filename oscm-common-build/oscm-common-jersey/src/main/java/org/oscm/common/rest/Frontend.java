/*******************************************************************************
 *                                                                              
 *  Copyright FUJITSU LIMITED 2017                                           
 *                                                                                                                                 
 *  Creation Date: Jun 2, 2017                                                      
 *                                                                              
 *******************************************************************************/

package org.oscm.common.rest;

import java.util.Date;
import java.util.List;
import java.util.UUID;
import java.util.concurrent.TimeUnit;

import javax.inject.Inject;
import javax.ws.rs.Consumes;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.WebApplicationException;
import javax.ws.rs.container.AsyncResponse;
import javax.ws.rs.container.Suspended;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import org.oscm.common.interfaces.data.Command;
import org.oscm.common.interfaces.data.Event;
import org.oscm.common.interfaces.data.Result;
import org.oscm.common.interfaces.data.Token;
import org.oscm.common.interfaces.enums.Messages;
import org.oscm.common.interfaces.enums.Status;
import org.oscm.common.interfaces.events.CommandPublisher;
import org.oscm.common.interfaces.events.ResultHandler;
import org.oscm.common.interfaces.exceptions.ServiceException;
import org.oscm.common.interfaces.exceptions.TimeoutException;
import org.oscm.common.interfaces.keys.ActivityKey;
import org.oscm.common.interfaces.keys.ActivityKey.Type;
import org.oscm.common.interfaces.services.QueryService;
import org.oscm.common.rest.filters.ActivityFilter;
import org.oscm.common.rest.filters.VersionFilter;
import org.oscm.common.rest.interfaces.Activity;
import org.oscm.common.rest.interfaces.Versioned;
import org.oscm.common.rest.provider.ExceptionMapper;
import org.oscm.common.util.ConfigurationManager;
import org.oscm.common.util.ServiceManager;
import org.oscm.common.util.logger.ServiceLogger;

/**
 * Main REST endpoint for services.
 * 
 * @author miethaner
 */
@Path("/{" + VersionFilter.PARAM_VERSION + "}")
public class Frontend {

    private static final String PATH_CMD = "/commands/{"
            + ActivityFilter.PARAM_ACTIVITY + "}";
    private static final String PATH_QUERY = "/queries/{"
            + ActivityFilter.PARAM_ACTIVITY + "}";

    private static final ServiceLogger LOGGER = ServiceLogger
            .getLogger(Frontend.class);

    @Inject
    private ServiceRequestContext context;

    public void setContext(ServiceRequestContext context) {
        this.context = context;
    }

    /**
     * Endpoint for executing commands. Commands will be validated and published
     * to the backend.
     * 
     * @param event
     *            the payload as event
     * @param asyncResponse
     *            the response handler
     * @throws WebApplicationException
     * @throws ServiceException
     */
    @Activity(Type.COMMAND)
    // @Secure //TODO uncomment after testing phase
    @Versioned
    @POST
    @Path(PATH_CMD)
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public void command(Event event,
            @Suspended final AsyncResponse asyncResponse)
            throws WebApplicationException, ServiceException {

        ActivityKey activityKey = context.getActivity();

        Token token = context.getToken();

        LOGGER.debug(Messages.DEBUG_COMMAND, activityKey.getActivityName());

        ConfigurationManager cm = ConfigurationManager.getInstance();

        Long timeout = cm.getConfigAsLong(JerseyConfig.JERSEY_REQUEST_TIMEOUT);

        if (timeout != null) {
            asyncResponse.setTimeout(timeout.longValue(), TimeUnit.SECONDS);
        }

        event.validateFor(activityKey);

        if (event.getId() == null) {
            event.setId(UUID.randomUUID());
        }

        if (event.getETag() == null) {
            event.setETag(UUID.randomUUID());
        }

        Command command = new Command();
        command.setId(UUID.randomUUID());
        command.setCommand(activityKey);
        command.setEvent(event);
        command.setToken(token);
        command.setTimestamp(new Date());

        CommandPublisher publisher = ServiceManager.getInstance()
                .getPublisher(cm.getSelf());

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
            public void onTimeout(Runnable run) {
                asyncResponse.setTimeoutHandler((ar) -> {
                    run.run();
                    ar.resume(new TimeoutException(Messages.ERROR_TIMEOUT));
                });
            }
        });

    }

    /**
     * Endpoint for executing queries. Queries will be validated and executed in
     * the backend.
     * 
     * @param event
     *            the payload as parameters
     * @return the response with the result
     * @throws WebApplicationException
     * @throws ServiceException
     */
    @Activity(Type.QUERY)
    // @Secure //TODO uncomment after testing phase
    @Versioned
    @POST
    @Path(PATH_QUERY)
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Response query(Event event)
            throws WebApplicationException, ServiceException {

        ActivityKey activityKey = context.getActivity();

        Token token = context.getToken();

        LOGGER.debug(Messages.DEBUG_QUERY, activityKey.getActivityName());

        event.validateFor(activityKey);

        QueryService queryService = ServiceManager.getInstance()
                .getQueryService(activityKey);

        List<Event> events = queryService.query(event, token);

        Result result = new Result();
        result.setCommand(activityKey);
        result.setEvents(events);
        result.setStatus(Status.SUCCESS);
        result.setTimestamp(new Date());

        return Response.ok(result).build();
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
