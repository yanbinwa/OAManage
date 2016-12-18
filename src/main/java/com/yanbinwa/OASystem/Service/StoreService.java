package com.yanbinwa.OASystem.Service;

import java.util.List;
import java.util.Set;

import com.yanbinwa.OASystem.Model.Store;
import com.yanbinwa.OASystem.Model.StoreDynamicInfo;

public interface StoreService
{   
    public static final String STORE_ID = "id";
    public static final String STORE_NAME = "name";
    public static final String STORE_ADDRESS = "address";
    public static final String STORE_TEL = "tel";
    public static final String STORE_PROVINCE_ID = "provinceId";
    public static final String STORE_CITY_ID = "cityId";
    public static final String STORE_AREA_ID = "areaId";
    
    Store findById(int id);
    
    void saveStore(Store store);

    List<Store> findAllStores();
    
    void deleteStore(Store store);
    
    Store vailadeAndGetStoreFromPayLoad(Object storeObj);
    
    void saveStoreDynamicInfo(StoreDynamicInfo storeDynamicInfo);
    
    void signStoreById(int id);
    
    String getStoreProvince();
    
    String getStoreCityByProvinceId(String provinceId);
    
    String getStoreAreaByCityId(String cityId);
    
    String getStoreByAreaId(String areaId);
    
    void loadStoreToLoactionMap(Set<Integer> storeIdSet);
    
}
