package com.yanbinwa.OASystem.Service;

import java.util.List;

import com.yanbinwa.OASystem.Model.Employee;

public interface EmployeeService
{
    public static final String EMPLOYEE_ID = "id";
    public static final String EMPLOYEE_NAME = "name";
    public static final String EMPLOYEE_SEX = "sex";
    public static final String EMPLOYEE_AGE = "age";
    public static final String EMPLOYEE_BIRTHDAY = "birthday";
    public static final String EMPLOYEE_TEL = "tel";
    public static final String EMPLOYEE_IDENTITY_ID = "ID";
    
    Employee findById(int id);
    
    void saveEmployee(Employee employee);

    List<Employee> findAllEmployees();
    
    void deleteEmployee(Employee employee);
    
    Employee vailadeAndGetEmployeeFromPayLoad(Object employeeObj);
}
