package com.yanbinwa.OASystem.Controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import com.yanbinwa.OASystem.Model.Employee;
import com.yanbinwa.OASystem.Service.EmployeeService;

@RestController("/employee")
public class EmployeeController
{
    @Autowired
    EmployeeService employeeService;
    
    public static final String EMPLOYEE_ROOT_URL = "/employee";
    
    @RequestMapping(value = EMPLOYEE_ROOT_URL + "/getAllEmployee", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
    public @ResponseBody List<Employee> listAllEmployee() {
        List<Employee> employeelist = employeeService.findAllEmployees();
        return employeelist;
    }
    
    @RequestMapping(value = EMPLOYEE_ROOT_URL + "/getEmployee/{id}", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
    public @ResponseBody Employee getEmployeeById(@PathVariable("id") int id)
    {
        Employee employee = employeeService.findById(id);
        return employee;
    }
    
    @RequestMapping(value = EMPLOYEE_ROOT_URL + "/createEmployee", method = RequestMethod.POST)
    public @ResponseBody Employee createEmployee(@RequestBody Employee employee) 
    {
        employeeService.saveEmployee(employee);
        return employee;
    }
    
    @RequestMapping(value = EMPLOYEE_ROOT_URL + "/deleteEmployee", method = RequestMethod.DELETE)
    public @ResponseBody Employee deleteEmployee(@RequestBody Employee employee) 
    {
        employeeService.deleteEmployee(employee);
        return employee;
    }
    
    @RequestMapping(value = EMPLOYEE_ROOT_URL + "/employeCheckin", method = RequestMethod.POST)
    public @ResponseBody String employeCheckin(@RequestBody Employee employee)
    {
        return employeeService.employeeCheckin(employee);
    }
    
    @RequestMapping(value = EMPLOYEE_ROOT_URL + "/employeCheckout", method = RequestMethod.POST)
    public @ResponseBody String employeCheckout(@RequestBody Employee employee)
    {
        return employeeService.employeeCheckout(employee);
    }
}
