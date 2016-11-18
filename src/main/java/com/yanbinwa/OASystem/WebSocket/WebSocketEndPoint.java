package com.yanbinwa.OASystem.WebSocket;

import javax.websocket.OnClose;
import javax.websocket.OnMessage;
import javax.websocket.OnOpen;
import javax.websocket.Session;
import javax.websocket.server.ServerEndpoint;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.socket.server.standard.SpringConfigurator;

import com.yanbinwa.OASystem.Service.MessageService;

@ServerEndpoint(value="/websocket", configurator = SpringConfigurator.class)
public class WebSocketEndPoint
{
    
    private final MessageService messageService;
    
    @Autowired
    public WebSocketEndPoint(MessageService messageService) {
        this.messageService = messageService;
    }
    
    Session session;
    
    @OnOpen
    public void onOpen(Session session)
    {
        this.session = session;
        messageService.handleMessage(session, null, MessageService.ONOPEN);
    }
    
    @OnClose
    public void onClose()
    {
        messageService.handleMessage(this.session, null, MessageService.ONCLOASE);
    }
    
    @OnMessage
    public void onMessage(String message, Session session)
    {
        messageService.handleMessage(session, message, MessageService.ONMESSAGE);
    }
}
