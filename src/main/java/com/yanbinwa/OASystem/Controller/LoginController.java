package com.yanbinwa.OASystem.Controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import com.yanbinwa.OASystem.Model.User;
import com.yanbinwa.OASystem.Service.LoginService;

import net.sf.json.JSONObject;

/**
 * The login controller control the login and sign function
 * 
 * @author yanbinwa
 *
 */

@RestController("/login")
public class LoginController
{
    @Autowired
    LoginService loginService;
    
    public static final String LOGIN_ROOT_URL = "/login";
    
    @RequestMapping(value = LOGIN_ROOT_URL + "/userSign", method = RequestMethod.POST, produces = MediaType.APPLICATION_JSON_VALUE)
    public @ResponseBody User userSign(@RequestBody JSONObject payLoad) 
    {
        return loginService.userSign(payLoad);
    }
    
    @RequestMapping(value = LOGIN_ROOT_URL + "/userLogin", method = RequestMethod.POST, produces = MediaType.APPLICATION_JSON_VALUE)
    public @ResponseBody User userLogin(@RequestBody JSONObject payLoad) 
    {
        return loginService.userLogin(payLoad);
    }
    
    @RequestMapping(value = LOGIN_ROOT_URL + "/userLogout", method = RequestMethod.POST, produces = MediaType.APPLICATION_JSON_VALUE)
    public @ResponseBody String userLogout(@RequestBody JSONObject payLoad) 
    {
        return loginService.userLogout(payLoad);
    }
    
    @RequestMapping(value = LOGIN_ROOT_URL + "/changePassword", method = RequestMethod.POST, produces = MediaType.APPLICATION_JSON_VALUE)
    public @ResponseBody String changePassword(@RequestBody JSONObject payLoad) 
    {
        return loginService.changePassword(payLoad);
    }
    
}
