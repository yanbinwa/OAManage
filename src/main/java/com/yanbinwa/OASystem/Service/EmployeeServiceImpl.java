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
import com.yanbinwa.OASystem.Model.Store;
import com.yanbinwa.OASystem.Model.User;
import com.yanbinwa.OASystem.Model.User.UserState;
import com.yanbinwa.OASystem.Utils.HttpUtils;

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
    
    @Autowired
    private StoreService storeService;
    
    @Autowired
    private ORCodeService oRCodeService; 
    
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
    public JSONObject employeeCheckin(JSONObject payLoad)
    {
        // TODO Auto-generated method stub
        JSONObject response = new JSONObject();
        response.put(RESPONSE_STATE, HttpUtils.RESPONSE_ERROR);
        if (payLoad == null)
        {
            response.put(RESPONSE_PAYLOAD, "employeeCheckin payLoad is empty");
            return response;
        }
        String barcodeStr = payLoad.getString(BARCODE);
        if (barcodeStr == null) 
        {
            response.put(RESPONSE_PAYLOAD, "employeeCheckin barcode is empty");
            return response;
        }
        String[] barcodeStrList = barcodeStr.split("_");
        if (barcodeStrList.length != 2)
        {
            response.put(RESPONSE_PAYLOAD, "employeeCheckin barcode is invaild");
            return response;
        }
        String oRCodeKey = barcodeStrList[0].trim();
        
        if (!oRCodeKey.equals(oRCodeService.getORCodeKey()))
        {
            response.put(RESPONSE_PAYLOAD, "barcode key is invaild");
            return response;
        }
        
        int storeId = Integer.valueOf(barcodeStrList[1]);
        Store store = storeService.findById(storeId);
        if (store == null)
        {
            response.put(RESPONSE_PAYLOAD, "store is invaild");
            return response;
        }
        
        int employeeId = payLoad.getInt(EMPLOYEE_ID);
        Employee employee = employeeDao.findById(employeeId);
        if (employee == null)
        {
            response.put(RESPONSE_PAYLOAD, "employee is not find");
            return response;
        }
        EmployeeDynamicInfo employeeDynamicInfo = employeeDynamicInfoDao.findById(employee.getEmployeeDynamicInfoId());
        if (employeeDynamicInfo == null)
        {
            response.put(RESPONSE_PAYLOAD, "employee dynamic info is not find");
            return response;
        }
        
        String action = payLoad.getString(CHECKIN_ACTION);
        if (action == null)
        {
            response.put(RESPONSE_PAYLOAD, "employeeCheckin action is empty");
            return response;
        }
        
        if (action.trim().equals(CHECKIN_ACTION_CHECKIN))
        {
            return checkin(employeeDynamicInfo, store, response);
        }
        else if(action.trim().equals(CHECKIN_ACTION_CHECKOUT))
        {
            return checkout(employeeDynamicInfo, store, response);
        }
        else
        {
            response.put(RESPONSE_PAYLOAD, "employeeCheckin action is vaild");
            return response;
        }
    }
    
    private JSONObject checkin(EmployeeDynamicInfo employeeDynamicInfo, Store store, JSONObject response)
    {
        CheckinStatus checkinStatus = employeeDynamicInfo.getCheckinStatus();
        if (checkinStatus == CheckinStatus.checkin)
        {
            int storeId = employeeDynamicInfo.getCheckinStoreId();
            String storeName = "";
            if (storeId == store.getId())
            {
                storeName = store.getName();
            }
            else if (storeId == -1)
            {
                storeName = "";
            }
            else
            {
                storeName = storeService.findById(storeId).getName();
            }
            response.put(RESPONSE_PAYLOAD, "Has already checkin at " + storeName);
            return response;
        }
        employeeDynamicInfo.setCheckinTime(System.currentTimeMillis());
        employeeDynamicInfo.setCheckinStatus(CheckinStatus.checkin);
        employeeDynamicInfo.setCheckinStoreId(store.getId());
        response.put(RESPONSE_STATE, HttpUtils.RESPONSE_OK);
        response.put(RESPONSE_PAYLOAD, store.getName());
        return response;
    }
    
    private JSONObject checkout(EmployeeDynamicInfo employeeDynamicInfo, Store store, JSONObject response)
    {
        CheckinStatus checkinStatus = employeeDynamicInfo.getCheckinStatus();
        if (checkinStatus == CheckinStatus.unCheckin)
        {
            response.put(RESPONSE_PAYLOAD, "Not checkin yet");
            return response;
        }
        employeeDynamicInfo.setCheckoutTime(System.currentTimeMillis());
        employeeDynamicInfo.setCheckinStatus(CheckinStatus.unCheckin);
        employeeDynamicInfo.setCheckinStoreId(-1);
        response.put(RESPONSE_STATE, HttpUtils.RESPONSE_OK);
        response.put(RESPONSE_PAYLOAD, store.getName());
        return response;
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
