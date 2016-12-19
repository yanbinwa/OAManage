package com.yanbinwa.OASystem.Service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.yanbinwa.OASystem.Dao.EmployeeDao;
import com.yanbinwa.OASystem.Dao.EmployeeDynamicInfoDao;
import com.yanbinwa.OASystem.Model.Employee;
import com.yanbinwa.OASystem.Model.EmployeeDynamicInfo;
import com.yanbinwa.OASystem.Model.EmployeeDynamicInfo.CheckinStatus;
import com.yanbinwa.OASystem.Model.User;
import com.yanbinwa.OASystem.Model.User.UserState;

import net.sf.json.JSONObject;

@Service("employeeService")
@Transactional
public class EmployeeServiceImpl implements EmployeeService
{

    private static final Logger logger = Logger.getLogger(EmployeeServiceImpl.class);
    
    @Autowired
    private EmployeeDao employeeDao;
    
    @Autowired
    private EmployeeDynamicInfoDao employeeDynamicInfoDao;
    
    @Autowired
    private UserService userService;
    
    @Override
    public Employee findById(int id)
    {
        // TODO Auto-generated method stub
        return employeeDao.findById(id);
    }

    @Override
    public void saveEmployee(Employee employee)
    {
        // TODO Auto-generated method stub
        employeeDao.saveEmployee(employee);
    }

    @Override
    public List<Employee> findAllEmployees()
    {
        // TODO Auto-generated method stub
        return employeeDao.findAllEmployees();
    }

    @Override
    public void deleteEmployee(Employee employee)
    {
        // TODO Auto-generated method stub
        employeeDao.deleteEmployee(employee);
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

    @Override
    public void saveEmployeeDynamicInfo(EmployeeDynamicInfo employeeDynamicInfo)
    {
        // TODO Auto-generated method stub
        employeeDynamicInfoDao.saveEmployeeDynamicInfo(employeeDynamicInfo);
    }

    @Override
    public String employeeCheckin(Employee employee)
    {
        // TODO Auto-generated method stub
        EmployeeDynamicInfo employeeDynamicInfo = employeeDynamicInfoDao.findById(employee.getEmployeeDynamicInfoId());
        if (employeeDynamicInfo != null)
        {
            employeeDynamicInfo.setCheckinTime(System.currentTimeMillis());
            employeeDynamicInfo.setCheckinStatus(CheckinStatus.checkin);
            return "";
        }
        else
        {
            return "Error to checkin";
        }
    }

    @Override
    public String employeeCheckout(Employee employee)
    {
        // TODO Auto-generated method stub
        EmployeeDynamicInfo employeeDynamicInfo = employeeDynamicInfoDao.findById(employee.getEmployeeDynamicInfoId());
        if (employeeDynamicInfo != null)
        {
            employeeDynamicInfo.setCheckoutTime(System.currentTimeMillis());
            employeeDynamicInfo.setCheckinStatus(CheckinStatus.unCheckin);
            return "";
        }
        else
        {
            return "Error to checkout";
        }
    }

    @Override
    public String getEmployeeInfoByStoreId(int storeId)
    {
        // TODO Auto-generated method stub
        Map<String, Map<String, Object>> employeeInfoMap = new HashMap<String, Map<String, Object>>();
        List<Employee> employees = employeeDao.findEmployeesByStoreId(storeId);
        if(employees == null)
        {
            return JSONObject.fromObject(employeeInfoMap).toString();
        }
        for(Employee employee : employees)
        {
            int userId = employee.getUserId();
            User user = userService.findById(userId);
            if (user == null)
            {
                logger.error("getEmployeeInfoByStoreId failed by userId: " + userId);
                continue;
            }
            if (user.getUserState() == UserState.NoneAuthorization)
            {
                continue;
            }
            int employeeDynamicInfoId = employee.getEmployeeDynamicInfoId();
            EmployeeDynamicInfo employeeDynamicInfo = employeeDynamicInfoDao.findById(employeeDynamicInfoId);
            if (employeeDynamicInfo == null)
            {
                logger.error("getEmployeeInfoByStoreId failed by employeeDynamicInfoId: " + employeeDynamicInfoId);
                continue;
            }
            
            int employeeId = employee.getId();
            Map<String, Object> employeeInfo = employeeInfoMap.get(String.valueOf(employeeId));
            if (employeeInfo == null)
            {
                employeeInfo = new HashMap<String, Object>();
                employeeInfoMap.put(String.valueOf(employeeId), employeeInfo);
            }
            employeeInfo.put("employee", employee);
            employeeInfo.put("employeeDynamicInfo", employeeDynamicInfo);
        }
        
        return JSONObject.fromObject(employeeInfoMap).toString();
    }

}
