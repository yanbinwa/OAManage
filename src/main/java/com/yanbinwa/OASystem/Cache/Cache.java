package com.yanbinwa.OASystem.Cache;

public class Cache
{
    private long timestamp;
    private String key;
    private String value;
    
    public Cache()
    {
        
    }
    
    public Cache(long timestamp, String key, String value)
    {
        this.timestamp = timestamp;
        this.key = key;
        this.value = value;
    }
    
    public long getTimestamp()
    {
        return timestamp;
    }
    
    public void setTimestamp(long timestamp)
    {
        this.timestamp = timestamp;
    }
    
    public String getKey()
    {
        return key;
    }
    
    public void setKey(String key)
    {
        this.key = key;
    }
    
    public String getValue()
    {
        return value;
    }
    
    public void setValue(String value)
    {
        this.value = value;
    }
}
