package com.yanbinwa.OASystem.Dao;

import com.yanbinwa.OASystem.Model.EmployeeDynamicInfo;

public interface EmployeeDynamicInfoDao
{
    EmployeeDynamicInfo findById(int id);
    
    void saveEmployeeDynamicInfo(EmployeeDynamicInfo employeeDynamicInfo);
}
