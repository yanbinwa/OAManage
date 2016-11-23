package com.yanbinwa.OASystem.Service;

import java.util.concurrent.atomic.AtomicInteger;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.yanbinwa.OASystem.Dao.UserDao;
import com.yanbinwa.OASystem.Model.Employee;
import com.yanbinwa.OASystem.Model.Store;
import com.yanbinwa.OASystem.Model.User;
import com.yanbinwa.OASystem.Model.User.AuthType;
import com.yanbinwa.OASystem.Model.User.UserState;
import com.yanbinwa.OASystem.Model.User.UserType;

import net.sf.json.JSONObject;


@Service("loginService")
@Transactional
public class LoginServiceImpl implements LoginService
{
    
    @Autowired
    private UserDao dao;
    
    @Autowired
    private EmployeeService employeeService;
    
    @Autowired
    private StoreService storeService;
    
    private AtomicInteger atomicUserId = new AtomicInteger(0);
    private AtomicInteger atomicEmployeeId = new AtomicInteger(0);
    private AtomicInteger atomicStoreId = new AtomicInteger(0);
    
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
        case "管理员":
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
        User user = dao.findByName(username);
        if(user != null)
        {
            return null;
        }
        
        user = new User();
        user.setId(atomicUserId.getAndIncrement());
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
        employee.setId(atomicEmployeeId.incrementAndGet());
        employee.setStoreId(storeId);
        
        user.setUserId(employee.getId());
        dao.saveUser(user);
        employeeService.saveEmployee(employee);
        
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
        store.setId(atomicStoreId.incrementAndGet());
        return false;
    }

    @Override
    public boolean userSign(JSONObject payLoad)
    {
        // TODO Auto-generated method stub
        if(payLoad == null)
        {
            return false;
        }
        
        Object userObj = payLoad.get("user");
        User user = getUserFromPayLoad(userObj);
        if (user == null)
        {
            return false;
        }
        if (user.getUserType() == UserType.Employee)
        {
            Object employeeObj = payLoad.get("employee");
            int storeId = payLoad.getInt("storeId");
            return employeeSign(user, employeeObj, storeId);
        }
        else if(user.getUserType() == UserType.Store)
        {
            Object storeObj = payLoad.get("store");
            return storeSign(user, storeObj);
        }
        
        return false;
    }

}
