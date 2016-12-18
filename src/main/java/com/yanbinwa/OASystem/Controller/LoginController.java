package com.yanbinwa.OASystem.Controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import com.yanbinwa.OASystem.Model.User;
import com.yanbinwa.OASystem.Service.LoginService;
import com.yanbinwa.OASystem.Service.PropertyService;

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
    
    @RequestMapping(value = LOGIN_ROOT_URL + "/userSign", method = RequestMethod.POST, produces = PropertyService.RESPONSE_JSON_UTF8)
    public @ResponseBody User userSign(@RequestBody JSONObject payLoad) 
    {
        return loginService.userSign(payLoad);
    }
    
    @RequestMapping(value = LOGIN_ROOT_URL + "/userLogin", method = RequestMethod.POST, produces = PropertyService.RESPONSE_JSON_UTF8)
    public @ResponseBody User userLogin(@RequestBody JSONObject payLoad) 
    {
        return loginService.userLogin(payLoad);
    }
    
    @RequestMapping(value = LOGIN_ROOT_URL + "/userLogout", method = RequestMethod.POST, produces = PropertyService.RESPONSE_JSON_UTF8)
    public @ResponseBody String userLogout(@RequestBody JSONObject payLoad) 
    {
        return loginService.userLogout(payLoad);
    }
    
    @RequestMapping(value = LOGIN_ROOT_URL + "/changePassword", method = RequestMethod.POST, produces = PropertyService.RESPONSE_JSON_UTF8)
    public @ResponseBody String changePassword(@RequestBody JSONObject payLoad) 
    {
        return loginService.changePassword(payLoad);
    }
    
    @RequestMapping(value = LOGIN_ROOT_URL + "/verifyUserSign", method = RequestMethod.POST, produces = PropertyService.RESPONSE_JSON_UTF8)
    public @ResponseBody String verifyUserSign(@RequestBody List<JSONObject> payLoad) 
    {
        return loginService.verifyUserSign(payLoad);
    }
    
    @RequestMapping(value = LOGIN_ROOT_URL + "/getStoreProvince", method = RequestMethod.GET, produces = PropertyService.RESPONSE_JSON_UTF8)
    public @ResponseBody String getStoreProvince()
    {
        return loginService.getStoreProvince();
    }
    
    @RequestMapping(value = LOGIN_ROOT_URL + "/getStoreCityByProvinceId/{id}", method = RequestMethod.GET, produces = PropertyService.RESPONSE_JSON_UTF8)
    public @ResponseBody String getStoreCityByProvinceId(@PathVariable("id") String id)
    {
        return loginService.getStoreCityByProvinceId(id);
    }
    
    @RequestMapping(value = LOGIN_ROOT_URL + "/getStoreAreaByCityId/{id}", method = RequestMethod.GET, produces = PropertyService.RESPONSE_JSON_UTF8)
    public @ResponseBody String getStoreAreaByCityId(@PathVariable("id") String id)
    {
        return loginService.getStoreAreaByCityId(id);
    }
    
    @RequestMapping(value = LOGIN_ROOT_URL + "/getStoreByAreaId/{id}", method = RequestMethod.GET, produces = PropertyService.RESPONSE_JSON_UTF8)
    public @ResponseBody String getStoreByAreaId(@PathVariable("id") String id)
    {
        return loginService.getStoreByAreaId(id);
    }
    
}
