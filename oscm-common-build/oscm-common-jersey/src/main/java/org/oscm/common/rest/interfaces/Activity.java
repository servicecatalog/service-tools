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
 * Custom annotation for REST activity methods. Annotated methods will be
 * checked for activities and validated. Together with {@link Secure} they will
 * be also checked for authorization. And Together with {@link Versioned} they
 * will be checked for acitivity versions.
 * 
 * @author miethaner
 */
@NameBinding
@Retention(RetentionPolicy.RUNTIME)
public @interface Activity {

    /**
     * The type of the activity.
     * 
     * @return the type
     */
    public Type value() default Type.NONE;
}
