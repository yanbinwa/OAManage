package com.yanbinwa.OASystem.Service;

import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicLong;

import javax.annotation.PostConstruct;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;
import org.springframework.stereotype.Service;

import com.yanbinwa.OASystem.Common.EventListener;
import com.yanbinwa.OASystem.Event.Event;
import com.yanbinwa.OASystem.Message.Message;
import com.yanbinwa.OASystem.Model.User;
import com.yanbinwa.OASystem.Model.User.UserType;
import com.yanbinwa.OASystem.Notification.Notification;
import com.yanbinwa.OASystem.Session.Session.SessionType;
import com.yanbinwa.OASystem.Utils.HttpUtils;

import net.sf.json.JSONObject;

@Service("notificationService")
public class NotificationServiceImpl implements NotificationService, EventListener
{

    @Autowired
    private EventService eventService;
    
    @Autowired
    private PropertyService propertyService;
    
    @Autowired
    private MessageServiceSpring messageServiceSpring;
    
    @Autowired
    private EmployeeService employeeService;
    
    @Autowired
    private StoreService storeService;
    
    private static final Logger logger = Logger.getLogger(NotificationServiceImpl.class);
    
    private ThreadPoolTaskExecutor poolTaskExecutor;
    private BlockingQueue<Event> eventQueue;
    
    private AtomicLong atomicNotificationId;
    
    @PostConstruct
    public void init()
    {
        String[] keys = {EventService.NOTIFICATION_SERVICE};
        eventService.register(this, keys);
        
        long notificationIdBak = (Long) propertyService.getLocalProperty(NOTIFICATION_ID_BAK, Long.class, 0);
        atomicNotificationId = new AtomicLong(notificationIdBak);
        
        poolTaskExecutor = new ThreadPoolTaskExecutor();
        poolTaskExecutor.setQueueCapacity((Integer)propertyService.getProperty(QUEUE_CAPACITY, Integer.class));
        poolTaskExecutor.setCorePoolSize((Integer)propertyService.getProperty(CORE_POOL_SIZE, Integer.class));  
        poolTaskExecutor.setMaxPoolSize((Integer)propertyService.getProperty(MAX_POOL_SIZE, Integer.class));  
        poolTaskExecutor.setKeepAliveSeconds((Integer)propertyService.getProperty(KEEP_ALIVE_SECONDS, Integer.class));
        poolTaskExecutor.initialize();
        
        eventQueue = new ArrayBlockingQueue<Event>((Integer)propertyService.getProperty(EVENT_QUEUE_SIZE, Integer.class));
        
        new Thread(new Runnable() {

            @Override
            public void run()
            {
                // TODO Auto-generated method stub
                handleEvent();
            }
            
        }).start();
    }
    
    private long getNoficationId()
    {
        long notificationId = atomicNotificationId.getAndIncrement();
        propertyService.setLocalProperty(NOTIFICATION_ID_BAK, atomicNotificationId, Integer.class);
        return notificationId;
    }
    
    private Notification getNotification(Message message, int expiredTime)
    {
        Notification notification = new Notification();
        notification.setMessage(message);
        notification.setExpiredTime(expiredTime);
        notification.setId(getNoficationId());
        return notification;
    }
    
    private void handleEvent()
    {
        while(true)
        {
            Event event = null;
            try
            {
                event = eventQueue.poll(1000, TimeUnit.MILLISECONDS);
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
            HandleEventHelper handleEventHelper = new HandleEventHelper(event);
            poolTaskExecutor.execute(handleEventHelper);
        }
    }
    
    private void handleEvent(Event event)
    {
        if (event == null)
        {
            logger.error("Empty event");
            return;
        }
        switch(event.getEventType())
        {
        case Sign:
            handleUserSignEvent(event);
            break;
            
        case ORCode:
            handleORCodeUpdateEvent(event);
            break;
            
        default:
            break;
        }
    }
    
    private void handleUserSignEvent(Event event)
    {
        User user = (User) event.getPayLoad();
        int userId = user.getUserId();
        UserType userType = user.getUserType();
        Object userObject = null;
        if (userType == UserType.Employee)
        {
            userObject = employeeService.findEmployeeById(userId);
        }
        else if(userType == UserType.Store)
        {
            userObject = storeService.findStoreById(userId);
        }
        if (userObject == null)
        {
            logger.error("Can not find the userId");
        }
        
        Message message = new Message();
        
        String routeKey = (String)propertyService.getProperty(MessageServiceSpring.USERSIGN_ROUTEKEY, String.class);
        String functionKey = (String)propertyService.getProperty(MessageServiceSpring.USERSIGN_FUNCTIONKEY, String.class);
        int responseCode = HttpUtils.RESPONSE_OK;
        JSONObject jsonObj = new JSONObject();
        jsonObj.put("type", "UserSignVerify");
        jsonObj.put("user", user);
        jsonObj.put("userObject", userObject);
        
        message.setRouteKey(routeKey);
        message.setFunctionKey(functionKey);
        message.setResponseCode(responseCode);
        message.setResponsePayLoad(jsonObj.toString());
        
        Notification notification = getNotification(message, NOTIFICATION_NEVER_EXPIRED_TIME);
        
        boolean ret = messageServiceSpring.notifiyUser(notification, SessionType.AdminStoreSession);
        if (!ret)
        {
            logger.error("Can not notify the message");
        }
    }
    
    private void handleORCodeUpdateEvent(Event event)
    {
        String oRCodeKey = (String)event.getPayLoad();
        if (oRCodeKey == null)
        {
            logger.error("ORCode is null");
            return;
        }
        Message message = new Message();
        String routeKey = (String)propertyService.getProperty(ORCodeService.ORCODEUPDATE_ROUTEKEY, String.class);
        String functionKey = (String)propertyService.getProperty(ORCodeService.ORCODEUPDATE_FUNCTIONKEY, String.class);
        int responseCode = HttpUtils.RESPONSE_OK;
        message.setRouteKey(routeKey);
        message.setFunctionKey(functionKey);
        message.setResponseCode(responseCode);
        message.setResponsePayLoad(oRCodeKey);
        
        Notification notification = getNotification(message, NOTIFICATION_QUICK_EXPIRED_TIME);
        
        boolean ret = messageServiceSpring.notifiyUser(notification, SessionType.AdminStoreSession);
        if (!ret)
        {
            logger.error("Can not send the ORCode update message to admin");
        }
    }
    
    @Override
    public void callBack(Event event)
    {
        // TODO Auto-generated method stub
        eventQueue.add(event);
    }
    
    class HandleEventHelper implements Runnable
    {

        private Event event;
        
        public HandleEventHelper(Event event)
        {
            this.event = event;
        }
        
        @Override
        public void run()
        {
            // TODO Auto-generated method stub
            handleEvent(event);
        }
        
    }

}
