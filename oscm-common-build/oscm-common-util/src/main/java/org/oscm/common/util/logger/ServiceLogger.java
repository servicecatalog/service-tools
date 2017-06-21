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
import java.util.logging.LogManager;
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
        LogManager.getLogManager().reset();

        Logger logger = Logger.getLogger("");
        logger.addHandler(handler);
        logger.setLevel(level);
    }

    private Logger logger;
    private Class<?> clazz;

    private ServiceLogger(Class<?> clazz) {
        this.clazz = clazz;
        logger = Logger.getLogger(clazz.getName());
    }

    /**
     * Logging the entering of the given method in the loggers class.
     * 
     * @param methodName
     *            the entered method
     */
    public void in(String methodName) {
        logger.logp(Level.FINER, clazz.getName(), methodName, ">>> IN >>>");
    }

    /**
     * Logging the exiting of the given method in the loggers class.
     * 
     * @param methodName
     *            the exited method
     */
    public void out(String methodName) {
        logger.logp(Level.FINER, clazz.getName(), methodName, "<<< OUT <<<");
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
        logger.logp(Level.FINE, clazz.getName(), null,
                messageKey.getMessage(values));
    }

    /**
     * Logging a debug exception
     * 
     * @param thrown
     *            the exception
     */
    public void debug(ServiceException thrown) {
        logger.logp(Level.FINE, clazz.getName(), null, thrown.getMessage(),
                thrown);
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
        logger.logp(Level.INFO, clazz.getName(), null,
                messageKey.getMessage(values));
    }

    /**
     * Logging a info exception
     * 
     * @param thrown
     *            the exception
     */
    public void info(ServiceException thrown) {
        logger.logp(Level.INFO, clazz.getName(), null, thrown.getMessage(),
                thrown);
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
        logger.logp(Level.WARNING, clazz.getName(), null,
                messageKey.getMessage(values));
    }

    /**
     * Logging a warning exception
     * 
     * @param thrown
     *            the exception
     */
    public void warning(ServiceException thrown) {
        logger.logp(Level.WARNING, clazz.getName(), null, thrown.getMessage(),
                thrown);
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
        logger.logp(Level.SEVERE, clazz.getName(), null,
                messageKey.getMessage(values));
    }

    /**
     * Logging a error exception
     * 
     * @param thrown
     *            the exception
     */
    public void error(ServiceException thrown) {
        logger.logp(Level.SEVERE, clazz.getName(), null, thrown.getMessage(),
                thrown);
    }

    /**
     * Logging a exception
     * 
     * @param thrown
     *            the exception
     */
    public void exception(Throwable thrown) {
        logger.logp(Level.SEVERE, clazz.getName(), null, thrown.getMessage(),
                thrown);
    }
}
