package com.yanbinwa.OASystem.Dao;

import java.util.List;

import com.yanbinwa.OASystem.Model.User;

public interface UserDao
{
    User findByName(String name);
    
    void saveUser(User user);
    
    List<User> findNoneAuthorizationUser();
    
    User findById(int id);
}
