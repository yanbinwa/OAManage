package com.yanbinwa.OASystem.Utils;

import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;

public class SpringBeanFactoryUtils
{
    
    private static ApplicationContext appCtx;
    
    public void setApplicationContext(ApplicationContext applicationContext) throws BeansException 
    {
        appCtx = applicationContext;
    }

    public static ApplicationContext getApplicationContext() 
    {
        return appCtx;
    }

    public static Object getBean(String beanName) 
    {
        return appCtx.getBean(beanName);
    }
}
