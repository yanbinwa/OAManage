package com.yanbinwa.OASystem.Controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import com.yanbinwa.OASystem.Model.Store;
import com.yanbinwa.OASystem.Service.StoreService;

@RestController("/store")
public class StoreController
{
    @Autowired
    StoreService storeService;
    
    public static final String STORE_ROOT_URL = "/store";
    
    @RequestMapping(value = STORE_ROOT_URL + "/getAllStore", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
    public @ResponseBody List<Store> listAllStore() {
        List<Store> storelist = storeService.findAllStores();
        return storelist;
    }
    
    @RequestMapping(value = STORE_ROOT_URL + "/getStore/{id}", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
    public @ResponseBody Store getStoreById(@PathVariable("id") int id)
    {
        Store store = storeService.findById(id);
        return store;
    }
    
    @RequestMapping(value = STORE_ROOT_URL + "/createStore", method = RequestMethod.POST)
    public @ResponseBody Store createStore(@RequestBody Store store) 
    {
        storeService.saveStore(store);
        return store;
    }
    
    @RequestMapping(value = STORE_ROOT_URL + "/deleteStore", method = RequestMethod.DELETE)
    public @ResponseBody Store deleteStore(@RequestBody Store store) 
    {
        storeService.deleteStore(store);
        return store;
    }
}
