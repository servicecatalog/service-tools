/*******************************************************************************
 *                                                                              
 *  Copyright FUJITSU LIMITED 2017                                           
 *                                                                                                                                 
 *  Creation Date: Jun 14, 2017                                                      
 *                                                                              
 *******************************************************************************/

package org.oscm.common.rest;

import org.glassfish.jersey.server.ResourceConfig;
import org.oscm.common.rest.filters.ActivityFilter;
import org.oscm.common.rest.filters.AuthenticationFilter;
import org.oscm.common.rest.filters.AuthorizationFilter;
import org.oscm.common.rest.filters.MethodFilter;
import org.oscm.common.rest.filters.VersionFilter;

/**
 * @author miethaner
 *
 */
public class JerseyResourceConfig extends ResourceConfig {

    public JerseyResourceConfig() {

        register(ActivityFilter.class);
        register(AuthenticationFilter.class);
        register(AuthorizationFilter.class);
        register(MethodFilter.class);
        register(VersionFilter.class);

        register(Health.class);
        register(Frontend.class);

        register(MessageProvider.class);
        register(ExceptionMapper.class);
    }

}
