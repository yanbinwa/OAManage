package com.yanbinwa.OASystem.Utils;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.util.Queue;

import com.yanbinwa.OASystem.Common.JsonPersist;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;


public class QueuePersistUtil
{
    @SuppressWarnings({ "rawtypes", "unchecked" })
    public static void loadQueue(Queue queue, Class clazz, String filename)
    {
        if (queue == null)
        {
            return;
        }
        
        if (!JsonPersist.class.isAssignableFrom(clazz))
        {
            return;
        }
        String ret = "";
        try
        {
            FileReader read = new FileReader(filename);
            BufferedReader bufferedReader = new BufferedReader(read);
            String line = null;
            while((line = bufferedReader.readLine()) != null)
            {
                ret += line;
            }
            read.close();
            if (ret == "")
            {
                return;
            }
            JSONArray jsonArray = JSONArray.fromObject(ret);
            for(int i = 0; i < jsonArray.size(); i ++)
            {
                JSONObject jsonObject = jsonArray.getJSONObject(i);
                Constructor cons = clazz.getDeclaredConstructor();
                JsonPersist obj = (JsonPersist) cons.newInstance();
                obj.setObjectfromJsonObject(jsonObject);
                queue.add(obj);
            }      
        } 
        catch (IOException | NoSuchMethodException | SecurityException | InstantiationException | IllegalAccessException | IllegalArgumentException | InvocationTargetException e)
        {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }
    
    @SuppressWarnings({ "rawtypes" })
    public static void persistQueue(Queue queue, Class clazz, String filename)
    {
        if (queue == null)
        {
            return;
        }
        if (!JsonPersist.class.isAssignableFrom(clazz))
        {
            return;
        }
        try
        {
            JSONArray array = new JSONArray();
            for(Object obj : queue.toArray())
            {
                JsonPersist jsonPersist = (JsonPersist)obj;
                JSONObject jsonObject = jsonPersist.getJsonObjectFromObject();
                array.add(jsonObject);
            }
            FileWriter writer = new FileWriter(filename);
            writer.write(array.toString());
            writer.close();
        } 
        catch (IOException e)
        {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        
    }
}
