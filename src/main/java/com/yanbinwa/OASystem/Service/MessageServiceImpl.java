package com.yanbinwa.OASystem.Service;

import java.io.IOException;
import java.util.concurrent.CopyOnWriteArraySet;

import javax.websocket.Session;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service("messageService")
@Transactional
public class MessageServiceImpl implements MessageService
{
    
    private static CopyOnWriteArraySet<Session> webSocketSessionSet = new CopyOnWriteArraySet<Session>();

    @Override
    public void handleMessage(Session session, String message, int type)
    {
        // TODO Auto-generated method stub
        if (type == MessageService.ONOPEN)
        {
            handleMessageOnOpen(session);
        }
        else if(type == MessageService.ONCLOASE)
        {
            handleMessageOnClose(session);
        }
        else if(type == MessageService.ONMESSAGE)
        {
            handleMessageOnMessage(session, message);
        }
    }
    
    protected void handleMessageOnOpen(Session session)
    {
        webSocketSessionSet.add(session);
        System.out.println("some one login");
    }
    
    protected void handleMessageOnClose(Session session)
    {
        webSocketSessionSet.remove(session);
        System.out.println("some one logout");
    }
    
    protected void handleMessageOnMessage(Session session, String message)
    {
        System.out.println("some one input a message");
        for(Session s : webSocketSessionSet)
        {
            if (s != session)
            {
                try
                {
                    s.getBasicRemote().sendText(message);
                } 
                catch (IOException e)
                {
                    // TODO Auto-generated catch block
                    e.printStackTrace();
                }
            }
        }
    }
}
