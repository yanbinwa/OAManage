package com.yanbinwa.OASystem.Service;

public interface PropertyService
{
    public static final String SYSTEM_PROPERTIES_PATH = "/Users/yanbinwa/Documents/workspace/OAManage/src/main/resources/system.properties";
    
    @SuppressWarnings("rawtypes")
    public Object getProperty(String key, Class clazz);
    
    @SuppressWarnings("rawtypes")
    public Object getLocalProperty(String key, Class clazz, Object defaultValue);
    
    public String getLocalProperty(String key, String defaultValue);
    
    public String getProperty(String key);
    
    
    @SuppressWarnings("rawtypes")
    public void setLocalProperty(String key, Object value, Class clazz);
    
    public void setLocalProperty(String key, String value);
}
