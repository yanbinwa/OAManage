package com.yanbinwa.OASystem.Controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import com.yanbinwa.OASystem.Model.Employee;
import com.yanbinwa.OASystem.Service.EmployeeService;
import com.yanbinwa.OASystem.Service.PropertyService;

import net.sf.json.JSONObject;

@RestController("/employee")
public class EmployeeController
{
    @Autowired
    EmployeeService employeeService;
    
    public static final String EMPLOYEE_ROOT_URL = "/employee";
    
    @RequestMapping(value = EMPLOYEE_ROOT_URL + "/getAllEmployee", method = RequestMethod.GET, produces = PropertyService.RESPONSE_JSON_UTF8)
    public @ResponseBody List<Employee> listAllEmployee() {
        List<Employee> employeelist = employeeService.findAllEmployees();
        return employeelist;
    }
    
    @RequestMapping(value = EMPLOYEE_ROOT_URL + "/getEmployee/{id}", method = RequestMethod.GET, produces = PropertyService.RESPONSE_JSON_UTF8)
    public @ResponseBody Employee getEmployeeById(@PathVariable("id") int id)
    {
        Employee employee = employeeService.findById(id);
        return employee;
    }
    
    @RequestMapping(value = EMPLOYEE_ROOT_URL + "/createEmployee", method = RequestMethod.POST, produces = PropertyService.RESPONSE_JSON_UTF8)
    public @ResponseBody Employee createEmployee(@RequestBody Employee employee) 
    {
        employeeService.saveEmployee(employee);
        return employee;
    }
    
    @RequestMapping(value = EMPLOYEE_ROOT_URL + "/deleteEmployee", method = RequestMethod.DELETE, produces = PropertyService.RESPONSE_JSON_UTF8)
    public @ResponseBody Employee deleteEmployee(@RequestBody Employee employee) 
    {
        employeeService.deleteEmployee(employee);
        return employee;
    }
    
    @RequestMapping(value = EMPLOYEE_ROOT_URL + "/employeeCheckin", method = RequestMethod.POST, produces = PropertyService.RESPONSE_JSON_UTF8)
    public @ResponseBody JSONObject employeCheckin(@RequestBody JSONObject payLoad)
    {
        return employeeService.employeeCheckin(payLoad);
    }
    
    
    @RequestMapping(value = EMPLOYEE_ROOT_URL + "/getEmployeeInfoByStoreId/{id}", method = RequestMethod.GET, produces = PropertyService.RESPONSE_JSON_UTF8)
    public @ResponseBody String getEmployeeInfoByStoreId(@PathVariable("id") int id)
    {
        return employeeService.getEmployeeInfoByStoreId(id);
    }
}
