package com.yanbinwa.OASystem.Service;

import java.io.IOException;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.CopyOnWriteArraySet;
import java.util.concurrent.TimeUnit;

import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;

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
import com.yanbinwa.OASystem.Utils.QueuePersistUtil;

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
    private static Map<Long, Set<String>> agingTimeToSessionIdMap = new HashMap<Long, Set<String>>();
    private static Map<String, Long> sessionIdToAgingTimeMap = new HashMap<String, Long>();
    private static Map<String, Session> onLineSessionIdMap = new HashMap<String, Session>();
    private static Map<String, User> sessionIdToUserMap = new HashMap<String, User>();
    
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
    
    @Autowired
    private WebsocketMessageProcessorService websocketMessageProcessorService;
        
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
        QueuePersistUtil.loadQueue(notifyAdminQueue, Message.class, MessageServiceSpring.NOTIFY_ADMIN_QUEUE_FILENAME);
        notifyNormalQueue = new ArrayBlockingQueue<Message>((Integer)propertyService.getProperty(NOTIFY_NORMAL_QUEUE_SIZE, Integer.class));
        QueuePersistUtil.loadQueue(notifyNormalQueue, Message.class, MessageServiceSpring.NOTIFY_NORMAL_QUEUE_FILENAME);
        
        new Thread(new Runnable() {

            @Override
            public void run()
            {
                // TODO Auto-generated method stub
                notifyAdminUser();
            }
            
        }).start();
        
        new Thread(new Runnable() {

            @Override
            public void run()
            {
                // TODO Auto-generated method stub
                notifyNormalUser();
            }
            
        }).start();
        
        new Thread(new Runnable() {

            @Override
            public void run()
            {
                // TODO Auto-generated method stub
                agingSessionId();
            }
            
        }).start();
    }
    
    @PreDestroy
    public void destroy()
    {
        QueuePersistUtil.persistQueue(notifyAdminQueue, Message.class, NOTIFY_ADMIN_QUEUE_FILENAME);
        QueuePersistUtil.persistQueue(notifyNormalQueue, Message.class, NOTIFY_NORMAL_QUEUE_FILENAME);
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
        createSession(webSocketSession);
    }
    
    private void handleMessageOnMessage(WebSocketSession session, TextMessage message)
    {
        // TODO Auto-generated method stub
        logger.info("this is sprint speaking: receive a message " + message.getPayload());
        Session s = getSession(session);
        Message msg = createMessage(s, message.getPayload());
        
        if (msg != null)
        {
            if (msg.getMethod() == Message.MessageHttpMethod.WEBSOCKET)
            {
                websocketMessageProcessorService.enqueueMessage(msg);
            }
            else
            {
                messageProcessorService.enqueueMessage(msg);
            }
        }
        else
        {
            logger.error("Invaliade websocket input");
        }

    }

    private void handleMessageOnClose(WebSocketSession webSocketSession)
    {
        // TODO Auto-generated method stub
        closeSession(webSocketSession);
    }
    
    private boolean isSessionLogin(Session session)
    {
        return !(session.getSessionType() == SessionType.NoneAuthorizationSession);
    }
    
    @Override
    public boolean isSessionIdOnLine(String sessionId)
    {
        // TODO Auto-generated method stub
        return onLineSessionIdMap.containsKey(sessionId);
    }

    @Override
    public void closeSession(WebSocketSession webSocketSession)
    {
        // TODO Auto-generated method stub
        Session session = webSocketSessionToSessionMap.remove(webSocketSession);
        sessionTypeToSessionMap.get(session.getSessionType()).remove(session); 
        onLineSessionIdMap.remove(session.getSessionId());
        
        if (isSessionLogin(session))
        {
            Long agingTime = System.currentTimeMillis() + (Long)propertyService.getProperty(NOTIFY_SESSION_AGE_TIME, Long.class);
            String sessionId = session.getSessionId();
            Set<String> sessionIdList = agingTimeToSessionIdMap.get(agingTime);
            if (sessionIdList == null)
            {
                sessionIdList = new HashSet<String>();
                agingTimeToSessionIdMap.put(agingTime, sessionIdList);
            }
            sessionIdList.add(sessionId);
            sessionIdToAgingTimeMap.put(sessionId, agingTime);
        }
        
        try
        {
            webSocketSession.close();
        } 
        catch (IOException e)
        {
            // TODO Auto-generated catch block
        }
    }

    @Override
    public boolean loginSession(Session session, User user)
    {
        // TODO Auto-generated method stub
        session.setUser(user);
        SessionType targetType = getSessionTypeFromUser(user);
        boolean ret = changeSessionType(session, session.getSessionType(), targetType);
        if (!ret)
        {
            return false;
        }
        sessionIdToUserMap.put(session.getSessionId(), user);
        return ret;
    }
    
    @Override
    public boolean logoutSession(Session session)
    {
        // TODO Auto-generated method stub
        boolean ret = changeSessionType(session, session.getSessionType(), SessionType.NoneAuthorizationSession);
        if (!ret)
        {
            return false;
        }
        sessionIdToUserMap.remove(session.getSessionId());
        return ret;
    }

    @Override
    public void createSession(WebSocketSession webSocketSession)
    {
        // TODO Auto-generated method stub
        Session session = new Session(webSocketSession);
        session.setSessionType(SessionType.NoneAuthorizationSession);
        sessionTypeToSessionMap.get(SessionType.NoneAuthorizationSession).add(session);
        webSocketSessionToSessionMap.put(webSocketSession, session);
    }
    
    @Override
    public boolean reLoginSession(Session session)
    {
        // TODO Auto-generated method stub
        String sessionId = session.getSessionId();
        User user = sessionIdToUserMap.get(session.getSessionId());
        if (user == null)
        {
            return false;
        }
        Long ageTime = sessionIdToAgingTimeMap.remove(sessionId);
        Set<String> sessionIdSet = agingTimeToSessionIdMap.get(ageTime);
        if (sessionIdSet != null)
        {
            sessionIdSet.remove(sessionId);
        }
        return loginSession(session, user);
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
        if (!isSessionLogin(session))
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
        case Message.METHOD_WEBSOCKET:
            return MessageHttpMethod.WEBSOCKET;
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
    
    private void notifyAdminUser()
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
            NotifyAdminUserHelper notifyAdminUserHelper = new NotifyAdminUserHelper(message);
            poolTaskExecutor.execute(notifyAdminUserHelper);
        }
    }
    
    private void notifyNormalUser()
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
            NotifyNormalUserHelper notifyNormalUserHelper = new NotifyNormalUserHelper(message);
            poolTaskExecutor.execute(notifyNormalUserHelper);
        }
    }
    
    private void agingSessionId()
    {
        while(true)
        {
            for(Map.Entry<Long, Set<String>> entry : agingTimeToSessionIdMap.entrySet()) 
            {
                Long currentTimestamp = System.currentTimeMillis();
                if (entry.getKey() < currentTimestamp)
                {
                    if (entry.getValue() != null) 
                    {
                        for(String sessionId : entry.getValue())
                        {
                            sessionIdToAgingTimeMap.remove(sessionId);
                            sessionIdToUserMap.remove(sessionId);
                        }
                    }
                    agingTimeToSessionIdMap.remove(entry.getKey());
                }
            }
            try
            {
                Thread.sleep(60000);
            } 
            catch (InterruptedException e)
            {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
        }
        
    }
    
    class NotifyAdminUserHelper implements Runnable
    {
        
        Message message;
        
        public NotifyAdminUserHelper(Message message)
        {
            this.message = message;
        }
        
        @Override
        public void run()
        {
            // TODO Auto-generated method stub
            // find all session for adminUser and send the message
            Set<Session> adminEmployeeSession = sessionTypeToSessionMap.get(SessionType.AdminEmployeeSession);
            if (adminEmployeeSession == null || adminEmployeeSession.isEmpty())
            {
                try
                {
                    Thread.sleep(5000);
                    if (adminEmployeeSession == null || adminEmployeeSession.isEmpty())
                    {
                        notifiyAdminUser(message);
                        return;
                    }
                } 
                catch (InterruptedException e)
                {
                    // TODO Auto-generated catch block
                    e.printStackTrace();
                }
            }
            TextMessage returnMessage = new TextMessage(message.getResponseJsonStr());
            for(Session session : adminEmployeeSession)
            {
                try
                {
                    session.getWebSocketSession().sendMessage(returnMessage);
                } 
                catch (IOException e)
                {
                    // TODO Auto-generated catch block
                    e.printStackTrace();
                }
            }
        }
    }
    
    class NotifyNormalUserHelper implements Runnable
    {
        
        Message message;
        
        public NotifyNormalUserHelper(Message message)
        {
            this.message = message;
        }
        
        @Override
        public void run()
        {
            // TODO Auto-generated method stub
            // find all session for adminUser or normalUser and send the message
            
        }
        
    }

    @Override
    public boolean isSessionExpired(String sessionId)
    {
        // TODO Auto-generated method stub
        return !sessionIdToAgingTimeMap.containsKey(sessionId);
    }
    
}
