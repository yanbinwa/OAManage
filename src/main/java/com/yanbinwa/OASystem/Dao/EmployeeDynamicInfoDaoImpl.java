package com.yanbinwa.OASystem.Dao;

import org.springframework.stereotype.Repository;

import com.yanbinwa.OASystem.Model.EmployeeDynamicInfo;

@Repository("employeeDynamicInfoDao")
public class EmployeeDynamicInfoDaoImpl extends AbstractDao<Integer, EmployeeDynamicInfo> implements EmployeeDynamicInfoDao
{

    @Override
    public EmployeeDynamicInfo findEmployeeDynamicInfoById(int id)
    {
        // TODO Auto-generated method stub
        return getByKey(id);
    }

    @Override
    public void saveEmployeeDynamicInfo(EmployeeDynamicInfo employeeDynamicInfo)
    {
        // TODO Auto-generated method stub
        persist(employeeDynamicInfo);
    }
    
}
