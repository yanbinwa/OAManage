package com.yanbinwa.OASystem.Service;

import java.io.IOException;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.TimeUnit;

import javax.annotation.PostConstruct;

import org.apache.commons.lang.RandomStringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.socket.TextMessage;

import com.yanbinwa.OASystem.Message.HttpResult;
import com.yanbinwa.OASystem.Message.Message;
import com.yanbinwa.OASystem.Session.Session;
import com.yanbinwa.OASystem.Utils.HttpUtils;

@Service("websocketMessageProcessorService")
@Transactional
public class WebsocketMessageProcessorServiceImpl implements WebsocketMessageProcessorService
{
    
    @Autowired
    private PropertyService propertyService;
    
    @Autowired
    private MessageServiceSpring messageServiceSpring;
    
    private ThreadPoolTaskExecutor poolTaskExecutor;
    private BlockingQueue<Message> messageQueue;

    @Override
    public void enqueueMessage(Message message)
    {
        // TODO Auto-generated method stub
        messageQueue.add(message);
    }
    
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
            WebsocketMessageHander websocketMessageHander = new WebsocketMessageHander(message);
            poolTaskExecutor.execute(websocketMessageHander);
        }
    }
    
    private String createSessionId()
    {
        return RandomStringUtils.randomAlphanumeric(20);
    }
    
    
    class WebsocketMessageHander implements Runnable
    {

        private Message message;
        
        public WebsocketMessageHander(Message message)
        {
            this.message = message;
        }
        
        private HttpResult handleGetSessionId(Message message)
        {
            String oldSessionId = message.getRequestPayLoad();
            String newSessionId = "";
            HttpResult httpResult = new HttpResult();
            if (messageServiceSpring.isSessionIdOnLine(oldSessionId))
            {
                try
                {
                    Thread.sleep(200);
                } 
                catch (InterruptedException e)
                {
                    // TODO Auto-generated catch block
                    e.printStackTrace();
                }
                if (messageServiceSpring.isSessionIdOnLine(oldSessionId))
                {
                    //Fix Me Close this session
                    httpResult.setStateCode(HttpUtils.RESPONSE_ERROR);
                    httpResult.setResponse("Error: Can not open two window");
                    return httpResult;
                }
            }
            Session session = message.getSession();
            if (!messageServiceSpring.isSessionExpired(oldSessionId))
            {
                newSessionId = oldSessionId;
                session.setSessionId(newSessionId);
                boolean ret = messageServiceSpring.reLoginSession(session);
                if (!ret)
                {
                    httpResult.setStateCode(HttpUtils.RESPONSE_ERROR);
                    httpResult.setResponse("Error: Can not reLoginSession");
                    return httpResult;
                }
            }
            else
            {
                newSessionId = createSessionId();
                session.setSessionId(newSessionId);
            }
            httpResult.setStateCode(HttpUtils.RESPONSE_OK);
            httpResult.setResponse(newSessionId);
            return httpResult;
        }
        
        @Override
        public void run()
        {
            // TODO Auto-generated method stub
            HttpResult httpResult = null;
            if (message.getUrl().contains(MessageServiceSpring.GET_SESSION_ID))
            {
                httpResult = handleGetSessionId(message);
            }
            else
            {
                return;
            }
            message.setResponseCode(httpResult.getStateCode());
            message.setResponsePayLoad(httpResult.getResponse());
            
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
