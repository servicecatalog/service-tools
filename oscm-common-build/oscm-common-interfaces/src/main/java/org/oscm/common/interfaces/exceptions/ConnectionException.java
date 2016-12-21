/*******************************************************************************
 *                                                                              
 *  Copyright FUJITSU LIMITED 2016                                           
 *                                                                                                                                 
 *  Creation Date: Aug 24, 2016                                                      
 *                                                                              
 *******************************************************************************/

package org.oscm.common.interfaces.exceptions;

import org.oscm.common.interfaces.config.ErrorKey;

/**
 * Connection exception for connectivity errors and events
 * 
 * @author miethaner
 */
public class ConnectionException extends ServiceException {

    private static final long serialVersionUID = 1981755503432130845L;

    /**
     * Creates new connection exception
     * 
     * @param errorKey
     *            the enum key for the error
     * @param values
     *            the values for message placeholders
     */
    public ConnectionException(ErrorKey errorKey, String... values) {
        super(errorKey, values);
    }

    /**
     * Creates new connection exception
     * 
     * @param errorKey
     *            the enum key for the error
     * @param e
     *            the causing exception.
     * @param values
     *            the values for message placeholders
     */
    public ConnectionException(ErrorKey errorKey, Throwable e,
            String... values) {
        super(errorKey, e, values);
    }
}
