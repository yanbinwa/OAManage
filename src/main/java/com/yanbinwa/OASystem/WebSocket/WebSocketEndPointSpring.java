package com.yanbinwa.OASystem.WebSocket;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.socket.CloseStatus;
import org.springframework.web.socket.TextMessage;
import org.springframework.web.socket.WebSocketSession;
import org.springframework.web.socket.handler.TextWebSocketHandler;

import com.yanbinwa.OASystem.Service.MessageServiceSpring;

/**
 * The websocket path is define in WebConfig.java 
 */

public class WebSocketEndPointSpring extends TextWebSocketHandler
{

    @Autowired
    MessageServiceSpring messageServiceSpring;

    @Override
    protected void handleTextMessage(WebSocketSession session, TextMessage message) throws Exception 
    {
        messageServiceSpring.handleMessage(session, message, MessageServiceSpring.ONMESSAGE);
    }
    
    @Override
    public void afterConnectionEstablished(WebSocketSession session) throws Exception 
    {
        messageServiceSpring.handleMessage(session, null, MessageServiceSpring.ONOPEN);
    }
    
    @Override
    public void afterConnectionClosed(WebSocketSession session, CloseStatus status) throws Exception 
    {
        messageServiceSpring.handleMessage(session, null, MessageServiceSpring.ONCLOASE);
    }
}
