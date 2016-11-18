package com.yanbinwa.OASystem.Service;

public interface PropertyService
{
    @SuppressWarnings("rawtypes")
    public Object getProperty(String key, Class clazz);
    
    public String getProperty(String key);
}
