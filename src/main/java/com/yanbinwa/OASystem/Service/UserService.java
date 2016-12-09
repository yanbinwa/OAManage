package com.yanbinwa.OASystem.Service;

import java.util.List;

import com.yanbinwa.OASystem.Model.User;
import com.yanbinwa.OASystem.Model.UserDynamicInfo;

public interface UserService
{
    User findByName(String name);
    
    void saveUser(User user);
    
    void saveUserDynamicInfo(UserDynamicInfo userDynamicInfo);
    
    List<User> findNoneAuthorizationUser();
    
    String changePassword(int id, String oldPassword, String newPassword);
    
    String verifyUserSign(List<User> userList);
    
    String userLogin(User user);
    
    String userLogout(User user);
}
