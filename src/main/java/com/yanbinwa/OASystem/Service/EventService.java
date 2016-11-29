package com.yanbinwa.OASystem.Service;

import com.yanbinwa.OASystem.Common.EventListener;
import com.yanbinwa.OASystem.Event.Event;

public interface EventService
{
    
    public static final String MESSAGE_SERVICE_SPRING = "messageServiceSpring";
    public static final String NOTIFICATION_SERVICE = "notificationService";
    
    public static final String EVENT_QUEUE_SIZE = "Event_QueueSize";
    public void register(EventListener listener, String[] keys);
    public boolean sendMessage(final Event event);
}
