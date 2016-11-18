package com.yanbinwa.OASystem.WebSocketClient;

import java.net.URI;
import java.net.URISyntaxException;

import javax.websocket.ClientEndpoint;

import com.yanbinwa.OASystem.Model.Employee;
import com.yanbinwa.OASystem.Service.MessageServiceSpring;

import net.sf.json.JSONObject;

@ClientEndpoint
public class WebSocketClient
{
    
    public static WebSocketClientEndPoint getWebSocketClientEndPoint(String urlStr)
    {
        if (urlStr == null)
        {
            return null;
        }
        try
        {
            URI url = new URI(urlStr);
            WebSocketClientEndPoint clientEndPoint = new WebSocketClientEndPoint(url);
            clientEndPoint.addMessageHandler(new WebSocketClientEndPoint.MessageHandler() 
            {
                public void handleMessage(String message) 
                {
                    System.out.println(message);
                }
            });
            return clientEndPoint;
        } 
        catch (URISyntaxException e)
        {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        return null;
    }
    
    public static void newEmployee(WebSocketClientEndPoint clientEndPoint, Employee employee)
    {
        if(employee == null || clientEndPoint == null)
        {
            return;
        }
        JSONObject jsObj = new JSONObject();
        addRouteKeyAndFunctionKey(jsObj);
        jsObj.put(MessageServiceSpring.URLNAME, "CreateEmployee");
        JSONObject employeeJsonObj = JSONObject.fromObject(employee);
        jsObj.put(MessageServiceSpring.PAYLOAD, employeeJsonObj.toString());
        jsObj.put(MessageServiceSpring.URLPARAMETER, "");
        clientEndPoint.sendMessage(jsObj.toString());
    }
    
    public static void listAllEmployee(WebSocketClientEndPoint clientEndPoint)
    {
        if(clientEndPoint == null)
        {
            return;
        }
        JSONObject jsObj = new JSONObject();
        addRouteKeyAndFunctionKey(jsObj);
        jsObj.put(MessageServiceSpring.URLNAME, "GetAllEmployee");
        jsObj.put(MessageServiceSpring.PAYLOAD, null);
        jsObj.put(MessageServiceSpring.URLPARAMETER, "");
        clientEndPoint.sendMessage(jsObj.toString());
    }
    
    public static void findEmployeeById(WebSocketClientEndPoint clientEndPoint, int id)
    {
        if(clientEndPoint == null)
        {
            return;
        }
        JSONObject jsObj = new JSONObject();
        addRouteKeyAndFunctionKey(jsObj);
        jsObj.put(MessageServiceSpring.URLNAME, "GetEmployeeById");
        jsObj.put(MessageServiceSpring.PAYLOAD, null);
        jsObj.put(MessageServiceSpring.URLPARAMETER, id);
        clientEndPoint.sendMessage(jsObj.toString());
    }
    
    public static void deleteEmployee(WebSocketClientEndPoint clientEndPoint, Employee employee)
    {
        if(employee == null || clientEndPoint == null)
        {
            return;
        }
        JSONObject jsObj = new JSONObject();
        addRouteKeyAndFunctionKey(jsObj);
        jsObj.put(MessageServiceSpring.URLNAME, "DeleteEmployee");
        JSONObject employeeJsonObj = JSONObject.fromObject(employee);
        jsObj.put(MessageServiceSpring.PAYLOAD, employeeJsonObj.toString());
        jsObj.put(MessageServiceSpring.URLPARAMETER, "");
        clientEndPoint.sendMessage(jsObj.toString());
    }
    
    public static void addRouteKeyAndFunctionKey(JSONObject obj)
    {
        obj.put(MessageServiceSpring.ROUTEKEY, "Test");
        obj.put(MessageServiceSpring.FUNCTIONKEY, "Test");
    }
    
    public static void closeWebSocketClientEndPoint(WebSocketClientEndPoint clientEndPoint)
    {
        clientEndPoint.closeSession();
    }
    
    public static void main(String[] args) 
    {
        //String urlStr = "ws://localhost:8080/OAManage/websocket/websocketSpring";
        String urlStr = "wss://localhost:8443/OAManage/websocket/websocketSpring";
        WebSocketClientEndPoint clientEndPoint = WebSocketClient.getWebSocketClientEndPoint(urlStr);
//        Employee employee = new Employee(6, "wzy", "13073546736");
//        WebSocketClient.newEmployee(clientEndPoint, employee);
        WebSocketClient.listAllEmployee(clientEndPoint);
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
