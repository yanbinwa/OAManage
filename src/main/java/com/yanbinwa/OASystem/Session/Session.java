package com.yanbinwa.OASystem.Session;

import org.springframework.web.socket.WebSocketSession;

import com.yanbinwa.OASystem.Model.User;

/**
 * This class is used to store WebSocketSession and other info
 */

public class Session
{
    
    public enum SessionType
    {
        NoneAuthorizationSession, AdminStoreSession, AdminEmployeeSession, NormalStoreSession, NormalEmployeeSession
    }
    
    WebSocketSession webSocketSession;
    User user;
    SessionType sessionType;
    String sessionId;
    
    public Session(WebSocketSession webSocketSession)
    {
        this.webSocketSession = webSocketSession;
    }
    
    public WebSocketSession getWebSocketSession()
    {
        return this.webSocketSession;
    }
    
    public void setUser(User user)
    {
        this.user = user;
    }
    
    public User getUser()
    {
        return user;
    }
    
    public void setSessionType(SessionType sessionType)
    {
        this.sessionType = sessionType;
    }
    
    public SessionType getSessionType()
    {
        return sessionType;
    }
    
    public void setSessionId(String sessionId)
    {
        this.sessionId = sessionId;
    }
    
    public String getSessionId()
    {
        return sessionId;
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
