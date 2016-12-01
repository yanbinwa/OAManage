package com.yanbinwa.OASystem.Service;

import java.io.IOException;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.TimeUnit;

import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.socket.TextMessage;

import com.yanbinwa.OASystem.Event.Event;
import com.yanbinwa.OASystem.Message.LoginMessageHander;
import com.yanbinwa.OASystem.Message.Message;
import com.yanbinwa.OASystem.Message.MessageHander;
import com.yanbinwa.OASystem.Message.SignMessageHander;
import com.yanbinwa.OASystem.Model.User;
import com.yanbinwa.OASystem.Session.Session;
import com.yanbinwa.OASystem.Session.Session.SessionType;
import com.yanbinwa.OASystem.Utils.HttpUtils;

@Service("messageProcessorService")
@Transactional
public class MessageProcessorServiceImpl implements MessageProcessorService
{
    private ThreadPoolTaskExecutor poolTaskExecutor;
    private BlockingQueue<Message> messageQueue;

    @Autowired
    private PropertyService propertyService;
    
    @Autowired
    private MessageServiceSpring messageServiceSpring;
    
    @Autowired
    private EventService eventService;
    
    @PostConstruct
    public void init()
    {
        poolTaskExecutor = new ThreadPoolTaskExecutor();
        poolTaskExecutor.setQueueCapacity((Integer)propertyService.getProperty(QUEUE_CAPACITY, Integer.class));
        poolTaskExecutor.setCorePoolSize((Integer)propertyService.getProperty(CORE_POOL_SIZE, Integer.class));  
        poolTaskExecutor.setMaxPoolSize((Integer)propertyService.getProperty(MAX_POOL_SIZE, Integer.class));  
        poolTaskExecutor.setKeepAliveSeconds((Integer)propertyService.getProperty(KEEP_ALIVE_SECONDS, Integer.class));  
        poolTaskExecutor.initialize();
        
        messageQueue = new ArrayBlockingQueue<Message>((Integer)propertyService.getProperty(MESSAGE_QUEUE_SIZE, Integer.class));
        
        new Thread(new Runnable() {

            @Override
            public void run()
            {
                // TODO Auto-generated method stub
                handleMessage();
            }
        
        }).start();
    }
    
    @PreDestroy
    public void destroy()
    {
        
    }
        
    @Override
    public boolean enqueueMessage(Message message)
    {
        // TODO Auto-generated method stub
        return messageQueue.add(message);
    }
    
    private boolean validateSession(Session session, Message message)
    {
        return messageServiceSpring.validateSession(session, message);
    }
    
    private MessageHander getMessageHander(Message message)
    {
        if (messageServiceSpring.isWhatUrl(MessageServiceSpring.USER_LOGIN, message.getUrl()))
        {
            return new LoginMessageHander(message, this);
        }
        else if(messageServiceSpring.isWhatUrl(MessageServiceSpring.USER_SIGN, message.getUrl()))
        {
            return new SignMessageHander(message, this);
        }
        return new MessageHander(message, this);
    }
    
    private void handleMessage()
    {
        //Fix me
        while(true)
        {
            Message message = null;
            try
            {
                message = messageQueue.poll(1000, TimeUnit.MILLISECONDS);
            } 
            catch (InterruptedException e)
            {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
            if(message == null)
            {
                continue;
            }
            
            //check the authorization of the session and message
            if (validateSession(message.getSession(), message))
            {
                MessageHander messageHander = getMessageHander(message);
                poolTaskExecutor.execute(messageHander);
            }
            else
            {
                message.setResponsePayLoad("Auther Error");
                message.setResponseCode(HttpUtils.RESPONSE_ERROR);
                TextMessage returnMessage = new TextMessage(message.getResponseJsonStr());
                try
                {
                    message.getSession().getWebSocketSession().sendMessage(returnMessage);
                } 
                catch (IOException e)
                {
                    // TODO Auto-generated catch block
                    e.printStackTrace();
                }
            }
        }
    }

    @Override
    public boolean changeSessionType(Session session, SessionType sourceType, SessionType targetType)
    {
        // TODO Auto-generated method stub
        return messageServiceSpring.changeSessionType(session, sourceType, targetType);
    }

    @Override
    public boolean isWhatUrl(String urlName, String url)
    {
        // TODO Auto-generated method stub
        return messageServiceSpring.isWhatUrl(urlName, url);
    }

    @Override
    public SessionType getSessionTypeFromUser(User user)
    {
        // TODO Auto-generated method stub
        return messageServiceSpring.getSessionTypeFromUser(user);
    }

    @Override
    public boolean sendEvent(Event event)
    {
        // TODO Auto-generated method stub
        return eventService.sendMessage(event);
    }
}
