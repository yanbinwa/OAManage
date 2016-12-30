package com.yanbinwa.OASystem.QueuePersist;

import java.lang.reflect.InvocationTargetException;

import com.yanbinwa.OASystem.Common.JsonPersist;

import net.sf.json.JSONObject;

public class AppendAction<T extends JsonPersist> extends JsonPersist
{
    
    public static final String APPENDACTION_ACTION = "appendAction";
    public static final String APPENDACTION_PAYLOAD = "payLoad";
    
    public enum QueueAction
    {
        POLL, PUSH
    }
    
    QueueAction appendAction;
    T payLoad;
    @SuppressWarnings("rawtypes")
    Class clazz;
    
    public AppendAction()
    {
        
    }
    
    @SuppressWarnings("rawtypes")
    public void setClazz(Class clazz)
    {
        this.clazz = clazz;
    }
    
    @SuppressWarnings("rawtypes")
    public Class getClazz()
    {
        return clazz;
    }
    
    public void setPayLoad(T payLoad)
    {
        this.payLoad = payLoad;
    }

    public T getPayLoad()
    {
        return payLoad;
    }
    
    public void setAppendAction(QueueAction appendAction)
    {
        this.appendAction = appendAction;
    }
    
    public QueueAction getAppendAction()
    {
        return appendAction;
    }
    
    @Override
    public JSONObject getJsonObjectFromObject()
    {
        // TODO Auto-generated method stub
        JSONObject jsonObject = new JSONObject();
        jsonObject.put(APPENDACTION_ACTION, appendAction.ordinal());
        JSONObject jsonObj = payLoad.getJsonObjectFromObject();
        jsonObject.put(APPENDACTION_PAYLOAD, jsonObj);
        return jsonObject;
    }

    @SuppressWarnings("unchecked")
    @Override
    public void setObjectfromJsonObject(JSONObject jsonObject)
    {
        // TODO Auto-generated method stub
        int actionIndex = jsonObject.getInt(APPENDACTION_ACTION);
        this.appendAction = QueueAction.values()[actionIndex];
        JSONObject jsonObj = jsonObject.getJSONObject(APPENDACTION_PAYLOAD);
        try
        {
            this.payLoad = (T)this.clazz.getConstructor().newInstance();
        } 
        catch (InstantiationException | IllegalAccessException | IllegalArgumentException | InvocationTargetException
                | NoSuchMethodException | SecurityException e)
        {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        this.payLoad.setObjectfromJsonObject(jsonObj);
    }
}
