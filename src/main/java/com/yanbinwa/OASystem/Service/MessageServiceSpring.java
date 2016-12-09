package com.yanbinwa.OASystem.Service;

import org.springframework.web.socket.TextMessage;
import org.springframework.web.socket.WebSocketSession;

import com.yanbinwa.OASystem.Message.Message;
import com.yanbinwa.OASystem.Model.User;
import com.yanbinwa.OASystem.Session.Session;
import com.yanbinwa.OASystem.Session.Session.SessionType;

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
    public static final String LOGIN = "Login";
    public static final String USER_LOGIN = "UserLogin";
    public static final String USER_LOGOUT = "UserLogout";
    public static final String USER_SIGN = "UserSign";
    public static final String USER_VERIFY_SIGN = "VerifyUserSign";
    
    public static final String QUEUE_CAPACITY = "MessageService_QueueCapacity";
    public static final String CORE_POOL_SIZE = "MessageService_CorePoolSize";
    public static final String MAX_POOL_SIZE = "MessageService_MaxPoolSize";
    public static final String KEEP_ALIVE_SECONDS = "MessageService_KeepAliveSeconds";
    public static final String NOTIFY_ADMIN_EMPLOYEE_QUEUE_SIZE = "MessageService_NotifyAdminEmployeeQueueSize";
    public static final String NOTIFY_NORMAL_EMPLOYEE_QUEUE_SIZE = "MessageService_NotifyNormalEmployeeQueueSize";
    public static final String NOTIFY_ADMIN_STORE_QUEUE_SIZE = "MessageService_NotifyAdminStoreQueueSize";
    public static final String NOTIFY_NORMAL_STORE_QUEUE_SIZE = "MessageService_NotifyNormalStoreQueueSize";
    
    public static final String NOTIFY_SESSION_AGE_TIME = "MessageService_SessionAgeTime";
    
    public static final String USERLOGIN_ROUTEKEY = "MessageService_UserLogin_RouteKey";
    public static final String USERLOGIN_FUNCTIONKEY = "MessageService_UserLogin_FunctionKey";
    public static final String USERSIGN_ROUTEKEY = "MessageService_UserSign_RouteKey";
    public static final String USERSIGN_FUNCTIONKEY = "MessageService_UserSign_FunctionKey";
    
    public static final String NOTIFY_ADMIN_EMPLOYEE_QUEUE_FILENAME = PropertyService.SYSTEM_RESOURCE_DIR + "/queueCache/" + "messageService_Admin_Employee_File.txt";
    public static final String NOTIFY_NORMAL_EMPLOYEE_QUEUE_FILENAME = PropertyService.SYSTEM_RESOURCE_DIR + "/queueCache/" + "messageService_Normal_Employee_File.txt";
    public static final String NOTIFY_ADMIN_STORE_QUEUE_FILENAME = PropertyService.SYSTEM_RESOURCE_DIR + "/queueCache/" + "messageService_Admin_Store_File.txt";
    public static final String NOTIFY_NORMAL_STORE_QUEUE_FILENAME = PropertyService.SYSTEM_RESOURCE_DIR + "/queueCache/" + "messageService_Normal_Store_File.txt";
    
    public static final String GET_SESSION_ID = "getSessionId";
    
    public void handleMessage(WebSocketSession session, TextMessage message, int type);
    public boolean validateSession(Session session, Message message);
    public boolean changeSessionType(Session session, SessionType sourceType, SessionType targetType);
    public void closeSession(WebSocketSession websocketSession);
    public boolean loginSession(Session session, User user);
    public void createSession(WebSocketSession websocketSession);
    
    public boolean isWhatUrl(String urlName, String url);
    public SessionType getSessionTypeFromUser(User user);
    
    public boolean notifiyAdminEmployeeUser(Message message);
    public boolean notifiyNormalEmployeeUser(Message message);
    public boolean notifiyAdminStoreUser(Message message);
    public boolean notifiyNormalStoreUser(Message message);
        
    public boolean isSessionIdOnLine(String sessionId);
    public boolean isSessionExpired(String sessionId);
    public boolean reLoginSession(Session session);
    public boolean logoutSession(Session session); 
    
    public User getUserBySessionId(String sessionId);
}
