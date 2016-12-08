package com.yanbinwa.OASystem.Service;

import java.util.List;

import com.yanbinwa.OASystem.Model.User;

public interface UserService
{
    User findByName(String name);
    
    void saveUser(User user);
    
    List<User> findNoneAuthorizationUser();
    
    String changePassword(int id, String oldPassword, String newPassword);
    
    String verifyUserSign(List<User> userList);
}
