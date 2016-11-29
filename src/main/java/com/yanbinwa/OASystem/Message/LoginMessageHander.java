package com.yanbinwa.OASystem.Message;

import com.yanbinwa.OASystem.Model.User;
import com.yanbinwa.OASystem.Service.MessageProcessorService;
import com.yanbinwa.OASystem.Session.Session;
import com.yanbinwa.OASystem.Session.Session.SessionType;
import com.yanbinwa.OASystem.Utils.HttpUtils;

import net.sf.json.JSONObject;

public class LoginMessageHander extends MessageHander
{
    
    public LoginMessageHander(Message message, MessageProcessorService messageProcessorService)
    {
        super(message, messageProcessorService);
        // TODO Auto-generated constructor stub
    }
    
    @Override
    protected void handleHttpResult(HttpResult httpResult)
    {
        message.setResponsePayLoad(httpResult.getResponse());
        message.setResponseCode(httpResult.getStateCode());
        
        String userStr = httpResult.getResponse();
        if (userStr == null || userStr.trim().equals(""))
        {
            message.setResponsePayLoad("Login Error");
            message.setResponseCode(HttpUtils.RESPONSE_ERROR);
            return;
        }
        JSONObject jsObj = JSONObject.fromObject(userStr);
        User user = (User)JSONObject.toBean(jsObj, User.class);
        Session session = message.getSession();
        session.setUser(user);
        SessionType targetType = messageProcessorService.getSessionTypeFromUser(user);
        boolean ret = messageProcessorService.changeSessionType(session, session.getSessionType(), targetType);
        if (! ret)
        {
            message.setResponsePayLoad("Login Error");
            message.setResponseCode(HttpUtils.RESPONSE_ERROR);
            return;
        }
    }
}
