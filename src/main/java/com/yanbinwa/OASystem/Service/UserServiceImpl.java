package com.yanbinwa.OASystem.Service;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

import javax.annotation.PostConstruct;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.yanbinwa.OASystem.Dao.UserDao;
import com.yanbinwa.OASystem.Dao.UserDynamicInfoDao;
import com.yanbinwa.OASystem.Model.User;
import com.yanbinwa.OASystem.Model.User.UserState;
import com.yanbinwa.OASystem.Model.User.UserType;
import com.yanbinwa.OASystem.Model.UserDynamicInfo;
import com.yanbinwa.OASystem.Model.UserDynamicInfo.LoginStatus;
import com.yanbinwa.OASystem.Utils.CacheUtils;

@Service("userService")
@Transactional
public class UserServiceImpl implements UserService
{
    
    private boolean isCache = false;
    
    @Autowired
    private UserDao userDao;
    
    @Autowired
    private UserDynamicInfoDao userDynamicInfoDao;
    
    @Autowired
    private StoreService storeService;
    
    @Autowired
    CacheService cacheService;
    
    @Autowired
    private PropertyService propertyService;
    
    @PostConstruct
    public void init()
    {
        isCache = (Boolean)propertyService.getProperty(SERVICE_ISCACHE, Boolean.class);
    }
    
    /** ------------------- Dao --------------------- */
    
    @Override
    public User findUserByName(String name)
    {
        // TODO Auto-generated method stub
        return userDao.findUserByName(name);
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
    public List<User> findStoreUser()
    {
        // TODO Auto-generated method stub
        return userDao.findStoreUser();
    }

    @Override
    public User findUserById(int id)
    {
        // TODO Auto-generated method stub
        if (isCache)
        {
            return findUserByIdWithCache(id);
        }
        return userDao.findUserById(id);
    }
    
    private User findUserByIdWithCache(int id)
    {
        String key = SERVICE_CACHE_KEY + "_" + USER_CACHE_KEY + "_" + id;
        String userStr = cacheService.getFromCache(key);
        if (userStr != null)
        {
            User userCache = (User)CacheUtils.convertObjectStrToObject(userStr, User.class);
            return userCache;
        }
        User user = userDao.findUserById(id);
        if (user == null)
        {
            return null;
        }
        userStr = CacheUtils.convertObjectToObjectStr(user);
        cacheService.putInCache(key, userStr);
        return user;
    }

    @Override
    public UserDynamicInfo findUserDynamicInfoById(int id)
    {
        // TODO Auto-generated method stub
        if (isCache)
        {
            return findUserDynamicInfoByIdWithCache(id);
        }
        return userDynamicInfoDao.findUserDynamicInfoById(id);
    }
    
    private UserDynamicInfo findUserDynamicInfoByIdWithCache(int id)
    {
        String key = SERVICE_CACHE_KEY + "_" + USERDYNAMICINFO_CACHE_KEY + "_" + id;
        String userDynamicInfoStr = cacheService.getFromCache(key);
        if (userDynamicInfoStr != null)
        {
            UserDynamicInfo userDynamicInfoCache = (UserDynamicInfo)CacheUtils.convertObjectStrToObject(userDynamicInfoStr, UserDynamicInfo.class);
            return userDynamicInfoCache;
        }
        UserDynamicInfo userDynamicInfo = userDynamicInfoDao.findUserDynamicInfoById(id);
        if (userDynamicInfo == null)
        {
            return null;
        }
        userDynamicInfoStr = CacheUtils.convertObjectToObjectStr(userDynamicInfo);
        cacheService.putInCache(key, userDynamicInfoStr);
        return userDynamicInfo;
    }
    
    @Override
    public void saveUserDynamicInfo(UserDynamicInfo userDynamicInfo)
    {
        // TODO Auto-generated method stub
        if (isCache)
        {
            saveUserDynamicInfoWithCache(userDynamicInfo);
            return;
        }
        userDynamicInfoDao.saveUserDynamicInfo(userDynamicInfo);
    }
    
    private void saveUserDynamicInfoWithCache(UserDynamicInfo userDynamicInfo)
    {
        userDynamicInfoDao.saveUserDynamicInfo(userDynamicInfo);
        String key = SERVICE_CACHE_KEY + "_" + USERDYNAMICINFO_CACHE_KEY + "_" + userDynamicInfo.getId();
        String userDynamicInfoStr = CacheUtils.convertObjectToObjectStr(userDynamicInfo);
        cacheService.putInCache(key, userDynamicInfoStr);
    }

    /** ---------------------------------------------- */
    
    @Override
    public String changePassword(int id, String oldPassword, String newPassword)
    {
        // TODO Auto-generated method stub
        User user = this.findUserById(id);
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
            User user = this.findUserById(userTmp.getId());
            user.setUserState(UserState.Authorization);
            if (user.getUserType() == UserType.Store) 
            {
                storeService.signStoreById(user.getUserId());
            }
        }
        return "";
    }

    @Override
    public String userLogin(User user)
    {
        // TODO Auto-generated method stub
        UserDynamicInfo userDynamicInfo = this.findUserDynamicInfoById(user.getUserDynamicInfoId());
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
        UserDynamicInfo userDynamicInfo = this.findUserDynamicInfoById(user.getUserDynamicInfoId());
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

    @Override
    public void loadUserToLoactionMap()
    {
        // TODO Auto-generated method stub
        List<User> userList = this.findStoreUser();
        Set<Integer> storeIdSet = new HashSet<Integer>();
        for (User user : userList)
        {
            if (user.getUserState() == UserState.NoneAuthorization)
            {
                continue;
            }
            storeIdSet.add(user.getUserId());
        }
        storeService.loadStoreToLoactionMap(storeIdSet);
    }

}
