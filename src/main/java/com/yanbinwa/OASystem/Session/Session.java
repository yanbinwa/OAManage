package com.yanbinwa.OASystem.Session;

import org.springframework.web.socket.WebSocketSession;

/**
 * This class is used to store WebSocketSession and other info
 */

public class Session
{
    
    WebSocketSession webSocketSession;
    
    public Session(WebSocketSession webSocketSession)
    {
        this.webSocketSession = webSocketSession;
    }
    
    public WebSocketSession getWebSocketSession()
    {
        return this.webSocketSession;
    }
    
    @Override
    public boolean equals(Object obj) 
    {
        if(this == obj)
        {
            return true;
        }
        if(obj == null)
        {
            return false;
        }
        if(!(obj instanceof Session))
        {
            return false;
        }
        Session session = (Session)obj;
        if (this.webSocketSession == session.webSocketSession)
        {
            return true;
        }
        else
        {
            return false;
        }
    }
}
