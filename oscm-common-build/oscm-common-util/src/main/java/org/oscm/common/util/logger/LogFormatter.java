/*******************************************************************************
 *                                                                              
 *  Copyright FUJITSU LIMITED 2017                                           
 *                                                                                                                                 
 *  Creation Date: May 12, 2017                                                      
 *                                                                              
 *******************************************************************************/

package org.oscm.common.util.logger;

import java.io.PrintWriter;
import java.io.StringWriter;
import java.net.InetAddress;
import java.net.UnknownHostException;
import java.sql.Date;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.logging.Formatter;
import java.util.logging.LogRecord;

import org.oscm.common.interfaces.exceptions.ServiceException;

/**
 * Custom formatter class for logs.
 * 
 * @author miethaner
 */
public class LogFormatter extends Formatter {

    private static final String FORMAT = "yyyy-MM-dd'T'HH:mm:ssXXX";

    private String hostname;
    private DateFormat format;

    public LogFormatter() {
        try {
            hostname = InetAddress.getLocalHost().getHostName();
        } catch (UnknownHostException e) {
            throw new RuntimeException("Unable to lookup hostname");
        }

        format = new SimpleDateFormat(FORMAT);
    }

    @Override
    public String format(LogRecord record) {

        StringBuilder sb = new StringBuilder();
        sb.append("[");
        sb.append(hostname);
        sb.append("] - [");
        sb.append(format.format(new Date(record.getMillis())));
        sb.append("] - [");
        sb.append(record.getLevel());

        if (record.getSourceClassName() != null) {
            sb.append("] - [");
            sb.append(record.getSourceClassName());

            if (record.getSourceMethodName() != null) {
                sb.append(".");
                sb.append(record.getSourceMethodName());
            }
        }

        sb.append("] ");
        sb.append(record.getMessage());

        if (record.getThrown() != null) {
            if (record.getThrown() instanceof ServiceException) {
                ServiceException se = (ServiceException) record.getThrown();
                sb.append(" - [");
                sb.append(se.getId().toString());
                sb.append("] - ");
            } else {
                sb.append(" - ");
            }

            StringWriter sw = new StringWriter();
            PrintWriter pw = new PrintWriter(sw);
            record.getThrown().printStackTrace(pw);
            pw.close();
            sb.append(sw.toString());
        }

        sb.append("\n");

        return sb.toString();
    }

}
