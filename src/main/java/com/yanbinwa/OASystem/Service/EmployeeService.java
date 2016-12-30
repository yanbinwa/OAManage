package com.yanbinwa.OASystem.Service;

import java.util.List;

import com.yanbinwa.OASystem.Model.Employee;
import com.yanbinwa.OASystem.Model.EmployeeDynamicInfo;

import net.sf.json.JSONObject;

public interface EmployeeService
{
    public static final String SERVICE_CACHE_KEY = "employeeService";
    public static final String EMPLOYEE_CACHE_KEY = "employee";
    public static final String EMPLOYEEDYNAMICINFO_CACHE_KEY = "employeeDynamicInfo";
    public static final String SERVICE_ISCACHE = "Employee_isCache";
    
    public static final String EMPLOYEE_ID = "id";
    public static final String EMPLOYEE_NAME = "name";
    public static final String EMPLOYEE_SEX = "sex";
    public static final String EMPLOYEE_AGE = "age";
    public static final String EMPLOYEE_BIRTHDAY = "birthday";
    public static final String EMPLOYEE_TEL = "tel";
    public static final String EMPLOYEE_IDENTITY_ID = "ID";

    public static final String BARCODE = "barcode";
    public static final String CHECKIN_ACTION = "checkinAction";
    public static final String CHECKIN_ACTION_CHECKIN = "checkin";
    public static final String CHECKIN_ACTION_CHECKOUT = "checkout";
    public static final String RESPONSE_STATE = "responseState";
    public static final String RESPONSE_PAYLOAD = "responsePayload";
    
    Employee findEmployeeById(int id);
    
    void saveEmployee(Employee employee);

    List<Employee> findAllEmployees();
    
    void deleteEmployee(Employee employee);
    
    EmployeeDynamicInfo findEmployeeDynamicInfoById(int id);
    
    List<Employee> findEmployeesByStoreId(int storeId);
        
    void saveEmployeeDynamicInfo(EmployeeDynamicInfo employeeDynamicInfo);
    
    Employee vailadeAndGetEmployeeFromPayLoad(Object employeeObj);
    
    JSONObject employeeCheckin(JSONObject payLoad);
            
    String getEmployeeInfoByStoreId(int storeId);
}
