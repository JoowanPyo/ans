package com.gemiso.zodiac.core.helper;

import com.gemiso.zodiac.core.provider.ApplicationContextProvider;
import org.springframework.context.ApplicationContext;

public class BeanHelper {
    public static Object getBean(Class<?> classType) {
        ApplicationContext applicationContext = ApplicationContextProvider.getApplicationContext();
        return applicationContext.getBean(classType);
    }
}
