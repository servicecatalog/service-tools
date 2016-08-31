/*******************************************************************************
 *                                                                              
 *  Copyright FUJITSU LIMITED 2016                                           
 *                                                                                                                                 
 *  Creation Date: Jun 9, 2016                                                      
 *                                                                              
 *******************************************************************************/

package org.oscm.common.rest.unit;

import java.security.Principal;

import javax.ws.rs.WebApplicationException;
import javax.ws.rs.core.SecurityContext;

import org.glassfish.jersey.server.ContainerRequest;
import org.junit.Test;
import org.mockito.Mockito;
import org.oscm.common.rest.SecurityFilter;

/**
 * Unit test for SecurityFilter
 * 
 * @author miethaner
 */
public class SecurityFilterTest {

    @SuppressWarnings("boxing")
    @Test
    public void testPositive() {

        ContainerRequest request = Mockito.mock(ContainerRequest.class);
        Principal principal = Mockito.mock(Principal.class);

        SecurityContext context = Mockito.mock(SecurityContext.class);
        Mockito.when(request.getSecurityContext()).thenReturn(context);
        Mockito.when(context.isSecure()).thenReturn(Boolean.TRUE);
        Mockito.when(context.getUserPrincipal()).thenReturn(principal);

        SecurityFilter filter = new SecurityFilter();
        filter.filter(request);
    }

    @SuppressWarnings("boxing")
    @Test(expected = WebApplicationException.class)
    public void testSecureConnectionNegative() throws Exception {

        ContainerRequest request = Mockito.mock(ContainerRequest.class);
        Principal principal = Mockito.mock(Principal.class);

        SecurityContext context = Mockito.mock(SecurityContext.class);
        Mockito.when(request.getSecurityContext()).thenReturn(context);
        Mockito.when(context.isSecure()).thenReturn(Boolean.FALSE);
        Mockito.when(context.getUserPrincipal()).thenReturn(principal);

        SecurityFilter filter = new SecurityFilter();
        filter.filter(request);
    }

    @SuppressWarnings("boxing")
    @Test(expected = WebApplicationException.class)
    public void testUserNegative() throws Exception {

        ContainerRequest request = Mockito.mock(ContainerRequest.class);

        SecurityContext context = Mockito.mock(SecurityContext.class);
        Mockito.when(request.getSecurityContext()).thenReturn(context);
        Mockito.when(context.isSecure()).thenReturn(Boolean.TRUE);
        Mockito.when(context.getUserPrincipal()).thenReturn(null);

        SecurityFilter filter = new SecurityFilter();
        filter.filter(request);
    }

}
