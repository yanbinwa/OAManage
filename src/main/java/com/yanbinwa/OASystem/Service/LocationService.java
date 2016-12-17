package com.yanbinwa.OASystem.Service;

public interface LocationService
{
    public static final String PROVINCE_INFO_FILE = PropertyService.SYSTEM_RESOURCE_DIR + "/location/province.json";
    public static final String CITY_INFO_FILE = PropertyService.SYSTEM_RESOURCE_DIR + "/location/city.json";
    public static final String AREA_INFO_FILE = PropertyService.SYSTEM_RESOURCE_DIR + "/location/area.json";
    
    String getProvinceList();
    String getCityList(String id);
    String getAreaList(String id);
}
