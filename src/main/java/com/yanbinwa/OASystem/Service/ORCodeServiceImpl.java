package com.yanbinwa.OASystem.Service;

import java.util.Timer;
import java.util.TimerTask;

import javax.annotation.PostConstruct;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.yanbinwa.OASystem.Event.Event;
import com.yanbinwa.OASystem.Event.Event.EventType;
import com.yanbinwa.OASystem.Utils.QRCodeUtil;

@Service("oRCodeService")
public class ORCodeServiceImpl implements ORCodeService
{
    
    @Autowired
    private EventService eventService;
    
    @Autowired
    private PropertyService propertyService;
    
    private String ORCodeKey;
    private int ORCodeKeyLen;
    private Timer timer;
    
    @PostConstruct
    public void init()
    {
        ORCodeKeyLen = (Integer)propertyService.getProperty(ORCODE_KEY_LENGTH, Integer.class);
        
        timer = new Timer();
        TimerTask task = new TimerTask()
        {

            @Override
            public void run()
            {
                // TODO Auto-generated method stub
                createAndSendORCodeKey();
            }
            
        };
        int intevalPeriod = (Integer)propertyService.getProperty(INTERVAL_PERIOD, Integer.class);
        timer.schedule(task, 0, intevalPeriod);
    }

    @Override
    public String getORCodeKey()
    {
        // TODO Auto-generated method stub
        return ORCodeKey;
    }
        
    private void createAndSendORCodeKey()
    {
        ORCodeKey = QRCodeUtil.getRandomQRCodeKey(ORCodeKeyLen);
        Event event = new Event(EventType.ORCode, ORCodeKey, EventService.NOTIFICATION_SERVICE);
        eventService.sendMessage(event);
    }

}
