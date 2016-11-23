package com.yanbinwa.OASystem.Service;

import net.sf.json.JSONObject;

public interface LoginService
{
    public static final String ID = "id";
    public static final String USERNAME = "username";
    public static final String USERPASSWORD = "password";
    public static final String USERTYPE = "userType";
    public static final String AUTHTYPE = "authType";
    public static final String USERSTATE = "userState";
    public static final String USERID = "userId";
    
    public boolean userSign(JSONObject payLoad);
}
