package com.yanbinwa.OASystem.Service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.yanbinwa.OASystem.Dao.EmployeeDao;
import com.yanbinwa.OASystem.Model.Employee;

@Service("employeeService")
@Transactional
public class EmployeeServiceImpl implements EmployeeService
{

    @Autowired
    private EmployeeDao dao;
    
    
    @Override
    public Employee findById(int id)
    {
        // TODO Auto-generated method stub
        return dao.findById(id);
    }

    @Override
    public void saveEmployee(Employee employee)
    {
        // TODO Auto-generated method stub
        dao.saveEmployee(employee);
    }

    @Override
    public List<Employee> findAllEmployees()
    {
        // TODO Auto-generated method stub
        return dao.findAllEmployees();
    }

    @Override
    public void deleteEmployee(Employee employee)
    {
        // TODO Auto-generated method stub
        dao.deleteEmployee(employee);
    }

}
