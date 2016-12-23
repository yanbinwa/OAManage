package com.yanbinwa.OASystem.Message;

import com.yanbinwa.OASystem.Service.EmployeeService;
import com.yanbinwa.OASystem.Service.MessageProcessorService;
import com.yanbinwa.OASystem.Utils.HttpUtils;

import net.sf.json.JSONObject;


public class CheckinMessageHander extends MessageHander
{
    
    public CheckinMessageHander(Message message, MessageProcessorService messageProcessorService)
    {
        super(message, messageProcessorService);
        // TODO Auto-generated constructor stub
    }
    
    private void handleCheckinMessage(String responsePayload) 
    {
        JSONObject reponseObj = JSONObject.fromObject(responsePayload);
        int responseCode = reponseObj.getInt(EmployeeService.RESPONSE_STATE);
        if (responseCode == HttpUtils.RESPONSE_ERROR)
        {
            message.setResponseCode(HttpUtils.RESPONSE_ERROR);
        }
        message.setResponsePayLoad(reponseObj.getString(EmployeeService.RESPONSE_PAYLOAD));
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
        
        handleCheckinMessage(httpResult.getResponse());
    }
}
