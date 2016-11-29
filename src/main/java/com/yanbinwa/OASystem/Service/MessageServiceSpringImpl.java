package com.yanbinwa.OASystem.Service;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.CopyOnWriteArraySet;
import java.util.concurrent.TimeUnit;

import javax.annotation.PostConstruct;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.socket.TextMessage;
import org.springframework.web.socket.WebSocketSession;

import com.yanbinwa.OASystem.Common.EventListener;
import com.yanbinwa.OASystem.Event.Event;
import com.yanbinwa.OASystem.Message.Message;
import com.yanbinwa.OASystem.Message.Message.MessageHttpMethod;
import com.yanbinwa.OASystem.Model.User;
import com.yanbinwa.OASystem.Model.User.AuthType;
import com.yanbinwa.OASystem.Model.User.UserType;
import com.yanbinwa.OASystem.Session.Session;
import com.yanbinwa.OASystem.Session.Session.SessionType;

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
    private static Map<SessionType, CopyOnWriteArraySet<Session>> sessionTypeToSessionMap = new HashMap<SessionType, CopyOnWriteArraySet<Session>>();
    private static Map<WebSocketSession, Session> webSocketSessionToSessionMap = new HashMap<WebSocketSession, Session>();
    private static final Logger logger = Logger.getLogger(MessageServiceSpringImpl.class);
    
    private BlockingQueue<Message> notifyAdminQueue;
    private BlockingQueue<Message> notifyNormalQueue;
    private ThreadPoolTaskExecutor poolTaskExecutor;
        
    @Autowired
    private PropertyService propertyService;
    
    @Autowired
    private MessageProcessorService messageProcessorService;
    
    @Autowired
    private EventService eventService;
        
    @PostConstruct
    public void init()
    {
        String[] keys = {EventService.MESSAGE_SERVICE_SPRING};
        eventService.register(this, keys);
        CopyOnWriteArraySet<Session> noneAuthorizationSession = new CopyOnWriteArraySet<Session>();
        CopyOnWriteArraySet<Session> adminStoreSession = new CopyOnWriteArraySet<Session>();
        CopyOnWriteArraySet<Session> adminEmployeeSession = new CopyOnWriteArraySet<Session>();
        CopyOnWriteArraySet<Session> normalStoreSession = new CopyOnWriteArraySet<Session>();
        CopyOnWriteArraySet<Session> normalEmployeeSession = new CopyOnWriteArraySet<Session>();
        
        sessionTypeToSessionMap.put(SessionType.NoneAuthorizationSession, noneAuthorizationSession);
        sessionTypeToSessionMap.put(SessionType.AdminStoreSession, adminStoreSession);
        sessionTypeToSessionMap.put(SessionType.AdminEmployeeSession, adminEmployeeSession);
        sessionTypeToSessionMap.put(SessionType.NormalStoreSession, normalStoreSession);
        sessionTypeToSessionMap.put(SessionType.NormalEmployeeSession, normalEmployeeSession);
        
        poolTaskExecutor = new ThreadPoolTaskExecutor();
        poolTaskExecutor.setQueueCapacity((Integer)propertyService.getProperty(QUEUE_CAPACITY, Integer.class));
        poolTaskExecutor.setCorePoolSize((Integer)propertyService.getProperty(CORE_POOL_SIZE, Integer.class));  
        poolTaskExecutor.setMaxPoolSize((Integer)propertyService.getProperty(MAX_POOL_SIZE, Integer.class));  
        poolTaskExecutor.setKeepAliveSeconds((Integer)propertyService.getProperty(KEEP_ALIVE_SECONDS, Integer.class));
        poolTaskExecutor.initialize();
        
        notifyAdminQueue = new ArrayBlockingQueue<Message>((Integer)propertyService.getProperty(NOTIFY_ADMIN_QUEUE_SIZE, Integer.class));
        notifyNormalQueue = new ArrayBlockingQueue<Message>((Integer)propertyService.getProperty(NOTIFY_NORMAL_QUEUE_SIZE, Integer.class));
        
        new Thread(new Runnable() {

            @Override
            public void run()
            {
                // TODO Auto-generated method stub
                notifyUserAdmin();
            }
            
        }).start();
        
        new Thread(new Runnable() {

            @Override
            public void run()
            {
                // TODO Auto-generated method stub
                notifyUserNormal();
            }
            
        }).start();
    }
    
    @Override
    public void handleMessage(WebSocketSession webSocketSession, TextMessage message, int type)
    {
        // TODO Auto-generated method stub
        if (type == MessageServiceSpring.ONOPEN)
        {
            handleMessageOnOpen(webSocketSession);
        }
        else if(type == MessageServiceSpring.ONCLOASE)
        {
            handleMessageOnClose(webSocketSession);
        }
        else if(type == MessageServiceSpring.ONMESSAGE)
        {
            handleMessageOnMessage(webSocketSession, message);
        }
    }

    private void handleMessageOnOpen(WebSocketSession webSocketSession)
    {
        // TODO Auto-generated method stub
        Session session = new Session(webSocketSession);
        session.setSessionType(SessionType.NoneAuthorizationSession);
        sessionTypeToSessionMap.get(SessionType.NoneAuthorizationSession).add(session);
        webSocketSessionToSessionMap.put(webSocketSession, session);
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
            messageProcessorService.enqueueMessage(msg);
        }
        else
        {
            logger.error("Invaliade websocket input");
        }

    }

    private void handleMessageOnClose(WebSocketSession webSocketSession)
    {
        // TODO Auto-generated method stub
        Session session = webSocketSessionToSessionMap.remove(webSocketSession);
        sessionTypeToSessionMap.get(session.getSessionType()).remove(session);
        try
        {
            webSocketSession.close();
        } 
        catch (IOException e)
        {
            // TODO Auto-generated catch block
        }
        logger.info("this is sprint speaking: remove a user");
    }

    @Override
    public boolean validateSession(Session session, Message message)
    {
        // TODO Auto-generated method stub
        if (session == null)
        {
            return false;
        }
        SessionType sessionType = session.getSessionType();
        if (!sessionTypeToSessionMap.get(sessionType).contains(session))
        {
            return false;
        }
        if (sessionType == SessionType.NoneAuthorizationSession)
        {
            if (message.getUrl().contains("/login/"))
            {
                return true;
            }
            else
            {
                return false;
            }
        }
        return true;
    }
    
    private Session getSession(WebSocketSession webSocketSession)
    {
        if(webSocketSession == null)
        {
            return null;
        }
        
        return webSocketSessionToSessionMap.get(webSocketSession);
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

    @Override
    public boolean changeSessionType(Session session, SessionType sourceType, SessionType targetType)
    {
        // TODO Auto-generated method stub
        if (session == null)
        {
            return false;
        }
        boolean ret = sessionTypeToSessionMap.get(sourceType).remove(session);
        if (!ret)
        {
            return false;
        }
        ret = sessionTypeToSessionMap.get(targetType).add(session);
        if (!ret)
        {
            return false;
        }
        session.setSessionType(targetType);
        return true;
    }

    @Override
    public boolean isWhatUrl(String urlName, String url)
    {
        // TODO Auto-generated method stub
        String whatUrl = (String) propertyService.getProperty(urlName, String.class);
        whatUrl = whatUrl.split(":")[1];
        if (url.contains(whatUrl))
        {
            return true;
        }
        return false;
    }

    @Override
    public SessionType getSessionTypeFromUser(User user)
    {
        // TODO Auto-generated method stub
        UserType userType = user.getUserType();
        AuthType authType = user.getAuthType();
        if (userType == UserType.Store && authType == AuthType.Admin)
        {
            return SessionType.AdminStoreSession;
        }
        else if(userType == UserType.Store && authType == AuthType.Normal)
        {
            return SessionType.NormalStoreSession;
        }
        else if(userType == UserType.Employee && authType == AuthType.Admin)
        {
            return SessionType.AdminEmployeeSession;
        }
        else if(userType == UserType.Employee && authType == AuthType.Normal)
        {
            return SessionType.NormalEmployeeSession;
        }
        
        return null;
    }

    @Override
    public boolean notifiyAdminUser(Message message)
    {
        // TODO Auto-generated method stub
        return notifyAdminQueue.add(message);
    }

    @Override
    public boolean notifiyNormalUser(Message message)
    {
        // TODO Auto-generated method stub
        return notifyNormalQueue.add(message);
    }
    
    private void notifyUserAdmin()
    {
        while(true)
        {
            Message message = null;
            try
            {
                message = notifyAdminQueue.poll(1000, TimeUnit.MILLISECONDS);
            } 
            catch (InterruptedException e)
            {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
            if(message == null)
            {
                continue;
            }
            NotifyUserHelper notifyUserHelper = new NotifyUserHelper(message);
            poolTaskExecutor.execute(notifyUserHelper);
        }
    }
    
    private void notifyUserNormal()
    {
        while(true)
        {
            Message message = null;
            try
            {
                message = notifyNormalQueue.poll(1000, TimeUnit.MILLISECONDS);
            } 
            catch (InterruptedException e)
            {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
            if(message == null)
            {
                continue;
            }
            NotifyUserHelper notifyUserHelper = new NotifyUserHelper(message);
            poolTaskExecutor.execute(notifyUserHelper);
        }
    }
    
    class NotifyUserHelper implements Runnable
    {
        
        Message message;
        
        public NotifyUserHelper(Message message)
        {
            this.message = message;
        }
        
        @Override
        public void run()
        {
            // TODO Auto-generated method stub
            
        }
        
    }
    
}
