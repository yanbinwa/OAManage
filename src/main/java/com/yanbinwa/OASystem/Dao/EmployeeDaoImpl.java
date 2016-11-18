package com.yanbinwa.OASystem.Dao;

import java.util.List;

import org.hibernate.Criteria;
import org.springframework.stereotype.Repository;

import com.yanbinwa.OASystem.Model.Employee;

@Repository("employeeDao")
public class EmployeeDaoImpl extends AbstractDao<Integer, Employee> implements EmployeeDao
{

    @Override
    public Employee findById(int id)
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

}
