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
import java.util.function.Supplier;

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

    private static volatile ServiceManager rm;

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

    private Map<ApplicationKey, Supplier<CommandPublisher>> publishers;
    private Map<ActivityKey, Supplier<CommandService>> commands;
    private Map<ActivityKey, Supplier<QueryService>> queries;
    private Map<TransitionKey, Supplier<TransitionService>> transitions;
    private Map<ConsumerKey, Supplier<ConsumerService>> consumers;
    private Map<EntityKey, Supplier<EventSource<?>>> sources;

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

        return publishers.get(application).get();
    }

    /**
     * Sets the supplier for the command publisher as value with the given
     * application as key.
     * 
     * @param application
     *            the application key
     * @param supplier
     *            the supplier for the publisher
     */
    public void setPublisher(ApplicationKey application,
            Supplier<CommandPublisher> supplier) {
        if (application == null || supplier == null) {
            throw new RuntimeException(
                    "Unable to set " + application + " publisher");
        }

        publishers.put(application, supplier);
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

        return commands.get(command).get();
    }

    /**
     * Sets the supplier for the command service as value with the given command
     * as key.
     * 
     * @param command
     *            the command key
     * @param supplier
     *            the supplier of the service
     */
    public void setCommandService(ActivityKey command,
            Supplier<CommandService> supplier) {
        if (command == null || supplier == null
                || command.getType() != Type.COMMAND) {
            throw new RuntimeException("Unable to set " + command + " service");
        }

        commands.put(command, supplier);
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

        return queries.get(query).get();
    }

    /**
     * Sets the supplier for the query service as value with the given query as
     * key.
     * 
     * @param query
     *            the query key
     * @param supplier
     *            the supplier of the service
     */
    public void setQueryService(ActivityKey query,
            Supplier<QueryService> supplier) {
        if (query == null || supplier == null
                || query.getType() != Type.QUERY) {
            throw new RuntimeException("Unable to set " + query + " service");
        }

        queries.put(query, supplier);
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

        return transitions.get(transition).get();
    }

    /**
     * Sets the supplier for the transition service as value with the given
     * transition as key.
     * 
     * @param transition
     *            the transition key
     * @param supplier
     *            the supplier of the service
     */
    public void setTransitionService(TransitionKey transition,
            Supplier<TransitionService> supplier) {
        if (transition == null || supplier == null) {
            throw new RuntimeException(
                    "Unable to set " + transition + " service");
        }

        transitions.put(transition, supplier);
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

        return consumers.get(consumer).get();
    }

    /**
     * Sets the supplier for the consumer service as value with the given
     * consumer as key.
     * 
     * @param consumer
     *            the consumer key
     * @param supplier
     *            the supplier of the service
     */
    public void setConsumerService(ConsumerKey consumer,
            Supplier<ConsumerService> supplier) {
        if (consumer == null || supplier == null) {
            throw new RuntimeException(
                    "Unable to set " + consumer + " service");
        }

        consumers.put(consumer, supplier);
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

        return (EventSource<E>) sources.get(entity).get();
    }

    /**
     * Sets the supplier for the event source as value with the given entity as
     * key.
     * 
     * @param entity
     *            the entity key
     * @param supplier
     *            the supplier of the source
     */
    public void setEventSource(EntityKey entity,
            Supplier<EventSource<?>> supplier) {
        if (entity == null || supplier == null) {
            throw new RuntimeException("Unable to set " + entity + " source");
        }

        sources.put(entity, supplier);
    }
}
