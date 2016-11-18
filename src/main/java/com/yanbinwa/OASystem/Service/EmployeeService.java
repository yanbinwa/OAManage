package com.yanbinwa.OASystem.Service;

import java.util.List;

import com.yanbinwa.OASystem.Model.Employee;

public interface EmployeeService
{
    Employee findById(int id);
    
    void saveEmployee(Employee employee);

    List<Employee> findAllEmployees();
    
    void deleteEmployee(Employee employee);
}
