package com.yanbinwa.OASystem.Message;

import com.yanbinwa.OASystem.Service.MessageServiceSpring;
import com.yanbinwa.OASystem.Session.Session;
import com.yanbinwa.OASystem.Utils.HttpUtils;

import net.sf.json.JSONObject;

public class Message
{
    public enum MessageHttpMethod 
    {
        GET, POST, DEL
    }
    
    public static final String METHOD_GET_KEY = "GET";
    public static final String METHOD_POST_KEY = "POST";
    public static final String METHOD_DEL_KEY = "DEL";
    
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
    
    public String getUrlParameter()
    {
        return this.urlParameter;
    }
    
    public String getRequestPayLoad()
    {
        return this.requestPayLoad;
    }
    
    public Session getSession()
    {
        return this.session;
    }
    
    public MessageHttpMethod getMethod()
    {
        return this.method;
    }
    
    public String getResponsePayLoad()
    {
        return this.responsePayLoad;
    }
    
    public void setRouteKey(String routeKey)
    {
        this.routeKey = routeKey;
    }
    
    public void setFunctionKey(String functionKey)
    {
        this.functionKey = functionKey;
    }
    
    public void setResponsePayLoad(String responsePayLoad)
    {
        this.responsePayLoad = responsePayLoad;
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
}
