package com.yanbinwa.OASystem.Service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.yanbinwa.OASystem.Dao.UserDao;
import com.yanbinwa.OASystem.Dao.UserDynamicInfoDao;
import com.yanbinwa.OASystem.Model.User;
import com.yanbinwa.OASystem.Model.User.UserState;
import com.yanbinwa.OASystem.Model.UserDynamicInfo;
import com.yanbinwa.OASystem.Model.UserDynamicInfo.LoginStatus;

@Service("userService")
@Transactional
public class UserServiceImpl implements UserService
{
    
    @Autowired
    private UserDao userDao;
    
    @Autowired
    private UserDynamicInfoDao userDynamicInfoDao;

    @Override
    public User findByName(String name)
    {
        // TODO Auto-generated method stub
        return userDao.findByName(name);
    }

    @Override
    public void saveUser(User user)
    {
        // TODO Auto-generated method stub
        userDao.saveUser(user);
    }

    @Override
    public List<User> findNoneAuthorizationUser()
    {
        // TODO Auto-generated method stub
        return userDao.findNoneAuthorizationUser();
    }

    @Override
    public String changePassword(int id, String oldPassword, String newPassword)
    {
        // TODO Auto-generated method stub
        User user = userDao.findById(id);
        if (!user.getPassword().equals(oldPassword))
        {
            return "oldPassword is not correct";
        }
        user.setPassword(newPassword);
        return "";
    }

    @Override
    public String verifyUserSign(List<User> userList)
    {
        // TODO Auto-generated method stub
        if (userList == null)
        {
            return "the verify list is null";
        }
        for(User userTmp : userList)
        {
            User user = userDao.findById(userTmp.getId());
            user.setUserState(UserState.Authorization);
        }
        return "";
    }

    @Override
    public void saveUserDynamicInfo(UserDynamicInfo userDynamicInfo)
    {
        // TODO Auto-generated method stub
        userDynamicInfoDao.saveUserDynamicInfo(userDynamicInfo);
    }

    @Override
    public String userLogin(User user)
    {
        // TODO Auto-generated method stub
        UserDynamicInfo userDynamicInfo = userDynamicInfoDao.findById(user.getUserDynamicInfoId());
        if (userDynamicInfo != null)
        {
            userDynamicInfo.setLoginTime(System.currentTimeMillis());
            userDynamicInfo.setLoginStatus(LoginStatus.login);
            return "";
        }
        else
        {
            return "login in error";
        }
    }

    @Override
    public String userLogout(User user)
    {
        // TODO Auto-generated method stub
        UserDynamicInfo userDynamicInfo = userDynamicInfoDao.findById(user.getUserDynamicInfoId());
        if (userDynamicInfo != null)
        {
            userDynamicInfo.setLogoutTime(System.currentTimeMillis());
            userDynamicInfo.setLoginStatus(LoginStatus.unLogin);
            return "";
        }
        else
        {
            return "login out error";
        }
    }

}
