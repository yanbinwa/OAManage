package com.yanbinwa.OASystem.Notification;

import com.yanbinwa.OASystem.Common.JsonPersist;
import com.yanbinwa.OASystem.Message.Message;

import net.sf.json.JSONObject;

public class Notification extends JsonPersist
{
    public static final String NOTIFICATION_ID = "id";
    public static final String NOTIFICATION_EXPIRETIMESTAMP = "expireTimestamp";
    public static final String NOTIFICATION_MESSAGE = "message";
    
    long id = -1;
    long expireTimestamp = -1; 
    Message message;
    
    public Notification()
    {
        
    }
    
    public Notification(Message message)
    {
        this.message = message;
    }
    
    public long getId()
    {
        return id;
    }
    
    public void setId(long id)
    {
        this.id = id;
    }
    
    public long getExpireTimestamp()
    {
        return expireTimestamp;
    }
    
    public void setExpireTimestamp(long expireTimestamp)
    {
        this.expireTimestamp = expireTimestamp;
    }
    
    public Message getMessage()
    {
        return message;
    }
    
    public void setMessage(Message message)
    {
        this.message = message;
    }
    
    public boolean isExpired()
    {
        if (expireTimestamp == -1)
        {
            return false;
        }
        if (expireTimestamp < System.currentTimeMillis())
        {
            return true;
        }
        return false;
    }
    
    public void setExpiredTime(int expiredTime)
    {
        if (expiredTime < 0)
        {
            expireTimestamp = -1;
        }
        else
        {
            expireTimestamp = System.currentTimeMillis() + expiredTime;
        }
    }
    
    @Override
    public boolean equals(Object other)
    {
        if (other == null || !(other instanceof Notification))
        {
            return false;
        }
        Notification otherNotification = (Notification)other;
        if (this.id == otherNotification.id)
        {
            return true;
        }
        return false;
    }
    
    @Override
    public String toString()
    {
        StringBuffer sb = new StringBuffer();
        sb.append("expireTimestamp: " + expireTimestamp).append("; ")
            .append("message: " + message);
        return sb.toString();
    }

    @Override
    public JSONObject getJsonObjectFromObject()
    {
        // TODO Auto-generated method stub
        JSONObject jsonObject = new JSONObject();
        jsonObject.put(NOTIFICATION_ID, id);
        jsonObject.put(NOTIFICATION_EXPIRETIMESTAMP, expireTimestamp);
        JSONObject messageJsonObj = message.getJsonObjectFromObject();
        jsonObject.put(NOTIFICATION_MESSAGE, messageJsonObj);
        return jsonObject;
    }

    @Override
    public void setObjectfromJsonObject(JSONObject jsonObject)
    {
        // TODO Auto-generated method stub
        this.id = jsonObject.getInt(NOTIFICATION_ID);
        this.expireTimestamp = jsonObject.getLong(NOTIFICATION_EXPIRETIMESTAMP);
        JSONObject messageJsonObj = jsonObject.getJSONObject(NOTIFICATION_MESSAGE);
        this.message = new Message();
        this.message.setObjectfromJsonObject(messageJsonObj);
    }
}
