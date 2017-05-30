/*******************************************************************************
 *                                                                              
 *  Copyright FUJITSU LIMITED 2017                                           
 *                                                                                                                                 
 *  Creation Date: May 30, 2017                                                      
 *                                                                              
 *******************************************************************************/

package org.oscm.common.rest;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

import javax.ws.rs.NameBinding;

/**
 * Custom annotation for REST method security. Annotated methods will be check
 * for secure connection and authentication.
 * 
 * @author miethaner
 */
@NameBinding
@Retention(RetentionPolicy.RUNTIME)
public @interface Secure {
}
