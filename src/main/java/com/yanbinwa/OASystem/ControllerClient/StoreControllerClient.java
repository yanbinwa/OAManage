package com.yanbinwa.OASystem.ControllerClient;

import com.yanbinwa.OASystem.Message.HttpResult;
import com.yanbinwa.OASystem.Model.Store;
import com.yanbinwa.OASystem.Utils.HttpUtils;

import net.sf.json.JSONObject;

public class StoreControllerClient
{
    
    public static final String StoreRootUrl = "http://localhost:8080/OAManage/servlet/store";
    
    public static void newStore(Store store)
    {
        String url = StoreRootUrl + "/createStore";
        JSONObject jsonObject = JSONObject.fromObject(store);
        HttpResult ret = null;
        try
        {
            ret = HttpUtils.httpRequest(url, jsonObject.toString(), "POST");
            System.out.println("Response is: " + ret.getResponse());
        } 
        catch (Exception e)
        {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }
    
    public static void listAllStore()
    {
        String url = StoreRootUrl + "/getAllStore";
        HttpResult ret = HttpUtils.httpRequest(url, null, "GET");
        System.out.println("Response is: " + ret.getResponse());
    }
    
    public static Store findStoreById(int id)
    {
        String url = StoreRootUrl + "/getStore/" + id;
        HttpResult ret = HttpUtils.httpRequest(url, null, "GET");
        JSONObject jsObj = JSONObject.fromObject(ret.getResponse());
        Store store = (Store)JSONObject.toBean(jsObj, Store.class);
        System.out.println(store);
        return store;
    }
    
    public static void deleteStore(Store store)
    {
        String url = StoreRootUrl + "/deleteStore";
        JSONObject jsonObject = JSONObject.fromObject(store);
        HttpResult ret = null;
        try
        {
            ret = HttpUtils.httpRequest(url, jsonObject.toString(), "DELETE");
            System.out.println("Response is: " + ret.getResponse());
        } 
        catch (Exception e)
        {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }
    
    public static void main(String args[])
    {
        Store store = new Store(1000, "CISCO", "上海市徐汇区宜山路926号新思大楼", "13403408973");
        StoreControllerClient.newStore(store);
        StoreControllerClient.findStoreById(1000);
        StoreControllerClient.listAllStore();
        StoreControllerClient.deleteStore(store);
        StoreControllerClient.listAllStore();
    }
}
