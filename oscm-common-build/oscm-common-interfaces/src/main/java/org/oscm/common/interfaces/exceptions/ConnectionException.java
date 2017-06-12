/*******************************************************************************
 *                                                                              
 *  Copyright FUJITSU LIMITED 2016                                           
 *                                                                                                                                 
 *  Creation Date: Aug 24, 2016                                                      
 *                                                                              
 *******************************************************************************/

package org.oscm.common.interfaces.exceptions;

import org.oscm.common.interfaces.enums.Reason;
import org.oscm.common.interfaces.keys.MessageKey;

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
     * @param messageKey
     *            the enum key for the message
     * @param values
     *            the values for message placeholders
     */
    public ConnectionException(MessageKey messageKey, String... values) {
        super(messageKey, values);
    }

    /**
     * Creates new connection exception
     * 
     * @param messageKey
     *            the enum key for the message
     * @param e
     *            the causing exception.
     * @param values
     *            the values for message placeholders
     */
    public ConnectionException(MessageKey messageKey, Throwable e,
            String... values) {
        super(messageKey, e, values);
    }

    @Override
    public Reason getReason() {
        return Reason.CONNECTION;
    }
}
