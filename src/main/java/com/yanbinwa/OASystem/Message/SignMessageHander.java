package com.yanbinwa.OASystem.Message;

import com.yanbinwa.OASystem.Event.Event;
import com.yanbinwa.OASystem.Event.Event.EventType;
import com.yanbinwa.OASystem.Model.User;
import com.yanbinwa.OASystem.Service.EventService;
import com.yanbinwa.OASystem.Service.MessageProcessorService;
import com.yanbinwa.OASystem.Utils.HttpUtils;

import net.sf.json.JSONObject;

public class SignMessageHander extends MessageHander
{

    public SignMessageHander(Message message, MessageProcessorService messageProcessorService)
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
            message.setResponsePayLoad("Sign Error");
            message.setResponseCode(HttpUtils.RESPONSE_ERROR);
            return;
        }
        JSONObject jsObj = JSONObject.fromObject(userStr);
        User user = (User)JSONObject.toBean(jsObj, User.class);
        Event event = new Event(EventType.Sign, user, EventService.NOTIFICATION_SERVICE);
        messageProcessorService.sendEvent(event);
    }

}
