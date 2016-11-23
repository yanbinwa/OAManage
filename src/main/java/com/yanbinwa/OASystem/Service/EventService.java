package com.yanbinwa.OASystem.Service;

import com.yanbinwa.OASystem.Common.EventListener;
import com.yanbinwa.OASystem.Event.Event;

public interface EventService
{
    public static final String EVENT_QUEUE_SIZE = "EventQueueSize";
    public void register(EventListener listener, String[] keys);
    public void sendMessage(final Event event);
}
