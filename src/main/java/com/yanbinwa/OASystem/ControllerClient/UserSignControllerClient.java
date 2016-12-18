package com.yanbinwa.OASystem.ControllerClient;

import com.yanbinwa.OASystem.Message.HttpResult;
import com.yanbinwa.OASystem.Model.Store;
import com.yanbinwa.OASystem.Model.User;
import com.yanbinwa.OASystem.Model.User.AuthType;
import com.yanbinwa.OASystem.Model.User.UserType;
import com.yanbinwa.OASystem.Utils.HttpUtils;

import net.sf.json.JSONObject;

public class UserSignControllerClient
{
    public static final String SignRootUrl = "http://localhost:8080/OAManage/servlet/login";
    
    public static void signStore(User user, Store store)
    {
        String url = SignRootUrl + "/userSign";
        
        JSONObject payload = new JSONObject();
        JSONObject userObj = new JSONObject();
        JSONObject storeObj = new JSONObject();
        
        userObj.put("username", user.getName());
        userObj.put("password", user.getPassword());
        userObj.put("userType", "store");
        if (user.getAuthType() == AuthType.Normal)
        {
            userObj.put("authType", "普通用户");
        }
        else
        {
            userObj.put("authType", "管理员用户");
        }
        payload.put("user", userObj);
        
        storeObj.put("name", store.getName());
        storeObj.put("address", store.getAddress());
        storeObj.put("tel", store.getTel());
        storeObj.put("provinceId", store.getProvinceId());
        storeObj.put("cityId", store.getCityId());
        storeObj.put("areaId", store.getAreaId());
        payload.put("store", storeObj);
        
        HttpResult ret = null;
        try
        {
            ret = HttpUtils.httpRequest(url, payload.toString(), "POST");
            System.out.println("Response is: " + ret.getResponse());
        } 
        catch (Exception e)
        {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }
    
    public static void main(String[] args) 
    {
        Store store = new Store("CISCO", "宜山路926号", "13222085556", "310000", "310100", "310104");
        User user = new User("CISCO", "cisco123", UserType.Store, AuthType.Normal);
        signStore(user, store);
    }
}
