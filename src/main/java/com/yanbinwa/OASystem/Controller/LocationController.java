package com.yanbinwa.OASystem.Controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import com.yanbinwa.OASystem.Service.LocationService;
import com.yanbinwa.OASystem.Service.PropertyService;

@RestController("/location")
public class LocationController
{
    @Autowired
    LocationService locationService;
    
    public static final String LOCATION_ROOT_URL = "/location";
    
    @RequestMapping(value = LOCATION_ROOT_URL + "/getProvinceList", method = RequestMethod.GET, produces = PropertyService.RESPONSE_JSON_UTF8)
    public @ResponseBody String getProvinceList() {
        return locationService.getProvinceList(); 
    }
    
    @RequestMapping(value = LOCATION_ROOT_URL + "/getCityList/{id}", method = RequestMethod.GET, produces = PropertyService.RESPONSE_JSON_UTF8)
    public @ResponseBody String getCityList(@PathVariable("id") String id) {
        return locationService.getCityList(id);
    }
    
    @RequestMapping(value = LOCATION_ROOT_URL + "/getAreaList/{id}", method = RequestMethod.GET, produces = PropertyService.RESPONSE_JSON_UTF8)
    public @ResponseBody String getAreaList(@PathVariable("id") String id) {
        return locationService.getAreaList(id);
    }
}
