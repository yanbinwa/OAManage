package com.yanbinwa.OASystem.Controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

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
    public @ResponseBody String userSign(@RequestBody JSONObject payLoad) 
    {
        boolean ret = loginService.userSign(payLoad);
        if (!ret)
        {
            return "user sign error";
        }
        return "";
    }
    
}
