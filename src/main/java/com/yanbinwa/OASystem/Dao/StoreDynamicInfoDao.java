package com.yanbinwa.OASystem.Dao;

import org.springframework.stereotype.Repository;

import com.yanbinwa.OASystem.Model.StoreDynamicInfo;

@Repository("storeDynamicInfoDao")
public interface StoreDynamicInfoDao
{
    StoreDynamicInfo findStoreDynamicInfoById(int id);
    
    void saveStoreDynamicInfo(StoreDynamicInfo storeDynamicInfo);
}
