package com.yanbinwa.OASystem.ControllerClient;

import com.yanbinwa.OASystem.Message.HttpResult;
import com.yanbinwa.OASystem.Model.Employee;
import com.yanbinwa.OASystem.Utils.HttpUtils;

import net.sf.json.JSONObject;

public class EmployeeControllerClient
{
    
//    public static final String EmployeeRootUrl = "http://localhost:8080/OAManage/servlet/employee";
    public static final String EmployeeRootUrl = "https://localhost:8443/OAManage/servlet/employee";
    
    public static void newEmployee(Employee employee)
    {
        String url = EmployeeRootUrl + "/createEmployee";
        JSONObject jsonObject = JSONObject.fromObject(employee);
        HttpResult ret = null;
        try
        {
            ret = HttpUtils.httpRequest(url, jsonObject.toString(), "POST");
            System.out.println("Response is: " + ret.getResponse());
        } 
        catch (Exception e)
        {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }
    
    public static void listAllEmployee()
    {
        String url = EmployeeRootUrl + "/getAllEmployee";
        HttpResult ret = HttpUtils.httpRequest(url, null, "GET");
        System.out.println("Response is: " + ret.getResponse());
    }
    
    public static Employee findEmployeeById(int id)
    {
        String url = EmployeeRootUrl + "/getEmployee/" + id;
        HttpResult ret = HttpUtils.httpRequest(url, null, "GET");
        JSONObject jsObj = JSONObject.fromObject(ret.getResponse());
        Employee employee = (Employee)JSONObject.toBean(jsObj, Employee.class);
        System.out.println(employee);
        return employee;
    }
    
    public static void deleteEmployee(Employee employee)
    {
        String url = EmployeeRootUrl + "/deleteEmployee";
        JSONObject jsonObject = JSONObject.fromObject(employee);
        HttpResult ret = null;
        try
        {
            ret = HttpUtils.httpRequest(url, jsonObject.toString(), "DELETE");
            System.out.println("Response is: " + ret.getResponse());
        } 
        catch (Exception e)
        {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }
    
    public static void main(String args[])
    {   
        Employee employee = new Employee(2, "王彦彬", "男", 1479814082794L, 27, "13222085556", "140109198912211039", 1);
        EmployeeControllerClient.newEmployee(employee);
        EmployeeControllerClient.findEmployeeById(2);
        EmployeeControllerClient.listAllEmployee();
        EmployeeControllerClient.deleteEmployee(employee);
        EmployeeControllerClient.listAllEmployee();
    }
}
