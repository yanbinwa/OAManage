package com.yanbinwa.OASystem.Service;

import javax.annotation.PostConstruct;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.yanbinwa.OASystem.Common.EventListener;
import com.yanbinwa.OASystem.Event.Event;

@Service("notificationService")
public class NotificationServiceImpl implements NotificationService, EventListener
{

    @Autowired
    private EventService eventService;
    
    @PostConstruct
    public void init()
    {
        String[] keys = {"NotificationService"};
        eventService.register(this, keys);
    }
    
    @Override
    public void callBack(Event event)
    {
        // TODO Auto-generated method stub
        
    }

}
