package com.yanbinwa.OASystem.Dao;

import org.springframework.stereotype.Repository;

import com.yanbinwa.OASystem.Model.StoreDynamicInfo;

@Repository("storeDynamicInfoDao")
public class StoreDynamicInfoDaoImpl extends AbstractDao<Integer, StoreDynamicInfo> implements StoreDynamicInfoDao
{
    @Override
    public StoreDynamicInfo findById(int id)
    {
        // TODO Auto-generated method stub
        return getByKey(id);
    }

    @Override
    public void saveStoreDynamicInfo(StoreDynamicInfo storeDynamicInfo)
    {
        // TODO Auto-generated method stub
        persist(storeDynamicInfo);
    }
}
