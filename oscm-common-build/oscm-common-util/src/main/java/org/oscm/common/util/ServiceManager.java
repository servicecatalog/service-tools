/*******************************************************************************
 *                                                                              
 *  Copyright FUJITSU LIMITED 2016                                           
 *                                                                                                                                 
 *  Creation Date: Dec 6, 2016                                                      
 *                                                                              
 *******************************************************************************/

package org.oscm.common.util;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import org.oscm.common.interfaces.data.Event;
import org.oscm.common.interfaces.events.CommandPublisher;
import org.oscm.common.interfaces.events.EventSource;
import org.oscm.common.interfaces.keys.ActivityKey;
import org.oscm.common.interfaces.keys.ActivityKey.Type;
import org.oscm.common.interfaces.keys.ServiceKey;
import org.oscm.common.interfaces.keys.TransitionKey;
import org.oscm.common.interfaces.services.CommandService;
import org.oscm.common.interfaces.services.QueryService;
import org.oscm.common.interfaces.services.TransitionService;

/**
 * Singleton class to manage service lookups.
 * 
 * @author miethaner
 */
public class ServiceManager {

    private static ServiceManager rm;

    /**
     * Returns the singleton instance of the service manager.
     * 
     * @return the resource manager
     */
    public static ServiceManager getInstance() {
        if (rm == null) {
            rm = new ServiceManager();
        }

        return rm;
    }

    private Map<ServiceKey, CommandPublisher> publishers;
    private Map<ActivityKey, CommandService> commands;
    private Map<ActivityKey, QueryService> queries;
    private Map<TransitionKey, TransitionService> transitions;
    private Map<Class<? extends Event>, EventSource<?>> sources;

    private ServiceManager() {
        publishers = new ConcurrentHashMap<>();
        commands = new ConcurrentHashMap<>();
        queries = new ConcurrentHashMap<>();
        transitions = new ConcurrentHashMap<>();
        sources = new ConcurrentHashMap<>();
    }

    /**
     * Gets the command publisher instance for the given application.
     * 
     * @param application
     *            the application key
     * @return the publisher
     */
    public CommandPublisher getPublisher(ServiceKey application) {
        if (application == null || !publishers.containsKey(application)) {
            throw new RuntimeException(application + " publisher not found");
        }

        return publishers.get(application);
    }

    /**
     * Sets the command publisher as value with the given application as key.
     * 
     * @param application
     *            the application key
     * @param publisher
     *            the publisher
     */
    public void setPublisher(ServiceKey application,
            CommandPublisher publisher) {
        if (application == null || publisher == null) {
            throw new RuntimeException(
                    "Unable to set " + application + " publisher");
        }

        publishers.put(application, publisher);
    }

    /**
     * Gets the command service instance for the given command.
     * 
     * @param command
     *            the command key
     * @return the service instance
     */
    public CommandService getCommandService(ActivityKey command) {
        if (command == null || !commands.containsKey(command)) {
            throw new RuntimeException(command + " serivce not found");
        }

        return commands.get(command);
    }

    /**
     * Sets the command service as value with the given command as key.
     * 
     * @param command
     *            the command key
     * @param service
     *            the service
     */
    public void setCommandService(ActivityKey command, CommandService service) {
        if (command == null || service == null
                || command.getType() != Type.COMMAND) {
            throw new RuntimeException("Unable to set " + command + " service");
        }

        commands.put(command, service);
    }

    /**
     * Gets the query service instance for the given query.
     * 
     * @param query
     *            the query key
     * @return the service instance
     */
    public QueryService getQueryService(ActivityKey query) {
        if (query == null || !queries.containsKey(query)) {
            throw new RuntimeException(query + " serivce not found");
        }

        return queries.get(query);
    }

    /**
     * Sets the query service as value with the given query as key.
     * 
     * @param query
     *            the query key
     * @param service
     *            the service
     */
    public void setQueryService(ActivityKey query, QueryService service) {
        if (query == null || service == null || query.getType() != Type.QUERY) {
            throw new RuntimeException("Unable to set " + query + " service");
        }

        queries.put(query, service);
    }

    /**
     * Gets the transition service instance for the given transition key.
     * 
     * @param transition
     *            the transition key
     * @return the service instance
     */
    public TransitionService getTransitionService(TransitionKey transition) {
        if (transition == null || !transitions.containsKey(transition)) {
            throw new RuntimeException(transition + " serivce not found");
        }

        return transitions.get(transition);
    }

    /**
     * Sets the transition service as value with the given event as key.
     * 
     * @param transition
     *            the transition key
     * @param service
     *            the service
     */
    public void setTransitionService(TransitionKey transition,
            TransitionService service) {
        if (transition == null || service == null) {
            throw new RuntimeException(
                    "Unable to set " + transition + " service");
        }

        transitions.put(transition, service);
    }

    /**
     * Gets the event source instance for the given event class.
     * 
     * @param clazz
     *            the event class
     * @return the source instance
     */
    @SuppressWarnings("unchecked")
    public <E extends Event> EventSource<E> getEventSource(Class<E> clazz) {
        if (clazz == null || !sources.containsKey(clazz)) {
            throw new RuntimeException(clazz + " source not found");
        }

        return (EventSource<E>) sources.get(clazz);
    }

    /**
     * Sets the event service as value with the given event as key.
     * 
     * @param event
     *            the event class
     * @param source
     *            the source
     */
    public <E extends Event> void setEventSource(Class<E> clazz,
            EventSource<E> source) {
        if (clazz == null || source == null) {
            throw new RuntimeException("Unable to set " + clazz + " source");
        }

        sources.put(clazz, source);
    }
}
