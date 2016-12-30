package com.yanbinwa.OASystem.Service;

import java.io.IOException;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
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
import com.yanbinwa.OASystem.Common.JsonPersist;
import com.yanbinwa.OASystem.Event.Event;
import com.yanbinwa.OASystem.Message.Message;
import com.yanbinwa.OASystem.Message.Message.MessageHttpMethod;
import com.yanbinwa.OASystem.Model.User;
import com.yanbinwa.OASystem.Model.User.AuthType;
import com.yanbinwa.OASystem.Model.User.UserType;
import com.yanbinwa.OASystem.Notification.Notification;
import com.yanbinwa.OASystem.QueuePersist.AppendAction.QueueAction;
import com.yanbinwa.OASystem.QueuePersist.PersistBlockQueue;
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
    private static Map<Long, Set<String>> agingTimeToSessionIdMap = new HashMap<Long, Set<String>>();
    private static Map<String, Long> sessionIdToAgingTimeMap = new HashMap<String, Long>();
    private static Map<String, Session> onLineSessionIdMap = new HashMap<String, Session>();
    private static Map<String, User> sessionIdToUserMap = new HashMap<String, User>();
    
    private static final Logger logger = Logger.getLogger(MessageServiceSpringImpl.class);
    
    private PersistBlockQueue<JsonPersist> notifyAdminEmployeeQueue;
    private PersistBlockQueue<JsonPersist> notifyNormalEmployeeQueue;
    private PersistBlockQueue<JsonPersist> notifyAdminStoreQueue;
    private PersistBlockQueue<JsonPersist> notifyNormalStoreQueue;
    private ThreadPoolTaskExecutor poolTaskExecutor;
        
    @Autowired
    private PropertyService propertyService;
    
    @Autowired
    private MessageProcessorService messageProcessorService;
    
    @Autowired
    private EventService eventService;
    
    @Autowired
    private WebsocketMessageProcessorService websocketMessageProcessorService;
    
    @Autowired
    private QueuePersistService queuePersistService;
        
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
        
        notifyAdminEmployeeQueue = new PersistBlockQueue<JsonPersist>((Integer)propertyService.getProperty(MessageServiceSpring.NOTIFY_ADMIN_EMPLOYEE_QUEUE_SIZE, Integer.class), 
                                            Notification.class, MessageServiceSpring.NOTIFY_ADMIN_EMPLOYEE_QUEUE_FILENAME);       
        queuePersistService.registerQueue(notifyAdminEmployeeQueue, Notification.class, MessageServiceSpring.NOTIFY_ADMIN_EMPLOYEE_QUEUE_FILENAME);
        queuePersistService.loadQueue(MessageServiceSpring.NOTIFY_ADMIN_EMPLOYEE_QUEUE_FILENAME);
        
        notifyNormalEmployeeQueue = new PersistBlockQueue<JsonPersist>((Integer)propertyService.getProperty(MessageServiceSpring.NOTIFY_NORMAL_EMPLOYEE_QUEUE_SIZE, Integer.class), 
                                            Notification.class, MessageServiceSpring.NOTIFY_NORMAL_EMPLOYEE_QUEUE_FILENAME);
        queuePersistService.registerQueue(notifyNormalEmployeeQueue, Notification.class, MessageServiceSpring.NOTIFY_NORMAL_EMPLOYEE_QUEUE_FILENAME);
        queuePersistService.loadQueue(MessageServiceSpring.NOTIFY_NORMAL_EMPLOYEE_QUEUE_FILENAME);
        
        notifyAdminStoreQueue = new PersistBlockQueue<JsonPersist>((Integer)propertyService.getProperty(MessageServiceSpring.NOTIFY_ADMIN_STORE_QUEUE_SIZE, Integer.class), 
                                            Notification.class, MessageServiceSpring.NOTIFY_ADMIN_STORE_QUEUE_FILENAME);
        queuePersistService.registerQueue(notifyAdminStoreQueue, Notification.class, MessageServiceSpring.NOTIFY_ADMIN_STORE_QUEUE_FILENAME);
        queuePersistService.loadQueue(MessageServiceSpring.NOTIFY_ADMIN_STORE_QUEUE_FILENAME);

        notifyNormalStoreQueue = new PersistBlockQueue<JsonPersist>((Integer)propertyService.getProperty(MessageServiceSpring.NOTIFY_NORMAL_STORE_QUEUE_SIZE, Integer.class), 
                                            Notification.class, MessageServiceSpring.NOTIFY_NORMAL_STORE_QUEUE_FILENAME);
        queuePersistService.registerQueue(notifyNormalStoreQueue, Notification.class, MessageServiceSpring.NOTIFY_NORMAL_STORE_QUEUE_FILENAME);
        queuePersistService.loadQueue(MessageServiceSpring.NOTIFY_NORMAL_STORE_QUEUE_FILENAME);
                
        new Thread(new Runnable() {

            @Override
            public void run()
            {
                // TODO Auto-generated method stub
                notifyAdminEmployeeUser();
            }
            
        }).start();
        
        new Thread(new Runnable() {

            @Override
            public void run()
            {
                // TODO Auto-generated method stub
                notifyNormalEmployeeUser();
            }
            
        }).start();
        
        new Thread(new Runnable() {

            @Override
            public void run()
            {
                // TODO Auto-generated method stub
                notifyAdminStoreUser();
            }
            
        }).start();
        
        new Thread(new Runnable() {

            @Override
            public void run()
            {
                // TODO Auto-generated method stub
                notifyNormalStoreUser();
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
            if (shouldURLAccessWithoutLogin(message.getUrl()))
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
    
    private boolean shouldURLAccessWithoutLogin(String url)
    {
        boolean ret = false;
        if (url.contains("/login/"))
        {
            return true;
        }
        else if(url.contains("/location/"))
        {
            return true;
        }
        return ret;
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
    public boolean notifiyUser(Notification notification, SessionType sessionType)
    {
        // TODO Auto-generated method stub
        boolean ret = false;
        switch(sessionType)
        {
        case AdminStoreSession:
            ret = notifyAdminStoreQueue.offer(notification);
            queuePersistService.appendAction(notification, QueueAction.PUSH, notifyAdminStoreQueue.getFilename());
            break;
        case AdminEmployeeSession:
            ret = notifyAdminEmployeeQueue.offer(notification);
            queuePersistService.appendAction(notification, QueueAction.PUSH, notifyAdminEmployeeQueue.getFilename());
            break;
        case NormalStoreSession:
            ret = notifyNormalStoreQueue.offer(notification);
            queuePersistService.appendAction(notification, QueueAction.PUSH, notifyNormalStoreQueue.getFilename());
            break;
        case NormalEmployeeSession:
            ret = notifyNormalEmployeeQueue.offer(notification);
            queuePersistService.appendAction(notification, QueueAction.PUSH, notifyNormalEmployeeQueue.getFilename());
            break;
        default:
            break;
        }
        return ret;
    }
    
    private boolean notifiyUserCancel(Notification notification, SessionType sessionType)
    {
        boolean ret = false;
        switch(sessionType)
        {
        case AdminStoreSession:
            ret = notifyAdminStoreQueue.cancelPoll(notification);
            break;
        case AdminEmployeeSession:
            ret = notifyAdminEmployeeQueue.cancelPoll(notification);
            break;
        case NormalStoreSession:
            ret = notifyNormalStoreQueue.cancelPoll(notification);
            break;
        case NormalEmployeeSession:
            ret = notifyNormalEmployeeQueue.cancelPoll(notification);
            break;
        default:
            break;
        }
        return ret;
    }
    
    private boolean notifiyUserCommit(Notification notification, SessionType sessionType)
    {
        boolean ret = false;
        switch(sessionType)
        {
        case AdminStoreSession:
            ret = notifyAdminStoreQueue.commitPoll(notification);
            queuePersistService.appendAction(notification, QueueAction.POLL, notifyAdminStoreQueue.getFilename());
            break;
        case AdminEmployeeSession:
            ret = notifyAdminEmployeeQueue.commitPoll(notification);
            queuePersistService.appendAction(notification, QueueAction.POLL, notifyAdminStoreQueue.getFilename());
            break;
        case NormalStoreSession:
            ret = notifyNormalStoreQueue.commitPoll(notification);
            queuePersistService.appendAction(notification, QueueAction.POLL, notifyAdminStoreQueue.getFilename());
            break;
        case NormalEmployeeSession:
            ret = notifyNormalEmployeeQueue.commitPoll(notification);
            queuePersistService.appendAction(notification, QueueAction.POLL, notifyAdminStoreQueue.getFilename());
            break;
        default:
            break;
        }
        return ret;
    }
        
    private void notifyAdminEmployeeUser()
    {
        while(true)
        {
            Notification notification = null;
            notification = (Notification)notifyAdminEmployeeQueue.pollWithoutCommit(1000, TimeUnit.MILLISECONDS);
            if(notification == null)
            {
                continue;
            }
            if(notification.isExpired())
            {
                continue;
            }
            NotifyUserHelper notifyAdminEmployeeUserHelper = new NotifyUserHelper(notification, SessionType.AdminEmployeeSession);
            poolTaskExecutor.execute(notifyAdminEmployeeUserHelper);
        }
    }
    
    private void notifyNormalEmployeeUser()
    {
        while(true)
        {
            Notification notification = null;
            notification = (Notification)notifyNormalEmployeeQueue.pollWithoutCommit(1000, TimeUnit.MILLISECONDS);
            if(notification == null)
            {
                continue;
            }
            if(notification.isExpired())
            {
                continue;
            }
            NotifyUserHelper notifyNormalUserHelper = new NotifyUserHelper(notification, SessionType.NormalEmployeeSession);
            poolTaskExecutor.execute(notifyNormalUserHelper);
        }
    }
    
    private void notifyAdminStoreUser()
    {
        while(true)
        {
            Notification notification = null;
            notification = (Notification)notifyAdminStoreQueue.pollWithoutCommit(1000, TimeUnit.MILLISECONDS);
            if(notification == null)
            {
                continue;
            }
            if(notification.isExpired())
            {
                continue;
            }
            NotifyUserHelper notifyAdminStoreUserHelper = new NotifyUserHelper(notification, SessionType.AdminStoreSession);
            poolTaskExecutor.execute(notifyAdminStoreUserHelper);
        }
    }
    
    private void notifyNormalStoreUser()
    {
        while(true)
        {
            Notification notification = null;
            notification = (Notification)notifyNormalStoreQueue.pollWithoutCommit(1000, TimeUnit.MILLISECONDS);
            if(notification == null)
            {
                continue;
            }
            if(notification.isExpired())
            {
                continue;
            }
            NotifyUserHelper notifyNormalStoreUserHelper = new NotifyUserHelper(notification, SessionType.NormalStoreSession);
            poolTaskExecutor.execute(notifyNormalStoreUserHelper);
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
    
    class NotifyUserHelper implements Runnable
    {

        Notification notification;
        SessionType sessionType;
        
        public NotifyUserHelper(Notification notification, SessionType sessionType)
        {
            this.notification = notification;
            this.sessionType = sessionType;
        }
        
        @Override
        public void run()
        {
            // TODO Auto-generated method stub
            Set<Session> userSession = sessionTypeToSessionMap.get(sessionType);
            if (userSession == null || userSession.isEmpty())
            {
                try
                {
                    Thread.sleep(1000);
                    if (userSession == null || userSession.isEmpty())
                    {
                        notifiyUserCancel(notification, sessionType);
                        return;
                    }
                } 
                catch (InterruptedException e)
                {
                    // TODO Auto-generated catch block
                    e.printStackTrace();
                }
            }
            TextMessage returnMessage = new TextMessage(notification.getMessage().getResponseJsonStr());
            for(Session session : userSession)
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
            notifiyUserCommit(notification, sessionType);
        }
        
    }

    @Override
    public boolean isSessionExpired(String sessionId)
    {
        // TODO Auto-generated method stub
        return !sessionIdToAgingTimeMap.containsKey(sessionId);
    }

    @Override
    public User getUserBySessionId(String sessionId)
    {
        // TODO Auto-generated method stub
        return sessionIdToUserMap.get(sessionId);
    }
    
}
