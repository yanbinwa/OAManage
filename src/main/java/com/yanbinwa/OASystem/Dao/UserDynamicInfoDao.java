package com.yanbinwa.OASystem.Dao;

import com.yanbinwa.OASystem.Model.UserDynamicInfo;

public interface UserDynamicInfoDao
{
    UserDynamicInfo findUserDynamicInfoById(int id);
    
    void saveUserDynamicInfo(UserDynamicInfo userDynamicInfo);
}
