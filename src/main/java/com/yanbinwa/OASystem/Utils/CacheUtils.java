package com.yanbinwa.OASystem.Utils;

import net.sf.json.JSONObject;

public class CacheUtils
{
    @SuppressWarnings("rawtypes")
    public static Object convertObjectStrToObject(String objStr, Class clazz)
    {
        if (objStr == null)
        {
            return null;
        }
        JSONObject jsonObj = JSONObject.fromObject(objStr);
        return JSONObject.toBean(jsonObj, clazz);
    }
    
    public static String convertObjectToObjectStr(Object object)
    {
        if (object == null)
        {
            return null;
        }
        JSONObject jsonObj = JSONObject.fromObject(object);
        return jsonObj.toString();
    }
}
