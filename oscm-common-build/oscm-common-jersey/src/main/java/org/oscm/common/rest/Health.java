/*******************************************************************************
 *                                                                              
 *  Copyright FUJITSU LIMITED 2017                                           
 *                                                                                                                                 
 *  Creation Date: May 30, 2017                                                      
 *                                                                              
 *******************************************************************************/

package org.oscm.common.rest;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.core.Response;

/**
 * Basic REST endpoint for service health checks.
 * 
 * @author miethaner
 */
@Path("/health")
public class Health {

    /**
     * Health check for the running service.
     * 
     * @return response with status code 200
     */
    @GET
    public Response checkHealth() {
        return Response.ok().build();
    }

}
