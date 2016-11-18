package com.yanbinwa.OASystem.Service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.PropertySource;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service("propertyService")
@PropertySource(value = { "classpath:webSocketRestAPI.properties", "classpath:application.properties" })
@Transactional
public class PropertyServiceImpl implements PropertyService
{

    @Autowired
    private Environment environment;
    
    @SuppressWarnings({ "rawtypes", "unchecked" })
    @Override
    public Object getProperty(String key, Class clazz)
    {
        // TODO Auto-generated method stub
        return environment.getProperty(key, clazz);
    }

    @Override
    public String getProperty(String key)
    {
        // TODO Auto-generated method stub
        return environment.getProperty(key);
    }

}
