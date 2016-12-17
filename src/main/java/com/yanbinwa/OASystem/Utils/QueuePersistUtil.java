package com.yanbinwa.OASystem.Utils;

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
        try
        {
            String ret = FileUtils.readJsonFile(filename);
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
        catch (NoSuchMethodException | SecurityException | InstantiationException | IllegalAccessException | IllegalArgumentException | InvocationTargetException e)
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
        JSONArray array = new JSONArray();
        for(Object obj : queue.toArray())
        {
            JsonPersist jsonPersist = (JsonPersist)obj;
            JSONObject jsonObject = jsonPersist.getJsonObjectFromObject();
            array.add(jsonObject);
        }
        FileUtils.writeFile(filename, array.toString());
    }
}
