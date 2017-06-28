/*******************************************************************************
 *                                                                              
 *  Copyright FUJITSU LIMITED 2017                                           
 *                                                                                                                                 
 *  Creation Date: Jan 18, 2017                                                      
 *                                                                              
 *******************************************************************************/

package org.oscm.common.util;

import java.util.Arrays;
import java.util.Iterator;
import java.util.logging.Level;
import java.util.logging.StreamHandler;

import org.oscm.common.interfaces.config.ConfigurationImporter;
import org.oscm.common.interfaces.enums.Messages;
import org.oscm.common.util.importer.EnvironmentImporter;
import org.oscm.common.util.importer.LocalLoader;
import org.oscm.common.util.importer.PropertiesImporter;
import org.oscm.common.util.logger.LogFormatter;
import org.oscm.common.util.logger.ServiceLogger;

/**
 * Server superclass to orchestrate the application and its technologies.
 * 
 * @author miethaner
 */
public abstract class ApplicationServer {

    private static final String PARAM_CONFIG = "-c";
    private static final String PARAM_CONFIG_ENV = "env";
    private static final String PARAM_CONFIG_LOCAL = "local";
    private static final String PARAM_CONFIG_REMOTE = "remote";
    private static final String PARAM_CONFIG_PROPERTIES = "properties";
    private static final String PARAM_LOGGER = "-l";
    private static final String PARAM_LOGGER_STDOUT = "stdout";

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
     * The main thread waits until a shutdown signal arrives and stops the
     * application after that.
     * 
     * @param app
     *            the application instance
     * @param params
     *            the parameters
     */
    protected void flow(String... params) throws Exception {

        processParameters(params);

        ServiceLogger logger = ServiceLogger.getLogger(ApplicationServer.class);

        Runtime.getRuntime().addShutdownHook(new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    logger.info(Messages.INFO_SERVICE_STOP);
                    stop();
                } catch (Exception e) {
                    // ignore
                }
            }
        }));

        logger.info(Messages.INFO_SERVICE_START);
        start();

        logger.info(Messages.INFO_SERVICE_READY);
        Thread.currentThread().join();

    }

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

        Iterator<String> itr = Arrays.asList(params).iterator();
        while (itr.hasNext()) {
            switch (itr.next()) {
            case PARAM_CONFIG:
                config = true;

                switch (itr.next()) {
                case PARAM_CONFIG_ENV:
                    importer = new EnvironmentImporter();
                    break;

                case PARAM_CONFIG_LOCAL:
                    if (!itr.hasNext()) {
                        throw new RuntimeException(
                                "Incomplete configuration parameters");
                    }

                    switch (itr.next()) {
                    case PARAM_CONFIG_PROPERTIES:
                        if (!itr.hasNext()) {
                            throw new RuntimeException(
                                    "Incomplete configuration parameters");
                        }

                        importer = new PropertiesImporter(
                                new LocalLoader(itr.next()));
                        break;
                    default:
                        throw new RuntimeException(
                                "Unknown configuration type");
                    }
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
                if (!itr.hasNext()) {
                    throw new RuntimeException("Incomplete logger parameters");
                }

                log = true;

                switch (itr.next()) {
                case PARAM_LOGGER_STDOUT:
                    if (!itr.hasNext()) {
                        throw new RuntimeException(
                                "Incomplete logger parameters");
                    }

                    LogFormatter lf = new LogFormatter();
                    StreamHandler sh = new StreamHandler(System.out, lf);

                    Level logLevel;

                    try {
                        logLevel = Level.parse(itr.next());
                    } catch (IllegalArgumentException e) {
                        throw new RuntimeException("Unknown log level");
                    }

                    ServiceLogger.init(sh, logLevel);
                    break;

                default:
                    throw new RuntimeException("Incomplete logger parameters");
                }

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
