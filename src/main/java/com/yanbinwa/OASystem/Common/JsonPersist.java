package com.yanbinwa.OASystem.Common;

import net.sf.json.JSONObject;

public interface JsonPersist
{
    public JSONObject getJsonObjectFromObject();
    
    public void setObjectfromJsonObject(JSONObject jsonObject);
}
