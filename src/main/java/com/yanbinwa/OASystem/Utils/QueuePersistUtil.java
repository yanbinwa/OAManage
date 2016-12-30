package com.yanbinwa.OASystem.Utils;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.List;
import java.util.Queue;

import com.yanbinwa.OASystem.Common.JsonPersist;
import com.yanbinwa.OASystem.QueuePersist.AppendAction;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;


public class QueuePersistUtil
{
    @SuppressWarnings({ "rawtypes", "unchecked" })
    public static void loadQueueSnapshot(Queue queue, Class clazz, String filename)
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
    
    @SuppressWarnings("rawtypes")
    public static void persistQueueSnapshot(Queue<? extends JsonPersist> queue, Class clazz, String filename)
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
    
    @SuppressWarnings("rawtypes")
    public static List<AppendAction<JsonPersist>> loadAppendAction(Class clazz, String filename)
    {
        List<AppendAction<JsonPersist>> appendActionList = new ArrayList<AppendAction<JsonPersist>>();
        File file = new File(filename);
        BufferedReader reader = null;
        try
        {
            reader = new BufferedReader(new FileReader(file));
            String line = null;
            while((line = reader.readLine()) != null)
            {
                AppendAction<JsonPersist> appendAction = new AppendAction<JsonPersist>();
                appendAction.setClazz(clazz);
                JSONObject jsonObject = JSONObject.fromObject(line);
                appendAction.setObjectfromJsonObject(jsonObject);
                appendActionList.add(appendAction);
            }
        }
        catch(Exception e)
        {
            e.printStackTrace();
        }
        finally
        {
            try
            {
                if (reader != null)
                {
                    reader.close();
                }
            }
            catch(Exception e)
            {
                e.printStackTrace();
            }
        }
        return appendActionList;
    }
    
    @SuppressWarnings("rawtypes")
    public static void persistAppendAction(AppendAction appendAction, String filename)
    {
        FileWriter write = null;
        try
        {
            write = new FileWriter(filename, true);
            JSONObject jsonObject = appendAction.getJsonObjectFromObject();
            write.write(jsonObject.toString() + "\n");
        }
        catch(Exception e)
        {
            e.printStackTrace();
        }
        finally
        {
            try
            {
                if (write != null)
                {
                    write.close();
                }
            }
            catch(Exception e)
            {
                e.printStackTrace();
            }
        }
    }
}
