/*******************************************************************************
 *                                                                              
 *  Copyright FUJITSU LIMITED 2017                                           
 *                                                                                                                                 
 *  Creation Date: May 26, 2017                                                      
 *                                                                              
 *******************************************************************************/

package org.oscm.common.util.logger;

import java.util.logging.Handler;
import java.util.logging.Level;
import java.util.logging.Logger;

import org.oscm.common.interfaces.config.MessageKey;
import org.oscm.common.interfaces.exceptions.ServiceException;

/**
 * Wrapper class for logging with convenience methods. Delegates to jdk logger.
 * 
 * @author miethaner
 */
public class ServiceLogger {

    public static ServiceLogger getLogger(Class<?> clazz) {
        return new ServiceLogger(clazz);
    }

    public static void init(Handler handler, Level level) {
        Logger logger = Logger.getLogger("");
        for (Handler h : logger.getHandlers()) {
            logger.removeHandler(h);
        }

        logger.addHandler(handler);
        logger.setLevel(level);
    }

    private Logger logger;

    private ServiceLogger(Class<?> clazz) {
        logger = Logger.getLogger(clazz.getName());
    }

    public void in(String className, String methodName) {
        logger.logp(Level.FINER, className, methodName, ">>> IN >>>");
    }

    public void out(String className, String methodName) {
        logger.logp(Level.FINER, className, methodName, "<<< OUT <<<");
    }

    public void debug(MessageKey messageKey, String... values) {
        logger.log(Level.FINE, messageKey.getMessage(values));
    }

    public void debug(ServiceException thrown) {
        logger.log(Level.FINE, thrown.getMessage(), thrown);
    }

    public void info(MessageKey messageKey, String... values) {
        logger.log(Level.INFO, messageKey.getMessage(values));
    }

    public void info(ServiceException thrown) {
        logger.log(Level.INFO, thrown.getMessage(), thrown);
    }

    public void warning(MessageKey messageKey, String... values) {
        logger.log(Level.WARNING, messageKey.getMessage(values));
    }

    public void warning(ServiceException thrown) {
        logger.log(Level.WARNING, thrown.getMessage(), thrown);
    }

    public void error(MessageKey messageKey, String... values) {
        logger.log(Level.SEVERE, messageKey.getMessage(values));
    }

    public void error(ServiceException thrown) {
        logger.log(Level.SEVERE, thrown.getMessage(), thrown);
    }

    public void exception(Exception thrown) {
        logger.log(Level.SEVERE, thrown.getMessage(), thrown);
    }
}
