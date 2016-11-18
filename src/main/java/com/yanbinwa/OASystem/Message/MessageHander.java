package com.yanbinwa.OASystem.Message;

import java.io.IOException;

import org.apache.log4j.Logger;
import org.springframework.web.socket.TextMessage;

import com.yanbinwa.OASystem.Utils.HttpUtils;

public class MessageHander implements Runnable
{
    private static final Logger logger = Logger.getLogger(MessageHander.class);
    Message message;
    
    public MessageHander(Message message)
    {
        this.message = message;
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
    
    private void sendMessage()
    {
        String type = null;
        switch(message.getMethod())
        {
        case GET:
            type = "GET";
            break;
            
        case POST:
            type = "POST";
            break;
            
        case DEL:
            type = "DELETE";
            break;
            
        default:
            break;
        }
        if(type == null)
        {
            logger.error("Unknown http method");
            return;
        }
        
        String url = message.getUrl() + message.getUrlParameter();
        String payLoad = message.getRequestPayLoad();
        HttpResult ret = HttpUtils.httpRequest(url, payLoad, type);
        message.setResponsePayLoad(ret.getResponse());
        message.setResponseCode(ret.getStateCode());
    }
    
    @Override
    public void run()
    {
        // TODO Auto-generated method stub
        handerMessage();
    }
}
