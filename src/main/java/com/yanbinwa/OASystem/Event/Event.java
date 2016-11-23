package com.yanbinwa.OASystem.Event;

import java.io.Serializable;
import java.util.concurrent.atomic.AtomicInteger;

public class Event implements Serializable
{
    private static final long serialVersionUID = 1L;
    private static AtomicInteger atomicEventId = new AtomicInteger(0);
    
    public enum EventType 
    {
        EmployeeSign, StoreSign, EmployeeLogin, StoreSignLogin, Order
    }
    
    int eventId;
    EventType eventType;
    /** From Model like Employee, Store */
    Object payLoad;
    String target;
    
    public Event(EventType eventType, Object playLoad, String target)
    {
        this.eventId = atomicEventId.getAndIncrement();
        this.eventType = eventType;
        this.payLoad = playLoad;
        this.target = target;
    }
    
    public Event()
    {
        
    }
    
    public int getEventId()
    {
        return this.eventId;
    }
    
    public void setPayLoad(Object payLoad)
    {
        this.payLoad = payLoad;
    }
    
    public Object getPayLoad()
    {
        return payLoad;
    }
    
    public void setEventType(EventType eventType)
    {
        this.eventType = eventType;
    }
    
    public EventType getEventType()
    {
        return eventType;
    }
    
    public String getTarget()
    {
        return target;
    }
    
    @Override
    public String toString()
    {
        StringBuffer sb = new StringBuffer();
        sb.append("eventId = " + eventId).append("; ")
            .append("target = " + target).append("; ")
            .append("eventType = " + eventType).append("; ")
            .append("payLoad = " + payLoad);
        return sb.toString();
    }
}
