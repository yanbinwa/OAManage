package com.yanbinwa.OASystem.Service;

import com.yanbinwa.OASystem.Event.Event;
import com.yanbinwa.OASystem.Message.Message;
import com.yanbinwa.OASystem.Model.User;
import com.yanbinwa.OASystem.Session.Session;

public interface MessageProcessorService
{
    
    public static final String QUEUE_CAPACITY = "MessageProcessor_QueueCapacity";
    public static final String CORE_POOL_SIZE = "MessageProcessor_CorePoolSize";
    public static final String MAX_POOL_SIZE = "MessageProcessor_MaxPoolSize";
    public static final String KEEP_ALIVE_SECONDS = "MessageProcessor_KeepAliveSeconds";
    
    public static final String MESSAGE_QUEUE_SIZE = "MessageProcessor_MessageQueueSize";
    public static final String EVENT_QUEUE_SIZE = "MessageProcessor_EventQueueSize";
    
    public boolean enqueueMessage(Message message);
    public boolean loginSession(Session session, User user);
    public boolean logoutSession(Session session);
    public boolean isWhatUrl(String urlName, String url);
    public boolean sendEvent(Event event);
}
