package com.yanbinwa.OASystem.Service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.yanbinwa.OASystem.Dao.UserDao;
import com.yanbinwa.OASystem.Model.User;

@Service("userService")
@Transactional
public class UserServiceImpl implements UserService
{
    
    @Autowired
    private UserDao dao;

    @Override
    public User findByName(String name)
    {
        // TODO Auto-generated method stub
        return dao.findByName(name);
    }

    @Override
    public void saveUser(User user)
    {
        // TODO Auto-generated method stub
        dao.saveUser(user);
    }

    @Override
    public List<User> findNoneAuthorizationUser()
    {
        // TODO Auto-generated method stub
        return dao.findNoneAuthorizationUser();
    }

    @Override
    public String changePassword(int id, String oldPassword, String newPassword)
    {
        // TODO Auto-generated method stub
        User user = dao.findById(id);
        if (!user.getPassword().equals(oldPassword))
        {
            return "oldPassword is not correct";
        }
        user.setPassword(newPassword);
        return "";
    }

}
