package com.shoppingcart.logger;

import org.apache.log4j.Level;
import org.apache.log4j.Logger;

public class LoggerUtil implements ILoggerUtil {
    private static final String SPACE_SEPARATOR = " ";
    private final Logger logger = Logger.getLogger(LoggerUtil.class);

    public void log(Level severityLevel, String component, String method, String status, Object object) {
        StringBuffer logLine = new StringBuffer();
        if (object != null) {
            logger.log(severityLevel,
                    logLine.append(component).append(SPACE_SEPARATOR).append("method=").append(method)
                            .append(SPACE_SEPARATOR).append("status=").append(status).append(SPACE_SEPARATOR)
                            .append("object=").append(object.toString()));
        } else {
            logger.log(severityLevel, logLine.append(component).append(SPACE_SEPARATOR).append("method=").append(method)
                    .append(SPACE_SEPARATOR).append("status=").append(status));
        }
    }

    public void log(Level severityLevel, String component, String method, String status, Object object,
            String infoMessage) {
        StringBuffer logLine = new StringBuffer();
        if (object != null) {
            logger.log(severityLevel,
                    logLine.append(component).append(SPACE_SEPARATOR).append("method=").append(method)
                            .append(SPACE_SEPARATOR).append("status=").append(status).append(SPACE_SEPARATOR)
                            .append("object=").append(object.toString()));
        } else {
            logger.log(severityLevel, logLine.append(component).append(SPACE_SEPARATOR).append("method=").append(method)
                    .append(SPACE_SEPARATOR).append("status=").append(status));
        }
    }

    public void log(Level severityLevel, String component, String method, String status) {
        StringBuffer logLine = new StringBuffer();
        logger.log(severityLevel, logLine.append(component).append(SPACE_SEPARATOR).append("method=").append(method)
                .append(SPACE_SEPARATOR).append("status=").append(status));
    }

    public void log(Level severityLevel, String component, String method, String status, String errorMessage) {
        StringBuffer logLine = new StringBuffer();
        logger.log(severityLevel, logLine.append(component).append(SPACE_SEPARATOR).append("method=").append(method)
                .append(SPACE_SEPARATOR).append("status=").append(status).append(SPACE_SEPARATOR).append(errorMessage));
    }
}
