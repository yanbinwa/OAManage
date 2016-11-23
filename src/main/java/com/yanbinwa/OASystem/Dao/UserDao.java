package com.yanbinwa.OASystem.Dao;

import com.yanbinwa.OASystem.Model.User;

public interface UserDao
{
    User findByName(String name);
    
    void saveUser(User user);
}
