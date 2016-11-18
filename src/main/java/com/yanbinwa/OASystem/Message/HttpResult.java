package com.yanbinwa.OASystem.Message;

public class HttpResult
{
    private String response = null;
    private int stateCode = -1;
    
    public HttpResult(String response, int stateCode)
    {
        this.response = response;
        this.stateCode = stateCode;
    }
    
    public HttpResult()
    {
        
    }
    
    public String getResponse()
    {
        return response;
    }
    
    public void setResponse(String response)
    {
        this.response = response;
    }
    
    public int getStateCode()
    {
        return stateCode;
    }
    
    public void setStateCode(int stateCode)
    {
        this.stateCode = stateCode;
    }
}
