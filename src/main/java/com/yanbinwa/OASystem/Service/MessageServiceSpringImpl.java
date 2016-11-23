package com.yanbinwa.OASystem.Service;

import java.util.concurrent.CopyOnWriteArraySet;

import javax.annotation.PostConstruct;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.socket.TextMessage;
import org.springframework.web.socket.WebSocketSession;

import com.yanbinwa.OASystem.Common.EventListener;
import com.yanbinwa.OASystem.Event.Event;
import com.yanbinwa.OASystem.Message.Message;
import com.yanbinwa.OASystem.Message.Message.MessageHttpMethod;
import com.yanbinwa.OASystem.Session.Session;

import net.sf.json.JSONObject;

/**
 * Manage the session, operate the message to get rest result
 * @author yanbinwa
 *
 */

@Service("messageServiceSpring")
@Transactional
public class MessageServiceSpringImpl implements MessageServiceSpring, EventListener
{
    private static CopyOnWriteArraySet<Session> webSocketSessionSet = new CopyOnWriteArraySet<Session>();
    private static final Logger logger = Logger.getLogger(MessageServiceSpringImpl.class);
        
    @Autowired
    private PropertyService propertyService;
    
    @Autowired
    private MessageProcessorService messageProcessorService;
    
    @Autowired
    private EventService eventService;
        
    @PostConstruct
    public void init()
    {
        String[] keys = {"MessageServiceSpring"};
        eventService.register(this, keys);
    }
    
    @Override
    public void handleMessage(WebSocketSession session, TextMessage message, int type)
    {
        // TODO Auto-generated method stub
        if (type == MessageServiceSpring.ONOPEN)
        {
            handleMessageOnOpen(session);
        }
        else if(type == MessageServiceSpring.ONCLOASE)
        {
            handleMessageOnClose(session);
        }
        else if(type == MessageServiceSpring.ONMESSAGE)
        {
            handleMessageOnMessage(session, message);
        }
    }

    private void handleMessageOnOpen(WebSocketSession session)
    {
        // TODO Auto-generated method stub
        webSocketSessionSet.add(new Session(session));
        logger.info("this is sprint speaking: add a user");
    }
    
    private void handleMessageOnMessage(WebSocketSession session, TextMessage message)
    {
        // TODO Auto-generated method stub
        logger.info("this is sprint speaking: receive a message " + message.getPayload());
        Session s = getSession(session);
        Message msg = createMessage(s, message.getPayload());
        if (msg != null)
        {
            messageProcessorService.enqueue(msg);
        }
        else
        {
            
        }

    }

    private void handleMessageOnClose(WebSocketSession session)
    {
        // TODO Auto-generated method stub
        webSocketSessionSet.remove(new Session(session));
        logger.info("this is sprint speaking: remove a user");
    }

    @Override
    public boolean validateSession(Session session)
    {
        // TODO Auto-generated method stub
        if (session == null)
        {
            return false;
        }
        if (webSocketSessionSet.contains(session))
        {
            return true;
        }
        return false;
    }
    
    private Session getSession(WebSocketSession webSocketSession)
    {
        if(webSocketSession == null)
        {
            return null;
        }
        for(Session session : webSocketSessionSet)
        {
            if(session.getWebSocketSession() == webSocketSession)
            {
                return session;
            }
        }
        return null;
    }
    
    private Message createMessage(Session session, String payLoad)
    {
        JSONObject jsObj = JSONObject.fromObject(payLoad);
        Object ob = jsObj.get(ROUTEKEY);
        if(ob == null || ob.toString() == "null")
        {
            logger.error("Have no " + ROUTEKEY);
            return null;
        }
        String routeKey = (String)ob;
        
        ob = jsObj.get(FUNCTIONKEY);
        if(ob == null || ob.toString() == "null")
        {
            logger.error("Have no " + FUNCTIONKEY);
            return null;
        }
        String functionKey = (String)ob;
        
        ob = jsObj.get(URLNAME);
        if(ob == null || ob.toString() == "null")
        {
            logger.error("Have no " + URLNAME);
            return null;
        }
        String urlName = (String)ob;
        String urlStr = (String)propertyService.getProperty(urlName, String.class);
        if (urlStr == null || ob.toString() == "null")
        {
            logger.error("Have no url keyPair");
            return null;
        }
        String urlRoot = (String)propertyService.getProperty(ROOT_URL, String.class);
        String[] urlEntry = urlStr.split(":");
        if (urlEntry.length != 2)
        {
            logger.error("Invalidated url string " + urlStr);
            return null;
        }
        MessageHttpMethod method =  getMessageHttpMethod(urlEntry[0].trim());
        if (method == null)
        {
            logger.error("Invalidated http method " + urlEntry[0].trim());
            return null;
        }
        String url = urlRoot + urlEntry[1].trim();
        
        ob = jsObj.get(URLPARAMETER);
        String urlParameter = "";
        if(ob != null && ob.toString() != "null")
        {
            urlParameter = "/" + (String)ob.toString();
        }
        
        ob = jsObj.get(PAYLOAD);
        String pd = "";
        if(ob != null && ob.toString() != "null")
        {
            pd = (String)ob.toString();
        }
        Message message = new Message(session, routeKey, functionKey, url, urlParameter, pd, method);
        logger.info("Create a message: " + message);
        return message;
    }
    
    private MessageHttpMethod getMessageHttpMethod(String method)
    {
        if (method == null || method.trim() == "")
        {
            return null;
        }
        switch(method)
        {
        case Message.METHOD_GET_KEY:
            return MessageHttpMethod.GET;
        case Message.METHOD_POST_KEY:
            return MessageHttpMethod.POST;
        case Message.METHOD_DEL_KEY:
            return MessageHttpMethod.DEL;
        default:
            return null;
        }
    }

    @Override
    public void callBack(Event event)
    {
        // TODO Auto-generated method stub
        logger.info(event);
    }
    
}
