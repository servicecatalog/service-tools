/*******************************************************************************
 *                                                                              
 *  Copyright FUJITSU LIMITED 2017                                           
 *                                                                                                                                 
 *  Creation Date: Jun 9, 2017                                                      
 *                                                                              
 *******************************************************************************/

package org.oscm.common.interfaces.events;

import org.oscm.common.interfaces.data.Result;

/**
 * @author miethaner
 *
 */
public interface ResultHandler {

    public void handle(Result result);

    public void handle(Throwable thrown);

    public boolean isAlive();
}
