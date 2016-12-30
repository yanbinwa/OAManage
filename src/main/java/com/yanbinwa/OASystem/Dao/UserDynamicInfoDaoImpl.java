package com.yanbinwa.OASystem.Dao;

import org.springframework.stereotype.Repository;

import com.yanbinwa.OASystem.Model.UserDynamicInfo;

@Repository("userDynamicInfoDao")
public class UserDynamicInfoDaoImpl extends AbstractDao<Integer, UserDynamicInfo> implements UserDynamicInfoDao
{

    @Override
    public UserDynamicInfo findUserDynamicInfoById(int id)
    {
        // TODO Auto-generated method stub
        return getByKey(id);
    }

    @Override
    public void saveUserDynamicInfo(UserDynamicInfo userDynamicInfo)
    {
        // TODO Auto-generated method stub
        persist(userDynamicInfo);
    }
    
}
