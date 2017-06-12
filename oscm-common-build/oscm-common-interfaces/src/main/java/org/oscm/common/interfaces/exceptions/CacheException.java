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
 * Component exception for cache events (e.g. data not modified)
 * 
 * @author miethaner
 */
public class CacheException extends ServiceException {

    private static final long serialVersionUID = -5288079064424707113L;

    /**
     * Creates new cache exception
     * 
     * @param messageKey
     *            the enum key for the message
     * @param values
     *            the values for message placeholders
     */
    public CacheException(MessageKey messageKey, String... values) {
        super(messageKey, values);
    }

    /**
     * Creates new cache exception
     * 
     * @param messageKey
     *            the enum key for the message
     * @param e
     *            the causing exception
     * @param values
     *            the values for message placeholders
     */
    public CacheException(MessageKey messageKey, Throwable e,
            String... values) {
        super(messageKey, e, values);
    }

    @Override
    public Reason getReason() {
        return Reason.CACHE;
    }
}
