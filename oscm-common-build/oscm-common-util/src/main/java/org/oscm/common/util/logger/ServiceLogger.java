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

import org.oscm.common.interfaces.exceptions.ServiceException;
import org.oscm.common.interfaces.keys.MessageKey;

/**
 * Wrapper class for logging with convenience methods. Delegates to
 * {@link java.util.logging.Logger}.
 * 
 * @author miethaner
 */
public class ServiceLogger {

    /**
     * Gets a new instance of the service logger for the given class.
     * 
     * @param clazz
     *            the logging class
     * @return the logger
     */
    public static ServiceLogger getLogger(Class<?> clazz) {
        return new ServiceLogger(clazz);
    }

    /**
     * Initialize the logger with the given handler and level
     * 
     * @param handler
     *            the log handler
     * @param level
     *            the logging level
     */
    public static void init(Handler handler, Level level) {
        Logger logger = Logger.getLogger("");
        // for (Handler h : logger.getHandlers()) {
        // logger.removeHandler(h);
        // }

        logger.addHandler(handler);
        logger.setLevel(level);
    }

    private Logger logger;

    private ServiceLogger(Class<?> clazz) {
        logger = Logger.getLogger(clazz.getName());
    }

    /**
     * Logging the entering of the given method in the given class.
     * 
     * @param className
     *            the methods class
     * @param methodName
     *            the entered method
     */
    public void in(String className, String methodName) {
        logger.logp(Level.FINER, className, methodName, ">>> IN >>>");
    }

    /**
     * Logging the exiting of the given method in the given class.
     * 
     * @param className
     *            the methods class
     * @param methodName
     *            the exited method
     */
    public void out(String className, String methodName) {
        logger.logp(Level.FINER, className, methodName, "<<< OUT <<<");
    }

    /**
     * Logging a debug message
     * 
     * @param messageKey
     *            the key of the message
     * @param values
     *            the values for the message placeholders
     */
    public void debug(MessageKey messageKey, String... values) {
        logger.log(Level.FINE, messageKey.getMessage(values));
    }

    /**
     * Logging a debug exception
     * 
     * @param thrown
     *            the exception
     */
    public void debug(ServiceException thrown) {
        logger.log(Level.FINE, thrown.getMessage(), thrown);
    }

    /**
     * Logging a info message
     * 
     * @param messageKey
     *            the key of the message
     * @param values
     *            the values for the message placeholders
     */
    public void info(MessageKey messageKey, String... values) {
        logger.log(Level.INFO, messageKey.getMessage(values));
    }

    /**
     * Logging a info exception
     * 
     * @param thrown
     *            the exception
     */
    public void info(ServiceException thrown) {
        logger.log(Level.INFO, thrown.getMessage(), thrown);
    }

    /**
     * Logging a warning message
     * 
     * @param messageKey
     *            the key of the message
     * @param values
     *            the values for the message placeholders
     */
    public void warning(MessageKey messageKey, String... values) {
        logger.log(Level.WARNING, messageKey.getMessage(values));
    }

    /**
     * Logging a warning exception
     * 
     * @param thrown
     *            the exception
     */
    public void warning(ServiceException thrown) {
        logger.log(Level.WARNING, thrown.getMessage(), thrown);
    }

    /**
     * Logging a error message
     * 
     * @param messageKey
     *            the key of the message
     * @param values
     *            the values for the message placeholders
     */
    public void error(MessageKey messageKey, String... values) {
        logger.log(Level.SEVERE, messageKey.getMessage(values));
    }

    /**
     * Logging a error exception
     * 
     * @param thrown
     *            the exception
     */
    public void error(ServiceException thrown) {
        logger.log(Level.SEVERE, thrown.getMessage(), thrown);
    }

    /**
     * Logging a exception
     * 
     * @param thrown
     *            the exception
     */
    public void exception(Throwable thrown) {
        logger.log(Level.SEVERE, thrown.getMessage(), thrown);
    }
}
