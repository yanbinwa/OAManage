package com.yanbinwa.OASystem.WebSocketClient;

import com.yanbinwa.OASystem.Service.MessageServiceSpring;

import net.sf.json.JSONObject;

public class WebSocketClientForORCode
{
    public static void getORCode(WebSocketClientEndPoint clientEndPoint)
    {
        JSONObject jsObj = new JSONObject();
        jsObj.put(MessageServiceSpring.URLNAME, "GetORCodeKey");
        jsObj.put(MessageServiceSpring.PAYLOAD, null);
        jsObj.put(MessageServiceSpring.URLPARAMETER, "");
        clientEndPoint.sendMessage(jsObj.toString());
    }
    
    public static void main(String[] args) 
    {
        String urlStr = "ws://localhost:8080/OAManage/websocket/websocketSpring";
        WebSocketClientEndPoint clientEndPoint = WebSocketClient.getWebSocketClientEndPoint(urlStr);
        WebSocketClientForORCode.getORCode(clientEndPoint);
        try
        {
            Thread.sleep(500000);
        } 
        catch (InterruptedException e)
        {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }
}
