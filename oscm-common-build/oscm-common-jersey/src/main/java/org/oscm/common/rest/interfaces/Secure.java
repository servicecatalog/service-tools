/*******************************************************************************
 *                                                                              
 *  Copyright FUJITSU LIMITED 2017                                           
 *                                                                                                                                 
 *  Creation Date: May 30, 2017                                                      
 *                                                                              
 *******************************************************************************/

package org.oscm.common.rest.interfaces;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

import javax.ws.rs.NameBinding;

/**
 * Custom annotation for REST method security. Annotated methods will be checked
 * for secure connection and authentication. Together with {@link Activity} they
 * will be also checked for authorization.
 * 
 * @author miethaner
 */
@NameBinding
@Retention(RetentionPolicy.RUNTIME)
public @interface Secure {
}
