package com.yanbinwa.OASystem.Dao;

import com.yanbinwa.OASystem.Model.EmployeeDynamicInfo;

public interface EmployeeDynamicInfoDao
{
    EmployeeDynamicInfo findEmployeeDynamicInfoById(int id);
    
    void saveEmployeeDynamicInfo(EmployeeDynamicInfo employeeDynamicInfo);
}
