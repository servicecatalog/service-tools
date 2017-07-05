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
import org.oscm.common.interfaces.keys.ApplicationKey;
import org.oscm.common.interfaces.keys.ConsumerKey;
import org.oscm.common.interfaces.keys.EntityKey;
import org.oscm.common.interfaces.keys.TransitionKey;
import org.oscm.common.interfaces.services.CommandService;
import org.oscm.common.interfaces.services.ConsumerService;
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

    private Map<ApplicationKey, CommandPublisher> publishers;
    private Map<ActivityKey, CommandService> commands;
    private Map<ActivityKey, QueryService> queries;
    private Map<TransitionKey, TransitionService> transitions;
    private Map<ConsumerKey, ConsumerService> consumers;
    private Map<EntityKey, EventSource<?>> sources;

    private ServiceManager() {
        publishers = new ConcurrentHashMap<>();
        commands = new ConcurrentHashMap<>();
        queries = new ConcurrentHashMap<>();
        transitions = new ConcurrentHashMap<>();
        consumers = new ConcurrentHashMap<>();
        sources = new ConcurrentHashMap<>();
    }

    /**
     * Gets the command publisher instance for the given application.
     * 
     * @param application
     *            the application key
     * @return the publisher
     */
    public CommandPublisher getPublisher(ApplicationKey application) {
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
    public void setPublisher(ApplicationKey application,
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
     * Sets the transition service as value with the given transition as key.
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
     * Gets the consumer service instance for the given consumer key.
     * 
     * @param consumer
     *            the consumer key
     * @return the service instance
     */
    public ConsumerService getConsumerService(ConsumerKey consumer) {
        if (consumer == null || !consumers.containsKey(consumer)) {
            throw new RuntimeException(consumer + " serivce not found");
        }

        return consumers.get(consumer);
    }

    /**
     * Sets the consumer service as value with the given consumer as key.
     * 
     * @param consumer
     *            the consumer key
     * @param service
     *            the service
     */
    public void setConsumerService(ConsumerKey consumer,
            ConsumerService service) {
        if (consumer == null || service == null) {
            throw new RuntimeException(
                    "Unable to set " + consumer + " service");
        }

        consumers.put(consumer, service);
    }

    /**
     * Gets the event source instance for the given entity key.
     * 
     * @param entity
     *            the entity key
     * @return the source instance
     */
    @SuppressWarnings("unchecked")
    public <E extends Event> EventSource<E> getEventSource(EntityKey entity) {
        if (entity == null || !sources.containsKey(entity)) {
            throw new RuntimeException(entity + " source not found");
        }

        return (EventSource<E>) sources.get(entity);
    }

    /**
     * Sets the event source as value with the given entity as key.
     * 
     * @param entity
     *            the entity key
     * @param source
     *            the source
     */
    public <E extends Event> void setEventSource(EntityKey entity,
            EventSource<E> source) {
        if (entity == null || source == null) {
            throw new RuntimeException("Unable to set " + entity + " source");
        }

        sources.put(entity, source);
    }
}
