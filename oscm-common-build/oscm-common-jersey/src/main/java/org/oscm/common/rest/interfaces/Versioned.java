/*******************************************************************************
 *                                                                              
 *  Copyright FUJITSU LIMITED 2016                                           
 *                                                                                                                                 
 *  Creation Date: May 9, 2016                                                      
 *                                                                              
 *******************************************************************************/

package org.oscm.common.rest.interfaces;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

import javax.ws.rs.NameBinding;

/**
 * Custom annotation for REST method versioning. Annotated methods will be
 * checked for version and validated.
 * 
 * @author miethaner
 */
@NameBinding
@Retention(RetentionPolicy.RUNTIME)
public @interface Versioned {
}
