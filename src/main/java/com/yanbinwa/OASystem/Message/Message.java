package com.yanbinwa.OASystem.Message;

import com.yanbinwa.OASystem.Common.JsonPersist;
import com.yanbinwa.OASystem.Service.MessageServiceSpring;
import com.yanbinwa.OASystem.Session.Session;
import com.yanbinwa.OASystem.Utils.HttpUtils;

import net.sf.json.JSONObject;

public class Message implements JsonPersist
{
    public enum MessageHttpMethod 
    {
        GET, POST, DEL, WEBSOCKET
    }
    
    public static final String METHOD_GET_KEY = "GET";
    public static final String METHOD_POST_KEY = "POST";
    public static final String METHOD_DEL_KEY = "DEL";
    public static final String METHOD_WEBSOCKET = "WEBSOCKET";
    
    String routeKey;
    String functionKey;
    String url;
    String urlParameter;
    String requestPayLoad;
    String responsePayLoad;
    int responseCode;
    MessageHttpMethod method;
    Session session;
    
    public Message()
    {
        
    }
    
    public Message(Session session, String routeKey, String functionKey, String url, String urlParameter, String requestPayLoad, MessageHttpMethod method)
    {
        this.routeKey = routeKey;
        this.functionKey = functionKey;
        this.url = url;
        this.urlParameter = urlParameter;
        this.requestPayLoad = requestPayLoad;
        this.responsePayLoad = "";
        this.method = method;
        this.session = session;
        this.responseCode = HttpUtils.RESPONSE_ERROR;
    }
    
    public String getUrl()
    {
        return this.url;
    }
    
    public void setUrl(String url)
    {
        this.url = url;
    }
    
    public String getUrlParameter()
    {
        return this.urlParameter;
    }
    
    public void setUrlParameter(String urlParameter)
    {
        this.urlParameter = urlParameter;
    }
    
    public String getRequestPayLoad()
    {
        return this.requestPayLoad;
    }
    
    public void setRequestPayLoad(String requestPayLoad)
    {
        this.requestPayLoad = requestPayLoad;
    }
    
    public Session getSession()
    {
        return this.session;
    }
    
    public void setSession(Session session)
    {
        this.session = session;
    }
    
    public MessageHttpMethod getMethod()
    {
        return this.method;
    }
    
    public void setMethod(MessageHttpMethod method)
    {
        this.method = method;
    }
    
    public String getResponsePayLoad()
    {
        return this.responsePayLoad;
    }
    
    public void setResponsePayLoad(String responsePayLoad)
    {
        this.responsePayLoad = responsePayLoad;
    }
    
    public String getRouteKey()
    {
        return this.routeKey;
    }
    
    public void setRouteKey(String routeKey)
    {
        this.routeKey = routeKey;
    }
    
    public String getFunctionKey()
    {
        return this.functionKey;
    }
    
    public void setFunctionKey(String functionKey)
    {
        this.functionKey = functionKey;
    }
    
    public int getResponseCode()
    {
        return this.responseCode;
    }
    
    public void setResponseCode(int responseCode)
    {
        this.responseCode = responseCode;
    }
    
    public String getResponseJsonStr()
    {
        JSONObject ob = new JSONObject();
        ob.put(MessageServiceSpring.ROUTEKEY, routeKey);
        ob.put(MessageServiceSpring.FUNCTIONKEY, functionKey);
        ob.put(MessageServiceSpring.RESPONSEPAYLOAD, responsePayLoad);
        ob.put(MessageServiceSpring.RESPONSECODE, responseCode);
        return ob.toString();
    }
    
    @Override
    public String toString()
    {
        StringBuffer sb = new StringBuffer();
        sb.append("url: " + url).append("; ")
            .append("urlParameter: " + urlParameter).append("; ")
            .append("requestPayLoad: " + requestPayLoad).append("; ")
            .append("responsePayLoad: " + responsePayLoad).append("; ")
            .append("responseCode: " + responseCode).append("; ")
            .append("method: " + method);
        return sb.toString();
    }

    @Override
    public JSONObject getJsonObjectFromObject()
    {
        // TODO Auto-generated method stub
        JSONObject jsonObject = new JSONObject();
        jsonObject.put(MessageServiceSpring.ROUTEKEY, routeKey);
        jsonObject.put(MessageServiceSpring.FUNCTIONKEY, functionKey);
        jsonObject.put(MessageServiceSpring.RESPONSEPAYLOAD, responsePayLoad);
        jsonObject.put(MessageServiceSpring.RESPONSECODE, responseCode);
        return jsonObject;
    }

    @Override
    public void setObjectfromJsonObject(JSONObject jsonObject)
    {
        // TODO Auto-generated method stub
        this.routeKey = jsonObject.getString(MessageServiceSpring.ROUTEKEY);
        this.functionKey = jsonObject.getString(MessageServiceSpring.FUNCTIONKEY);
        this.responsePayLoad = jsonObject.getString(MessageServiceSpring.RESPONSEPAYLOAD);
        this.responseCode = jsonObject.getInt(MessageServiceSpring.RESPONSECODE);
    }
}
