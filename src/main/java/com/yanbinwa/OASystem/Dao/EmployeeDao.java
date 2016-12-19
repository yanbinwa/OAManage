package com.yanbinwa.OASystem.Dao;

import java.util.List;

import com.yanbinwa.OASystem.Model.Employee;


public interface EmployeeDao
{
    Employee findById(int id);

    void saveEmployee(Employee employee);
    
    List<Employee> findAllEmployees();
    
    void deleteEmployee(Employee employee);
    
    List<Employee> findEmployeesByStoreId(int storeId);
}
