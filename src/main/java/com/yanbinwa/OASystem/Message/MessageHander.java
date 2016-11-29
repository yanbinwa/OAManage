package com.yanbinwa.OASystem.Message;

import java.io.IOException;

import org.apache.log4j.Logger;
import org.springframework.web.socket.TextMessage;

import com.yanbinwa.OASystem.Service.MessageProcessorService;
import com.yanbinwa.OASystem.Utils.HttpUtils;

public class MessageHander implements Runnable
{
    protected static final Logger logger = Logger.getLogger(MessageHander.class);
    protected Message message;
    protected MessageProcessorService messageProcessorService;
    
    public MessageHander(Message message, MessageProcessorService messageProcessorService)
    {
        this.message = message;
        this.messageProcessorService = messageProcessorService;
    }
    
    public void setMessageServiceSpring(MessageProcessorService messageProcessorService)
    {
        this.messageProcessorService = messageProcessorService;
    }
    
    public void handerMessage()
    {
        
        sendMessage();

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
    
    protected void handleHttpResult(HttpResult httpResult)
    {
        message.setResponsePayLoad(httpResult.getResponse());
        message.setResponseCode(httpResult.getStateCode());
    }
    
    protected void sendMessage()
    {
        String type = HttpUtils.getHttpMethodStr(message.method);
        if(type == null)
        {
            logger.error("Unknown http method");
            return;
        }
        
        String url = message.getUrl() + message.getUrlParameter();
        String payLoad = message.getRequestPayLoad();
        HttpResult ret = HttpUtils.httpRequest(url, payLoad, type);
        handleHttpResult(ret);
    }
    
    @Override
    public void run()
    {
        // TODO Auto-generated method stub
        handerMessage();
    }
}
