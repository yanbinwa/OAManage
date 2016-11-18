package com.yanbinwa.OASystem.Service;

import org.springframework.web.socket.TextMessage;
import org.springframework.web.socket.WebSocketSession;

import com.yanbinwa.OASystem.Session.Session;

public interface MessageServiceSpring
{
    public static final int ONOPEN = 1;
    public static final int ONCLOASE = 2;
    public static final int ONMESSAGE = 3;
    
    public static final String ROUTEKEY = "routeKey";
    public static final String FUNCTIONKEY = "functionKey";
    public static final String URLNAME = "urlName";
    public static final String URLPARAMETER = "urlParameter";
    public static final String PAYLOAD = "payLoad";
    public static final String RESPONSEPAYLOAD = "responsePayLoad";
    public static final String RESPONSECODE = "responseCode";
    
    public static final String ROOT_URL = "RootUrl";
    
    public void handleMessage(WebSocketSession session, TextMessage message, int type);
    public boolean validateSession(Session session);
}
