package com.yanbinwa.OASystem.Service;

public interface PropertyService
{
    public static final String SYSTEM_RESOURCE_DIR = "/Users/yanbinwa/Documents/workspace/OAManage/src/main/resources";
    
    public static final String SYSTEM_PROPERTIES_PATH = SYSTEM_RESOURCE_DIR + "/" + "system.properties";
    
    public static final String RESPONSE_JSON_UTF8 = "application/json;charset=UTF-8";
    
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
