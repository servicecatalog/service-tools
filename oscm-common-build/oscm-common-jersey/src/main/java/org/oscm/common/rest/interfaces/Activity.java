/*******************************************************************************
 *                                                                              
 *  Copyright FUJITSU LIMITED 2017                                           
 *                                                                                                                                 
 *  Creation Date: Jun 2, 2017                                                      
 *                                                                              
 *******************************************************************************/

package org.oscm.common.rest.interfaces;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

import javax.ws.rs.NameBinding;

import org.oscm.common.interfaces.keys.ActivityKey.Type;

/**
 * @author miethaner
 *
 */
@NameBinding
@Retention(RetentionPolicy.RUNTIME)
public @interface Activity {

    public Type value() default Type.NONE;
}
