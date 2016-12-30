package com.yanbinwa.OASystem.Service;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;

import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.yanbinwa.OASystem.Cache.Cache;

import net.sf.json.JSONObject;
import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPool;
import redis.clients.jedis.JedisPoolConfig;

@Service("cacheService")
@Transactional
public class CacheServiceImpl implements CacheService
{
    private JedisPool jedisPool = null;
    
    private int redisExpireTime = 0;
    
    @Autowired
    private PropertyService propertyService;
    
    @PostConstruct
    public void init()
    {
        String redisIp = propertyService.getProperty(CacheService.REDIS_IP);
        Integer redisPort = (Integer)propertyService.getProperty(CacheService.REDIS_PORT, Integer.class);
        Integer redisMaxTotal = (Integer)propertyService.getProperty(CacheService.REDIS_MAXTOTAL, Integer.class);
        Integer redisMaxIdle = (Integer)propertyService.getProperty(CacheService.REDIS_MAXIDLE, Integer.class);
        Integer redisMaxWait = (Integer)propertyService.getProperty(CacheService.REDIS_MAXWAIT, Integer.class);
        Boolean redisTestOnBorrow = (Boolean)propertyService.getProperty(CacheService.REDIS_TESTONBORROW, Boolean.class);
        initialPool(redisIp, redisPort, redisMaxTotal, redisMaxIdle, redisMaxWait, redisTestOnBorrow);
        
        redisExpireTime = (Integer)propertyService.getProperty(CacheService.REDIS_EXPIRETIME, Integer.class);
    }
    
    @PreDestroy
    public void destroy()
    {
        closePool();     
    }
    
    /** ------------------ Redis Function ------------------- */
    
    private Jedis getJedisConnection()
    {
        if (jedisPool != null)
        {
            return jedisPool.getResource();
        }
        return null;
    }
    
    private void initialPool(String ip, int port, int maxTotal, int maxIdle, long maxWait, boolean testOnBorrow)
    {
        JedisPoolConfig config = new JedisPoolConfig();
        config.setMaxTotal(maxTotal);
        config.setMaxIdle(maxIdle); 
        config.setMaxWaitMillis(maxWait); 
        config.setTestOnBorrow(testOnBorrow); 
        
        jedisPool = new JedisPool(config, ip, port);
    }
    
    @SuppressWarnings("deprecation")
    private void returnJedisConnection(Jedis redis)
    {
        jedisPool.returnResource(redis);
    }
    
    private void closePool()
    {
        if (jedisPool != null)
        {
            jedisPool.close();
        }
    }
    
    private void setString(Jedis jedis, String key, String value)
    {
        jedis.set(key, value);
        jedis.expire(key, redisExpireTime);
    }
    
    private String getString(Jedis jedis, String key)
    {
        String ret = jedis.get(key);
        if (ret == null)
        {
            return null;
        }
        jedis.expire(key, redisExpireTime);
        return ret;
    }
    
    private void delString(Jedis jedis, String key)
    {
        jedis.del(key);
    }
    
    /** --------------------------------------------------------- */
    
    private void putCacheToRedis(Jedis jedis, String key, String value)
    {
        long timestamp = System.currentTimeMillis();
        Cache cache = new Cache(timestamp, key, value);
        JSONObject cacheObj = JSONObject.fromObject(cache);
        setString(jedis, key, cacheObj.toString());
    }
    
    private String getCacheFromRedis(Jedis jedis, String key)
    {
        String cacheStr = getString(jedis, key);
        JSONObject cacheObj = JSONObject.fromObject(cacheStr);
        Cache cache = (Cache)JSONObject.toBean(cacheObj, Cache.class);
        if (cache == null)
        {
            return null;
        }
        return cache.getValue();
    }

    @Override
    public void putInCache(String key, String value)
    {
        // TODO Auto-generated method stub
        if (key == null || value == null)
        {
            return;
        }
        Jedis jedis = getJedisConnection();
        if (jedis == null)
        {
            return;
        }
        putCacheToRedis(jedis, key, value);
        returnJedisConnection(jedis);
    }

    @Override
    public void putInCache(Map<String, String> keyValues)
    {
        // TODO Auto-generated method stub
        if (keyValues == null)
        {
            return;
        }
        Jedis jedis = getJedisConnection();
        if (jedis == null)
        {
            return;
        }
        for(Map.Entry<String, String> entry : keyValues.entrySet())
        {
            putCacheToRedis(jedis, entry.getKey(), entry.getValue());
        }
        returnJedisConnection(jedis);
    }

    @Override
    public String getFromCache(String key)
    {
        // TODO Auto-generated method stub
        if (key == null)
        {
            return null;
        }
        Jedis jedis = getJedisConnection();
        if (jedis == null)
        {
            return null;
        }
        String ret = getCacheFromRedis(jedis, key);
        returnJedisConnection(jedis);
        return ret;
    }

    @Override
    public Map<String, String> getFromCache(Set<String> keys)
    {
        // TODO Auto-generated method stub
        if (keys == null)
        {
            return null;
        }
        Jedis jedis = getJedisConnection();
        if (jedis == null)
        {
            return null;
        }
        Map<String, String> retMap = new HashMap<String, String>();
        for (String key : keys)
        {
            String ret = getCacheFromRedis(jedis, key);
            retMap.put(key, ret);
        }
        returnJedisConnection(jedis);
        return retMap;
    }

    @Override
    public void delInCache(String key)
    {
        // TODO Auto-generated method stub
        if (key == null)
        {
            return;
        }
        Jedis jedis = getJedisConnection();
        if (jedis == null)
        {
            return;
        }
        delString(jedis, key);
        returnJedisConnection(jedis);
    }

    @Override
    public void delInCache(Set<String> keys)
    {
        // TODO Auto-generated method stub
        if (keys == null)
        {
            return;
        }
        Jedis jedis = getJedisConnection();
        if (jedis == null)
        {
            return;
        }
        for (String key : keys)
        {
            delString(jedis, key);
        }
        returnJedisConnection(jedis);
    }   
}
