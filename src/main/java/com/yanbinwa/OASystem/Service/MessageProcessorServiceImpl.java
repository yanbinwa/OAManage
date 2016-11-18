package com.yanbinwa.OASystem.Service;

import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.TimeUnit;

import javax.annotation.PostConstruct;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.yanbinwa.OASystem.Message.Message;
import com.yanbinwa.OASystem.Message.MessageHander;
import com.yanbinwa.OASystem.Session.Session;

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
    
    @PostConstruct
    public void init()
    {
        poolTaskExecutor = new ThreadPoolTaskExecutor();
        poolTaskExecutor.setQueueCapacity((Integer)propertyService.getProperty(QUEUE_CAPACITY, Integer.class));
        poolTaskExecutor.setCorePoolSize((Integer)propertyService.getProperty(CORE_POOL_SIZE, Integer.class));  
        poolTaskExecutor.setMaxPoolSize((Integer)propertyService.getProperty(MAX_POOL_SIZE, Integer.class));  
        poolTaskExecutor.setKeepAliveSeconds((Integer)propertyService.getProperty(KEEP_ALIVE_SECONDS, Integer.class));  
        poolTaskExecutor.initialize();
        
        messageQueue = new ArrayBlockingQueue<Message>((Integer)propertyService.getProperty(MESSAGE_PROCESSOR_QUEUE_SIZE, Integer.class));
        
        new Thread(new Runnable() {

                @Override
                public void run()
                {
                    // TODO Auto-generated method stub
                    handleMessage();
                }
        
        }).start();
    }
    
    @Override
    public boolean enqueue(Message message)
    {
        // TODO Auto-generated method stub
        return messageQueue.add(message);
    }
    
    private boolean validateSession(Session session)
    {
        return messageServiceSpring.validateSession(session);
    }
    
    private void handleMessage()
    {
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
            if (validateSession(message.getSession()))
            {
                MessageHander messageHander = new MessageHander(message);
                poolTaskExecutor.execute(messageHander);
            }
        }
    }

}
