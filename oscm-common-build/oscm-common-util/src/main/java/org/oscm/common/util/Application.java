/*******************************************************************************
 *                                                                              
 *  Copyright FUJITSU LIMITED 2017                                           
 *                                                                                                                                 
 *  Creation Date: Jan 18, 2017                                                      
 *                                                                              
 *******************************************************************************/

package org.oscm.common.util;

import java.util.logging.Level;
import java.util.logging.StreamHandler;

import org.oscm.common.interfaces.config.ConfigurationImporter;
import org.oscm.common.interfaces.config.ConfigurationLoader;
import org.oscm.common.util.importer.EnvironmentImporter;
import org.oscm.common.util.importer.LocalLoader;
import org.oscm.common.util.importer.PropertiesImporter;
import org.oscm.common.util.logger.LogFormatter;
import org.oscm.common.util.logger.ServiceLogger;

/**
 * Startup class to orchestrate the application and its technologies.
 * 
 * @author miethaner
 */
public abstract class Application {

    private static final String PARAM_CONFIG = "-c";
    private static final String PARAM_CONFIG_ENV = "env";
    private static final String PARAM_CONFIG_LOCAL = "local";
    private static final String PARAM_CONFIG_REMOTE = "remote";
    private static final String PARAM_CONFIG_PROPERTIES = "properties";
    private static final String PARAM_LOGGER = "-l";
    private static final String PARAM_LOGGER_STDOUT = "stdout";
    private static final String PARAM_RESET = "-r";

    /**
     * Starts the application with the following parameter options:
     * <p>
     * configuration source: -c [local|remote|env] {[properties|xml|yaml|json]
     * &lt;location&gt;} <br>
     * determines the source, the type and the location of the configuration.
     * <p>
     * logger configuration: -l [stdout|file] &lt;level&gt;
     * {&lt;location&gt;}<br>
     * determines the destination and the log level for the application logger.
     * <p>
     * reset data connection: -r <br>
     * republishes all data and reconsumes all proxies
     * <p>
     * The main thread waits until any input on the command line is entered and
     * stops the application after that.
     * 
     * @param app
     *            the application instance
     * @param params
     *            the parameters
     */
    protected void flow(String... params) throws Exception {

        processParameters(params);

        Runtime.getRuntime().addShutdownHook(new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    System.out.println("Stopping service");
                    stop();
                } catch (Exception e) {
                    // ignore
                }
            }
        }));

        System.out.println("Starting service");
        start();

        System.out.println("Press Ctrl + C to stop service");
        Thread.currentThread().join();

    }

    protected boolean reset = false;
    protected ConfigurationImporter importer = null;

    /**
     * Processes the parameters and creates depending classes, like the
     * configuration importer and loader, according to the given values.
     * 
     * @param params
     *            the parameters to process
     * @throws Exception
     */
    protected void processParameters(String... params) throws Exception {

        boolean config = false;
        boolean log = false;
        ConfigurationLoader loader = null;

        for (int i = 0; i < params.length; i++) {
            switch (params[i]) {
            case PARAM_CONFIG:
                i++;
                config = true;

                switch (params[i]) {
                case PARAM_CONFIG_ENV:
                    importer = new EnvironmentImporter();
                    break;

                case PARAM_CONFIG_LOCAL:
                    if (i + 1 >= params.length) {
                        throw new RuntimeException(
                                "Incomplete configuration parameters");
                    }
                    i++;

                    loader = new LocalLoader(params[i + 1]);
                    switch (params[i]) {
                    case PARAM_CONFIG_PROPERTIES:
                        importer = new PropertiesImporter(loader);
                        break;
                    default:
                        throw new RuntimeException(
                                "Unknown configuration type");
                    }

                    i++;
                    break;

                case PARAM_CONFIG_REMOTE:
                    throw new RuntimeException(
                            "Configuration loader not implemented");

                default:
                    throw new RuntimeException(
                            "Incomplete configuration parameters");
                }
                break;

            case PARAM_LOGGER:
                if (i + 1 >= params.length) {
                    throw new RuntimeException();
                }
                i++;
                log = true;

                switch (params[i]) {
                case PARAM_LOGGER_STDOUT:
                    if (i + 1 >= params.length) {
                        throw new RuntimeException("Incomplete parameters");
                    }
                    i++;

                    LogFormatter lf = new LogFormatter();
                    StreamHandler sh = new StreamHandler(System.out, lf);

                    Level logLevel;

                    try {
                        logLevel = Level.parse(params[i]);
                    } catch (IllegalArgumentException e) {
                        throw new RuntimeException("Unknown log level");
                    }

                    ServiceLogger.init(sh, logLevel);
                    break;

                default:
                    throw new RuntimeException("Incomplete logger parameters");
                }

            case PARAM_RESET:
                reset = true;
                break;

            default:
                throw new RuntimeException("Unknown parameter");
            }
        }

        if (!config) {
            throw new RuntimeException("Configuration parameter is missing");
        }

        if (!log) {
            throw new RuntimeException("Logging parameter is missing");
        }
    }

    /**
     * Orchestrates all necessary resources and starts the all components.
     * 
     * @throws Exception
     */
    protected abstract void start() throws Exception;

    /**
     * Stops all running components.
     */
    protected abstract void stop() throws Exception;

}
