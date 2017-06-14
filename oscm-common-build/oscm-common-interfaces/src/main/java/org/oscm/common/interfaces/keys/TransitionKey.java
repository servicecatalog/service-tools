/*******************************************************************************
 *                                                                              
 *  Copyright FUJITSU LIMITED 2017                                           
 *                                                                                                                                 
 *  Creation Date: Jun 14, 2017                                                      
 *                                                                              
 *******************************************************************************/

package org.oscm.common.interfaces.keys;

import org.oscm.common.interfaces.data.Event;

/**
 * @author miethaner
 *
 */
public interface TransitionKey {

    public String getTransitionName();

    public Class<? extends Event> getInputClass();

    public Class<? extends Event> getOutputClass();

}
