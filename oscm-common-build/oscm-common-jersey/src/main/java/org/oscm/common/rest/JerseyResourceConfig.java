/*******************************************************************************
 *                                                                              
 *  Copyright FUJITSU LIMITED 2017                                           
 *                                                                                                                                 
 *  Creation Date: Jun 14, 2017                                                      
 *                                                                              
 *******************************************************************************/

package org.oscm.common.rest;

import javax.ws.rs.ApplicationPath;

import org.glassfish.hk2.utilities.binding.AbstractBinder;
import org.glassfish.jersey.process.internal.RequestScoped;
import org.glassfish.jersey.server.ResourceConfig;
import org.oscm.common.rest.filters.ActivityFilter;
import org.oscm.common.rest.filters.AuthenticationFilter;
import org.oscm.common.rest.filters.AuthorizationFilter;
import org.oscm.common.rest.filters.MethodFilter;
import org.oscm.common.rest.filters.VersionFilter;
import org.oscm.common.rest.provider.ExceptionMapper;
import org.oscm.common.rest.provider.VersionedMessageProvider;
import org.oscm.common.rest.provider.ContextFactory;

/**
 * Configuration class for the REST service layer with jersey.
 * 
 * @author miethaner
 */
@ApplicationPath("")
public class JerseyResourceConfig extends ResourceConfig {

    public JerseyResourceConfig() {

        register(new AbstractBinder() {
            @Override
            protected void configure() {
                bindFactory(ContextFactory.class)
                        .to(ServiceRequestContext.class).proxy(true)
                        .proxyForSameScope(false).in(RequestScoped.class);
            }
        });

        register(ActivityFilter.class);
        register(AuthenticationFilter.class);
        register(AuthorizationFilter.class);
        register(MethodFilter.class);
        register(VersionFilter.class);

        register(Health.class);
        register(Frontend.class);

        register(VersionedMessageProvider.class);
        register(ExceptionMapper.class);
    }

}
