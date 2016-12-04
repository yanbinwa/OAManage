package com.yanbinwa.OASystem.Message;

import com.yanbinwa.OASystem.Event.Event;
import com.yanbinwa.OASystem.Event.Event.EventType;
import com.yanbinwa.OASystem.Model.User;
import com.yanbinwa.OASystem.Service.EventService;
import com.yanbinwa.OASystem.Service.MessageProcessorService;
import com.yanbinwa.OASystem.Service.MessageServiceSpring;
import com.yanbinwa.OASystem.Session.Session;
import com.yanbinwa.OASystem.Utils.HttpUtils;

import net.sf.json.JSONObject;

public class LoginMessageHander extends MessageHander
{
    
    public LoginMessageHander(Message message, MessageProcessorService messageProcessorService)
    {
        super(message, messageProcessorService);
        // TODO Auto-generated constructor stub
    }
    
    private void handleLoginMessage(String responsePayload) 
    {
        String userStr = responsePayload;
        if (userStr == null || userStr.trim().equals(""))
        {
            message.setResponsePayLoad("Login Error");
            message.setResponseCode(HttpUtils.RESPONSE_ERROR);
            return;
        }
        JSONObject jsObj = JSONObject.fromObject(userStr);
        User user = (User)JSONObject.toBean(jsObj, User.class);
        Session session = message.getSession();
        boolean ret = messageProcessorService.loginSession(session, user);
        if (! ret)
        {
            message.setResponsePayLoad("Login Error");
            message.setResponseCode(HttpUtils.RESPONSE_ERROR);
            return;
        }
    }
    
    private void handleSignMessage(String responsePayload)
    {
        String userStr = responsePayload;
        if (userStr == null || userStr.trim().equals(""))
        {
            message.setResponsePayLoad("Sign Error");
            message.setResponseCode(HttpUtils.RESPONSE_ERROR);
            return;
        }
        JSONObject jsObj = JSONObject.fromObject(userStr);
        User user = (User)JSONObject.toBean(jsObj, User.class);
        Event event = new Event(EventType.Sign, user, EventService.NOTIFICATION_SERVICE);
        messageProcessorService.sendEvent(event);
    }
    
    private void handleLogoutMessage(String responsePayload)
    {
        Session session = message.getSession();
        boolean ret = messageProcessorService.logoutSession(session);
        if (! ret)
        {
            message.setResponsePayLoad("Logout Error");
            message.setResponseCode(HttpUtils.RESPONSE_ERROR);
            return;
        }
    }
    
    @Override
    protected void handleHttpResult(HttpResult httpResult)
    {
        message.setResponsePayLoad(httpResult.getResponse());
        message.setResponseCode(httpResult.getStateCode());
        
        if(httpResult.getStateCode() != HttpUtils.RESPONSE_OK)
        {
            return;
        }
        
        if(messageProcessorService.isWhatUrl(MessageServiceSpring.USER_LOGIN, message.getUrl()))
        {
            handleLoginMessage(httpResult.getResponse());
        }
        else if(messageProcessorService.isWhatUrl(MessageServiceSpring.USER_SIGN, message.getUrl()))
        {
            handleSignMessage(httpResult.getResponse());
        }
        else if(messageProcessorService.isWhatUrl(MessageServiceSpring.USER_LOGOUT, message.getUrl()))
        {
            handleLogoutMessage(httpResult.getResponse());
        }
    }
}
