package com.yanbinwa.OASystem.Service;

import java.util.List;

import com.yanbinwa.OASystem.Model.User;
import com.yanbinwa.OASystem.Model.UserDynamicInfo;

public interface UserService
{
    public static final String SERVICE_CACHE_KEY = "userService";
    public static final String USER_CACHE_KEY = "user";
    public static final String USERDYNAMICINFO_CACHE_KEY = "userDynamicInfo";
    public static final String SERVICE_ISCACHE = "User_isCache";
    
    User findUserByName(String name);
    
    void saveUser(User user);
    
    List<User> findNoneAuthorizationUser();
    
    List<User> findStoreUser();
    
    User findUserById(int id);
        
    UserDynamicInfo findUserDynamicInfoById(int id);
    
    void saveUserDynamicInfo(UserDynamicInfo userDynamicInfo);
    
    String changePassword(int id, String oldPassword, String newPassword);
    
    String verifyUserSign(List<User> userList);
    
    String userLogin(User user);
    
    String userLogout(User user);
    
    void loadUserToLoactionMap();
}
