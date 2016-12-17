package com.yanbinwa.OASystem.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;

import javax.annotation.PostConstruct;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.yanbinwa.OASystem.Model.Employee;
import com.yanbinwa.OASystem.Model.EmployeeDynamicInfo;
import com.yanbinwa.OASystem.Model.EmployeeDynamicInfo.CheckinStatus;
import com.yanbinwa.OASystem.Model.Store;
import com.yanbinwa.OASystem.Model.StoreDynamicInfo;
import com.yanbinwa.OASystem.Model.User;
import com.yanbinwa.OASystem.Model.User.AuthType;
import com.yanbinwa.OASystem.Model.User.UserState;
import com.yanbinwa.OASystem.Model.User.UserType;
import com.yanbinwa.OASystem.Model.UserDynamicInfo;
import com.yanbinwa.OASystem.Model.UserDynamicInfo.LoginStatus;

import net.sf.json.JSONObject;


@Service("loginService")
@Transactional
public class LoginServiceImpl implements LoginService
{
    
    @Autowired
    private UserService userService;
    
    @Autowired
    private EmployeeService employeeService;
    
    @Autowired
    private StoreService storeService;
    
    @Autowired
    private PropertyService propertyService;
    
    @Autowired
    private MessageServiceSpring messageServiceSpring;
    
    private AtomicInteger atomicUserId;
    private AtomicInteger atomicEmployeeId;
    private AtomicInteger atomicStoreId = new AtomicInteger(0);
    
    @PostConstruct
    public void init()
    {
        int userIdBak = (Integer) propertyService.getLocalProperty(LoginService.USER_ID_BAK, Integer.class, 0);
        atomicUserId = new AtomicInteger(userIdBak);
        int employeeIdBak = (Integer) propertyService.getLocalProperty(LoginService.EMPLOYEE_ID_BAK, Integer.class, 0);
        atomicEmployeeId = new AtomicInteger(employeeIdBak);
        int storeIdBak = (Integer) propertyService.getLocalProperty(LoginService.STORE_ID_BAK, Integer.class, 0);
        atomicStoreId = new AtomicInteger(storeIdBak);
    }
    
    private int getUserId()
    {
        int userId = atomicUserId.getAndIncrement();
        propertyService.setLocalProperty(LoginService.USER_ID_BAK, atomicUserId, Integer.class);
        return userId;
    }
    
    private int getEmployeeId()
    {
        int employeeId = atomicEmployeeId.getAndIncrement();
        propertyService.setLocalProperty(LoginService.EMPLOYEE_ID_BAK, atomicEmployeeId, Integer.class);
        return employeeId;
    }
    
    private int getStoreId()
    {
        int storeId = atomicStoreId.getAndIncrement();
        propertyService.setLocalProperty(LoginService.STORE_ID_BAK, atomicStoreId, Integer.class);
        return storeId;
    }
    
    private UserType getUserType(String userType)
    {
        if(userType == null)
        {
            return null;
        }
        switch(userType)
        {
        case "employee":
            return UserType.Employee;
        case "store":
            return UserType.Store;
        }
        return null;
    }
    
    private AuthType getUserAuth(String authType)
    {
        if(authType == null)
        {
            return null;
        }
        switch(authType)
        {
        case "普通用户":
            return AuthType.Normal;
        case "管理员用户":
            return AuthType.Admin;
        }
        return null;
    }
    
    private User getUserFromPayLoad(Object userObj) 
    {
        if(userObj == null || !(userObj instanceof JSONObject))
        {
            return null;
        }
        JSONObject userJsonObj = (JSONObject)userObj;
        
        String username = userJsonObj.getString(LoginService.USERNAME);
        User user = userService.findByName(username);
        if(user != null)
        {
            return null;
        }
        
        user = new User();
        user.setId(getUserId());
        user.setName(username);
        
        String password = userJsonObj.getString(LoginService.USERPASSWORD);
        user.setPassword(password);
        
        String userType = userJsonObj.getString(LoginService.USERTYPE);
        user.setUserType(getUserType(userType));
        
        String authType = userJsonObj.getString(LoginService.AUTHTYPE);
        user.setAuthType(getUserAuth(authType));
        
        user.setUserState(UserState.NoneAuthorization);
        
        return user;
    }
    
    private boolean employeeSign(User user, Object employeeObj, int storeId)
    {
        if (user == null || employeeObj == null)
        {
            return false;
        }
        Employee employee = employeeService.vailadeAndGetEmployeeFromPayLoad(employeeObj);
        
        if (employee == null)
        {
            return false;
        }
        employee.setId(getEmployeeId());
        employee.setStoreId(storeId);
        
        user.setUserId(employee.getId());
        UserDynamicInfo userDynamicInfo = createUserDynamicInfo(user);
        userService.saveUser(user);
        userService.saveUserDynamicInfo(userDynamicInfo);
        
        EmployeeDynamicInfo employeeDynamicInfo = createEmployeeDynamicInfo(employee);
        employeeService.saveEmployee(employee);
        employeeService.saveEmployeeDynamicInfo(employeeDynamicInfo);
        return true;
    }
    
    private boolean storeSign(User user, Object storeObj)
    {
        if (user == null || storeObj == null)
        {
            return false;
        }
        Store store = storeService.vailadeAndGetStoreFromPayLoad(storeObj);
        
        if(store == null)
        {
            return false;
        }
        store.setId(getStoreId());
        user.setUserId(store.getId());
        UserDynamicInfo userDynamicInfo = createUserDynamicInfo(user);
        userService.saveUser(user);
        userService.saveUserDynamicInfo(userDynamicInfo);
        
        StoreDynamicInfo storeDynamicInfo = createStoreDynamicInfo(store);
        storeService.saveStore(store);
        storeService.saveStoreDynamicInfo(storeDynamicInfo);
        return true;
    }

    private UserDynamicInfo createUserDynamicInfo(User user)
    {
        UserDynamicInfo userDynamicInfo = new UserDynamicInfo();
        userDynamicInfo.setId(user.getId());
        userDynamicInfo.setLoginTime(-1);
        userDynamicInfo.setLoginStatus(LoginStatus.unLogin);
        user.setUserDynamicInfoId(userDynamicInfo.getId());
        return userDynamicInfo;
    }
    
    private EmployeeDynamicInfo createEmployeeDynamicInfo(Employee employee)
    {
        EmployeeDynamicInfo employeeDynamicInfo = new EmployeeDynamicInfo();
        employeeDynamicInfo.setId(employee.getId());
        employeeDynamicInfo.setCheckinTime(-1);
        employeeDynamicInfo.setCheckinStatus(CheckinStatus.unCheckin);
        employee.setEmployeeDynamicInfoId(employeeDynamicInfo.getId());
        return employeeDynamicInfo;
    }
    
    private StoreDynamicInfo createStoreDynamicInfo(Store store)
    {
        StoreDynamicInfo storeDynamicInfo = new StoreDynamicInfo();
        storeDynamicInfo.setId(store.getId());
        store.setStoreDynamicInfoId(storeDynamicInfo.getId());
        return storeDynamicInfo;
    }
    
    @Override
    public User userSign(JSONObject payLoad)
    {
        // TODO Auto-generated method stub
        if(payLoad == null)
        {
            return null;
        }
        
        Object userObj = payLoad.get("user");
        User user = getUserFromPayLoad(userObj);
        if (user == null)
        {
            return null;
        }
        
        boolean ret = false;
        if (user.getUserType() == UserType.Employee)
        {
            Object employeeObj = payLoad.get("employee");
            int storeId = payLoad.getInt("storeId");
            ret = employeeSign(user, employeeObj, storeId);
        }
        else if(user.getUserType() == UserType.Store)
        {
            Object storeObj = payLoad.get("store");
            ret = storeSign(user, storeObj);
        }
        
        if (ret)
        {
            return user;
        }
        else
        {
            return null;
        }
    }

    @Override
    public User userLogin(JSONObject payLoad)
    {
        // TODO Auto-generated method stub
        String username = payLoad.getString(LoginService.USERNAME);
        User user = userService.findByName(username);
        if (user == null)
        {
            return null;
        }
        
        if (user.getUserState() != UserState.Authorization)
        {
            return null;
        }
        
        String userPassword = payLoad.getString(LoginService.USERPASSWORD);
        if (!userPassword.equals(user.getPassword()))
        {
            return null;
        }
        
        String userTypeStr = payLoad.getString(LoginService.USERTYPE);
        if (userTypeStr == null)
        {
            return null;
        }
        UserType userType = getUserType(userTypeStr);
        if (userType != user.getUserType())
        {
            return null;
        }
        
        String authTypeStr = payLoad.getString(LoginService.AUTHTYPE);
        if (authTypeStr == null)
        {
            return null;
        }
        AuthType authType = getUserAuth(authTypeStr);
        if (authType != user.getAuthType())
        {
            return null;
        }
        
        userService.userLogin(user);
        return user;
    }

    @Override
    public String changePassword(JSONObject payLoad)
    {
        // TODO Auto-generated method stub
        int id = payLoad.getInt(LoginService.ID);
        String oldPassword = payLoad.getString(LoginService.OLD_PASSWORD);
        String newPassword = payLoad.getString(LoginService.NEW_PASSWORD);
        return userService.changePassword(id, oldPassword, newPassword);
    }

    @Override
    public String userLogout(JSONObject payLoad)
    {
        // TODO Auto-generated method stub
        String sessionId = payLoad.getString(LoginService.SESSION_ID);
        User user = messageServiceSpring.getUserBySessionId(sessionId);
        if (user != null)
        {
            userService.userLogout(user);
        }
        return "";
    }

    @Override
    public String verifyUserSign(List<JSONObject> payLoad)
    {
        // TODO Auto-generated method stub
        List<User> userList = new ArrayList<User>();
        for(JSONObject jsObj : payLoad)
        {
            User user = (User)JSONObject.toBean(jsObj, User.class);
            userList.add(user);
        }
        return userService.verifyUserSign(userList);
    }

}
