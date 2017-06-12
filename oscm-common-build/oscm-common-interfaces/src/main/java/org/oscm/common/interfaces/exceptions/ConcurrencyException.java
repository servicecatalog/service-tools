/*******************************************************************************
 *                                                                              
 *  Copyright FUJITSU LIMITED 2016                                           
 *                                                                                                                                 
 *  Creation Date: Jun 23, 2016                                                      
 *                                                                              
 *******************************************************************************/

package org.oscm.common.interfaces.exceptions;

import org.oscm.common.interfaces.enums.Reason;
import org.oscm.common.interfaces.keys.MessageKey;

/**
 * Component exception for concurrency events and errors.
 * 
 * @author miethaner
 */
public class ConcurrencyException extends ServiceException {

    private static final long serialVersionUID = -4180662601244206044L;

    /**
     * Creates new concurrency exception
     * 
     * @param messageKey
     *            the enum key for the message
     * @param values
     *            the values for message placeholders
     */
    public ConcurrencyException(MessageKey messageKey, String... values) {
        super(messageKey, values);
    }

    /**
     * Creates new concurrency exception
     * 
     * @param messageKey
     *            the enum key for the message
     * @param e
     *            the causing exception.
     * @param values
     *            the values for message placeholders
     */
    public ConcurrencyException(MessageKey messageKey, Throwable e,
            String... values) {
        super(messageKey, e, values);
    }

    @Override
    public Reason getReason() {
        return Reason.CONCURRENCY;
    }
}