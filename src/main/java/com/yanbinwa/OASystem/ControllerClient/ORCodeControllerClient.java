package com.yanbinwa.OASystem.ControllerClient;

import com.yanbinwa.OASystem.Message.HttpResult;
import com.yanbinwa.OASystem.Utils.HttpUtils;

public class ORCodeControllerClient
{
    public static final String ORCodeRootUrl = "http://localhost:8080/OAManage/servlet/ORCode";
    
    public static void getORCodeRootUrl()
    {
        String url = ORCodeRootUrl + "/getORCodeKey";
        HttpResult ret = null;
        try
        {
            ret = HttpUtils.httpRequest(url, null, "GET");
            System.out.println("Response is: " + ret.getResponse());
        } 
        catch (Exception e)
        {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }
    
    public static void main(String[] args)
    {
        getORCodeRootUrl();
    }
}
