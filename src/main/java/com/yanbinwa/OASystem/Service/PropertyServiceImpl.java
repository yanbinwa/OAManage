package com.yanbinwa.OASystem.Service;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.Properties;

import javax.annotation.PostConstruct;

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
        
    Properties properties; 
    
    @PostConstruct
    public void init()
    {
        loadLocalProperties();
    }
    
    private void loadLocalProperties()
    {
        if (properties == null)
        {
            properties = new Properties();
        }
        File file = new File(SYSTEM_PROPERTIES_PATH);
        InputStream is = null;
        try
        {
            is = new FileInputStream(file);
            properties.load(is);
        } 
        catch (IOException e)
        {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        finally
        {
            if (is != null)
            {
                try
                {
                    is.close();
                } 
                catch (IOException e)
                {
                    // TODO Auto-generated catch block
                    e.printStackTrace();
                }
            }
        }
    }
    
    private void saveLocalProperties()
    {
        if (properties == null)
        {
            return;
        }
        File file = new File(SYSTEM_PROPERTIES_PATH);
        OutputStream os = null;
        try
        {
            os = new FileOutputStream(file);
            properties.store(os, null);
        } 
        catch (FileNotFoundException e)
        {
            // TODO Auto-generated catch block
            e.printStackTrace();
        } 
        catch (IOException e)
        {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        finally
        {
            if (os != null)
            {
                try
                {
                    os.close();
                } 
                catch (IOException e)
                {
                    // TODO Auto-generated catch block
                    e.printStackTrace();
                }
            }
        }
    }
    
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

    @SuppressWarnings("rawtypes")
    @Override
    public Object getLocalProperty(String key, Class clazz, Object defaultValue)
    {
        // TODO Auto-generated method stub
        if (properties == null)
        {
            return null;
        }
        String valueStr = properties.getProperty(key);
        Object value = null;
        if (clazz == Integer.class)
        {
            value = Integer.parseInt(valueStr);
        }
        else if(clazz == Long.class)
        {
            value = Long.parseLong(valueStr);
        }
        else if(clazz == String.class)
        {
            value = valueStr;
        }
        if (value == null)
        {
            return defaultValue;
        }
        return value;
    }

    @Override
    public String getLocalProperty(String key, String defaultValue)
    {
        // TODO Auto-generated method stub
        if (properties == null)
        {
            return null;
        }
        String value = properties.getProperty(key);
        if (value == null)
        {
            return defaultValue;
        }
        return value;
    }

    @SuppressWarnings("rawtypes")
    @Override
    public void setLocalProperty(String key, Object value, Class clazz)
    {
        // TODO Auto-generated method stub
        if (value == null)
        {
            return;
        }
        if (properties == null)
        {
            properties = new Properties();
        }
        Object oldValue = getLocalProperty(key, clazz, null);
        if (value.equals(oldValue))
        {
            return;
        }
        properties.setProperty(key, value.toString());
        saveLocalProperties();
    }

    @Override
    public void setLocalProperty(String key, String value)
    {
        // TODO Auto-generated method stub
        if (value == null)
        {
            return;
        }
        if (properties == null)
        {
            properties = new Properties();
        }
        String oldValue = getLocalProperty(key, null);
        if (value.equals(oldValue))
        {
            return;
        }
        properties.setProperty(key, value);
        saveLocalProperties();
    }

}
