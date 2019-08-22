package com.shoppingcart.logger;

import org.apache.log4j.Level;

public interface ILoggerUtil {
    public void log(Level severityLevel, String component, String method, String status, Object object);

    public void log(Level severityLevel, String component, String method, String status);

    public void log(Level severityLevel, String component, String method, String status, Object object,
            String infoMessage);

    public void log(Level severityLevel, String component, String method, String status, 
            String errorMessage);
}
