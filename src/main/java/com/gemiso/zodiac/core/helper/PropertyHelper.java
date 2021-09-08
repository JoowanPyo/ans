package com.gemiso.zodiac.core.helper;

import com.gemiso.zodiac.core.provider.ApplicationContextProvider;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.ApplicationContext;

@Slf4j
public class PropertyHelper {
    public static String getProperty(String propertyName) {
        return getProperty(propertyName, null);
    }

    public static String getProperty(String propertyName, String defaultValue) {
        String value = defaultValue;
        ApplicationContext applicationContext = ApplicationContextProvider.getApplicationContext();
        if(applicationContext.getEnvironment().getProperty(propertyName) == null) {
            log.warn(propertyName + " properties was not loaded.");
        } else {
            value = applicationContext.getEnvironment().getProperty(propertyName).toString();
        }
        return value;
    }

}