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
 * Interface for result handlers with asynchronous requests.
 * 
 * @author miethaner
 */
public interface ResultHandler {

    /**
     * Creates a response from the given result for the handlers corresponding
     * request.
     * 
     * @param result
     *            the result for the response
     */
    public void handle(Result result);

    /**
     * Creates a response from the given throwable for the handlers
     * corresponding request.
     * 
     * @param thrown
     *            the throwable for the response
     */
    public void handle(Throwable thrown);

    /**
     * Registers the given runnable to be executed on a timeout event.
     * 
     * @param run
     *            the runnable to execute
     */
    public void onTimeout(Runnable run);
}
