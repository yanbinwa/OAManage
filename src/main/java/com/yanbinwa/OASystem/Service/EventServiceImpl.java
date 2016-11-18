package com.yanbinwa.OASystem.Service;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.TimeUnit;

import javax.annotation.PostConstruct;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.yanbinwa.OASystem.Common.EventListener;
import com.yanbinwa.OASystem.Model.Event;

@Service("eventService")
public class EventServiceImpl implements EventService
{

    private static final Logger logger = Logger.getLogger(EventServiceImpl.class);
    
    private Map<String, Set<EventListener>> keyToListener;
    
    private BlockingQueue<Event> messageQueue;
    
    @Autowired
    private PropertyService propertyService;

    @PostConstruct
    public void init()
    {
        keyToListener = new HashMap<String, Set<EventListener>>();
        messageQueue = new ArrayBlockingQueue<Event>((Integer)propertyService.getProperty(EVENT_QUEUE_SIZE, Integer.class));
        new Thread(new Runnable() {

            @Override
            public void run()
            {
                // TODO Auto-generated method stub
                handleEvent();
            }
            
        }).start();
    }
    
    @Override
    public void register(EventListener listener, String[] keys)
    {
        // TODO Auto-generated method stub
        if (keys == null)
        {
            return;
        }
        for(String key : keys)
        {
            Set<EventListener> listenerSet = keyToListener.get(key);
            if(listenerSet == null)
            {
                listenerSet = new HashSet<EventListener>();
                keyToListener.put(key, listenerSet);
            }
            listenerSet.add(listener);
        }
    }

    @Override
    public void sendMessage(Event event)
    {
        // TODO Auto-generated method stub
        messageQueue.add(event);
    }
    
    protected void handleEvent()
    {
        while(true)
        {
            Event event = null;
            try
            {
                event = messageQueue.poll(1000, TimeUnit.MILLISECONDS);
            } 
            catch (InterruptedException e)
            {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
            if(event == null)
            {
                continue;
            }
            logger.info(event);
            String key = event.getName();
            Set<EventListener> listenerSet = keyToListener.get(key);
            if(listenerSet == null)
            {
                return;
            }
            for(EventListener listener : listenerSet)
            {
                listener.callBack(event);
            }
        }
    }

}
