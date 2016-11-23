package com.yanbinwa.OASystem.Service;

import java.util.List;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.yanbinwa.OASystem.Dao.EmployeeDao;
import com.yanbinwa.OASystem.Model.Employee;

import net.sf.json.JSONObject;

@Service("employeeService")
@Transactional
public class EmployeeServiceImpl implements EmployeeService
{

    private static final Logger logger = Logger.getLogger(EmployeeServiceImpl.class);
    
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

    @Override
    public Employee vailadeAndGetEmployeeFromPayLoad(Object employeeObj)
    {
        // TODO Auto-generated method stub
        if(employeeObj == null || !(employeeObj instanceof JSONObject))
        {
            return null;
        }
        JSONObject employeeJsonObj = (JSONObject) employeeObj;
        Employee employee = new Employee();
        String name = employeeJsonObj.getString(EmployeeService.EMPLOYEE_NAME);
        if (name == null || name.trim() == "")
        {
            logger.error("Employ name should not be empty");
            return null;
        }
        employee.setName(name);
        String sex = employeeJsonObj.getString(EmployeeService.EMPLOYEE_SEX);
        if (sex == null || (!sex.trim().equals("男") && !sex.trim().equals("女")))
        {
            logger.error("Employ sex is empty or invalidate");
            return null;
        }
        employee.setSex(sex);
        
        long birthday = employeeJsonObj.getLong(EmployeeService.EMPLOYEE_BIRTHDAY);
        employee.setBirthday(birthday);
        
        int age = employeeJsonObj.getInt(EmployeeService.EMPLOYEE_AGE);
        employee.setAge(age);
        
        String tel = employeeJsonObj.getString(EmployeeService.EMPLOYEE_TEL);
        if (tel == null || tel.trim() == "")
        {
            logger.error("Employ tel should not be empty");
            return null;
        }
        employee.setTel(tel);
        
        String identityId = employeeJsonObj.getString(EmployeeService.EMPLOYEE_IDENTITY_ID);
        if (identityId == null || identityId.trim() == "")
        {
            logger.error("Employ identity should not be empty");
            return null;
        }
        employee.setIdentityId(identityId);
        
        return employee;
    }

}
