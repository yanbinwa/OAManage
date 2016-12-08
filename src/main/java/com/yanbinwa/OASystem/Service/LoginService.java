package com.yanbinwa.OASystem.Service;

import java.util.List;

import com.yanbinwa.OASystem.Model.User;

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
    
    public static final String USER_ID_BAK = "Login_UserIdBak";
    public static final String EMPLOYEE_ID_BAK = "Login_EmployeeIdBak";
    public static final String STORE_ID_BAK = "Login_StoreIdBak";
    
    
    public static final String OLD_PASSWORD = "oldPassword";
    public static final String NEW_PASSWORD = "newPassword";
    
    public static final String SESSION_ID = "sessionId";
        
    public User userSign(JSONObject payLoad);
    public User userLogin(JSONObject payLoad);
    public String changePassword(JSONObject payLoad);
    public String userLogout(JSONObject payLoad);
    public String verifyUserSign(List<JSONObject> payLoad);
}
