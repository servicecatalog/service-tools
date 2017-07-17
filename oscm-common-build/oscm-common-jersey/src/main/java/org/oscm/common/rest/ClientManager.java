/*******************************************************************************
 *                                                                              
 *  Copyright FUJITSU LIMITED 2017                                           
 *                                                                                                                                 
 *  Creation Date: Jul 12, 2017                                                      
 *                                                                              
 *******************************************************************************/

package org.oscm.common.rest;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import javax.ws.rs.client.Client;
import javax.ws.rs.client.ClientBuilder;
import javax.ws.rs.client.WebTarget;
import javax.ws.rs.core.Feature;

import org.glassfish.jersey.SslConfigurator;
import org.oscm.common.interfaces.keys.ApplicationKey;

/**
 * Singleton class to manage the REST base client.
 * 
 * @author miethaner
 */
public class ClientManager {

    private static volatile ClientManager cm;

    /**
     * Returns the singleton instance of the client manager.
     * 
     * @return the client manager
     */
    public static ClientManager getInstance() {
        if (cm == null) {
            cm = new ClientManager();
        }

        return cm;
    }

    /**
     * Initializes the client manager from the given ssl configuration. This
     * overwrites the previous client manager instance.
     * 
     * @param ssl
     *            the SSL configurator
     */
    public static void init(SslConfigurator ssl) {
        cm = new ClientManager(ssl);
    }

    private Client client;

    private Map<ApplicationKey, List<Class<?>>> providers;
    private Map<ApplicationKey, List<Feature>> features;

    private ClientManager() {
        this.providers = new ConcurrentHashMap<>();
        this.features = new ConcurrentHashMap<>();
        this.client = ClientBuilder.newClient();
    }

    private ClientManager(SslConfigurator ssl) {
        this.providers = new ConcurrentHashMap<>();
        this.features = new ConcurrentHashMap<>();
        this.client = ClientBuilder.newBuilder()
                .sslContext(ssl.createSSLContext()).build();
    }

    public synchronized void addProvider(ApplicationKey application,
            Class<?> provider) {
        if (application == null || provider == null) {
            throw new RuntimeException(
                    "Unable to add provider for " + application);
        }

        List<Class<?>> list;

        if (providers.containsKey(application)) {
            list = providers.get(application);
        } else {
            list = new ArrayList<>();
            providers.put(application, list);
        }

        list.add(provider);
    }

    public synchronized void addFeature(ApplicationKey application,
            Feature feature) {
        if (application == null || feature == null) {
            throw new RuntimeException(
                    "Unable to add feature for " + application);
        }

        List<Feature> list;

        if (features.containsKey(application)) {
            list = features.get(application);
        } else {
            list = new ArrayList<>();
            features.put(application, list);
        }

        list.add(feature);
    }

    /**
     * Creates a web target of the given application for the given URL from the
     * client.
     * 
     * @param application
     *            the application key
     * @param target
     *            the target URL
     * @return the web target
     */
    public synchronized WebTarget getTarget(ApplicationKey application,
            String target) {

        WebTarget wt = client.target(target);

        if (providers.containsKey(application)) {
            providers.get(application).forEach((c) -> wt.register(c));
        }

        if (features.containsKey(application)) {
            features.get(application).forEach((f) -> wt.register(f));
        }

        return wt;
    }
}
