package com.yanbinwa.OASystem.Service;

import java.util.Map;
import java.util.Set;

public interface CacheService
{
    public static final String REDIS_IP = "Cache_Redis_Ip";
    public static final String REDIS_PORT = "Cache_Redis_Port";
    public static final String REDIS_MAXTOTAL = "Cache_Redis_MaxTotal";
    public static final String REDIS_MAXIDLE = "Cache_Redis_MaxIdle";
    public static final String REDIS_MAXWAIT = "Cache_Redis_MaxWait";
    public static final String REDIS_TESTONBORROW = "Cache_Redis_TestOnBorrow";
    public static final String REDIS_EXPIRETIME = "Cache_Redis_ExpireTime";
    
    void putInCache(String key, String value);
    void putInCache(Map<String, String> keyValues);
    
    String getFromCache(String key);
    Map<String, String> getFromCache(Set<String> keys);
    
    void delInCache(String key);
    void delInCache(Set<String> keys);
}
