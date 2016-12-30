package com.yanbinwa.OASystem.Dao;

import java.util.List;

import org.hibernate.Criteria;
import org.hibernate.criterion.Restrictions;
import org.springframework.stereotype.Repository;

import com.yanbinwa.OASystem.Model.Employee;

@Repository("employeeDao")
public class EmployeeDaoImpl extends AbstractDao<Integer, Employee> implements EmployeeDao
{

    @Override
    public Employee findEmployeeById(int id)
    {
        // TODO Auto-generated method stub
        return getByKey(id);
    }

    @Override
    public void saveEmployee(Employee employee)
    {
        // TODO Auto-generated method stub
        persist(employee);
    }

    @SuppressWarnings("unchecked")
    @Override
    public List<Employee> findAllEmployees()
    {
        // TODO Auto-generated method stub
        Criteria criteria = createEntityCriteria();
        return (List<Employee>) criteria.list();
    }

    @Override
    public void deleteEmployee(Employee employee)
    {
        // TODO Auto-generated method stub
        delete(employee);
    }

    @SuppressWarnings("unchecked")
    @Override
    public List<Employee> findEmployeesByStoreId(int storeId)
    {
        // TODO Auto-generated method stub
        Criteria criteria = createEntityCriteria();
        criteria.add(Restrictions.eq("storeId", storeId));
        return (List<Employee>)criteria.list();
    }

}
