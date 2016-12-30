package com.yanbinwa.OASystem.Common;

import net.sf.json.JSONObject;

public abstract class JsonPersist
{
    public abstract JSONObject getJsonObjectFromObject();
    
    public abstract void setObjectfromJsonObject(JSONObject jsonObject);
}
