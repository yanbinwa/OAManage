package com.yanbinwa.OASystem.Service;

import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.annotation.PostConstruct;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.yanbinwa.OASystem.Dao.StoreDao;
import com.yanbinwa.OASystem.Dao.StoreDynamicInfoDao;
import com.yanbinwa.OASystem.Model.Store;
import com.yanbinwa.OASystem.Model.StoreDynamicInfo;
import com.yanbinwa.OASystem.Utils.CacheUtils;

import net.sf.json.JSONObject;

@Service("storeService")
@Transactional
public class StoreServiceImpl implements StoreService
{
    private static final Logger logger = Logger.getLogger(EmployeeServiceImpl.class);
    private boolean isCache = false;
    
    @Autowired
    private StoreDao storeDao;
    
    @Autowired
    private StoreDynamicInfoDao storeDynamicInfoDao;
    
    @Autowired
    private LocationService locationService;
    
    @Autowired
    CacheService cacheService;
    
    @Autowired
    private PropertyService propertyService;
    
    private Map<String, Set<String>> provinceIdToCityIdMap = new HashMap<String, Set<String>>();
    private Map<String, Set<String>> cityIdToAreaIdMap = new HashMap<String, Set<String>>();
    private Map<String, Set<Integer>> areaIdToStoreIdMap = new HashMap<String, Set<Integer>>();
    private Map<Integer, String> storeIdToStoreNameMap = new HashMap<Integer, String>();
    
    @PostConstruct
    public void init()
    {
        isCache = (Boolean)propertyService.getProperty(SERVICE_ISCACHE, Boolean.class);
    }
    
    /* ------------------- Dao --------------------- */
    
    @Override
    public Store findStoreById(int id)
    {
        // TODO Auto-generated method stub
        if (isCache)
        {
            return findStoreByIdWithCache(id);
        }
        return storeDao.findStoreById(id);
    }
    
    private Store findStoreByIdWithCache(int id)
    {
        String key = SERVICE_CACHE_KEY + "_" + STORE_CACHE_KEY + "_" + id;
        String storeStr = cacheService.getFromCache(key);
        if (storeStr != null)
        {
            Store storeCache = (Store)CacheUtils.convertObjectStrToObject(storeStr, Store.class);
            return storeCache;
        }
        Store store = storeDao.findStoreById(id);
        if (store == null)
        {
            return null;
        }
        storeStr = CacheUtils.convertObjectToObjectStr(store);
        cacheService.putInCache(key, storeStr);
        return store;
    }

    @Override
    public void saveStore(Store store)
    {
        // TODO Auto-generated method stub
        if (isCache)
        {
            saveStoreWithCache(store);
            return;
        }
        storeDao.saveStore(store);
    }
    
    private void saveStoreWithCache(Store store)
    {
        storeDao.saveStore(store);
        String key = SERVICE_CACHE_KEY + "_" + STORE_CACHE_KEY + "_" + store.getId();
        String storeStr = CacheUtils.convertObjectToObjectStr(store);
        cacheService.putInCache(key, storeStr);
    }

    @Override
    public List<Store> findAllStores()
    {
        // TODO Auto-generated method stub
        return storeDao.findAllStores();
    }

    @Override
    public void deleteStore(Store store)
    {
        // TODO Auto-generated method stub
        if (isCache)
        {
            deleteStoreWithCache(store);
            return;
        }
        storeDao.deleteStore(store);
    }
    
    private void deleteStoreWithCache(Store store)
    {
        storeDao.deleteStore(store);
        String key = SERVICE_CACHE_KEY + "_" + STORE_CACHE_KEY + "_" + store.getId();
        cacheService.delInCache(key);
    }
    
    @Override
    public void saveStoreDynamicInfo(StoreDynamicInfo storeDynamicInfo)
    {
        // TODO Auto-generated method stub
        if (isCache)
        {
            saveStoreDynamicInfoWithCache(storeDynamicInfo);
            return;
        }
        storeDynamicInfoDao.saveStoreDynamicInfo(storeDynamicInfo);
    }
    
    private void saveStoreDynamicInfoWithCache(StoreDynamicInfo storeDynamicInfo)
    {
        storeDynamicInfoDao.saveStoreDynamicInfo(storeDynamicInfo);
        String key = SERVICE_CACHE_KEY + "_" + STOREDYNAMICINFO_CACHE_KEY + "_" + storeDynamicInfo.getId();
        String storeDynamicInfoStr = CacheUtils.convertObjectToObjectStr(storeDynamicInfo);
        cacheService.putInCache(key, storeDynamicInfoStr);
    }

    @Override
    public StoreDynamicInfo findStoreDynamicInfoById(int id)
    {
        // TODO Auto-generated method stub
        if (isCache)
        {
            return findStoreDynamicInfoByIdWithCache(id);
        }
        return storeDynamicInfoDao.findStoreDynamicInfoById(id);
    }
    
    private StoreDynamicInfo findStoreDynamicInfoByIdWithCache(int id)
    {
        String key = SERVICE_CACHE_KEY + "_" + STOREDYNAMICINFO_CACHE_KEY + "_" + id;
        String storeDynamicInfoStr = cacheService.getFromCache(key);
        if (storeDynamicInfoStr != null)
        {
            StoreDynamicInfo storeDynamicInfoCache = (StoreDynamicInfo)CacheUtils.convertObjectStrToObject(storeDynamicInfoStr, StoreDynamicInfo.class);
            return storeDynamicInfoCache;
        }
        StoreDynamicInfo storeDynamicInfo = storeDynamicInfoDao.findStoreDynamicInfoById(id);
        if (storeDynamicInfo == null)
        {
            return null;
        }
        storeDynamicInfoStr = CacheUtils.convertObjectToObjectStr(storeDynamicInfo);
        cacheService.putInCache(key, storeDynamicInfoStr);
        return storeDynamicInfo;  
    }
    
    /* ----------------------------------------------------- */

    @Override
    public Store vailadeAndGetStoreFromPayLoad(Object storeObj)
    {
        // TODO Auto-generated method stub
        if(storeObj == null || !(storeObj instanceof JSONObject))
        {
            return null;
        }
        JSONObject storeJsonObj = (JSONObject) storeObj;
        Store store = new Store();
        String name = storeJsonObj.getString(StoreService.STORE_NAME);
        if (name == null || name.trim() == "")
        {
            logger.error("Employ name should not be empty");
            return null;
        }
        store.setName(name);
        
        String address = storeJsonObj.getString(StoreService.STORE_ADDRESS);
        if (address == null || address.trim() == "")
        {
            logger.error("Employ tel should not be empty");
            return null;
        }
        store.setAddress(address);
        
        String tel = storeJsonObj.getString(StoreService.STORE_TEL);
        if (tel == null || tel.trim() == "")
        {
            logger.error("Employ tel should not be empty");
            return null;
        }
        store.setTel(tel);
        
        String provinceId = storeJsonObj.getString(StoreService.STORE_PROVINCE_ID);
        if (provinceId == null || provinceId.trim() == "")
        {
            logger.error("Employ tel should not be empty");
            return null;
        }
        store.setProvinceId(provinceId);
        
        String cityId = storeJsonObj.getString(StoreService.STORE_CITY_ID);
        if (cityId == null || cityId.trim() == "")
        {
            logger.error("Employ tel should not be empty");
            return null;
        }
        store.setCityId(cityId);
        
        String areaId = storeJsonObj.getString(StoreService.STORE_AREA_ID);
        if (areaId == null || areaId.trim() == "")
        {
            logger.error("Employ tel should not be empty");
            return null;
        }
        store.setAreaId(areaId);
        
        String location = "";
        String provinceName = locationService.getProvinceById(provinceId);
        if (provinceName == null)
        {
            logger.error("Can not found province by id: " + provinceId);
            return null;
        }
        location += provinceName;
        String cityName = locationService.getCityById(cityId);
        if (cityName == null)
        {
            logger.error("Can not found city by id: " + cityId);
            return null;
        }
        location += cityName;
        String areaName = locationService.getAreaById(areaId);
        if (areaName == null)
        {
            logger.error("Can not found city by id: " + areaId);
            return null;
        }
        location += areaName;
        store.setLocation(location);
        return store;
    }

    @Override
    public void signStoreById(int id)
    {
        // TODO Auto-generated method stub
        Store store = this.findStoreById(id);
        if (store == null)
        {
            return;
        }
        addStoreToLoactionMap(store);
    }
    
    private void addStoreToLoactionMap(Store store)
    {
        String provinceId = store.getProvinceId();
        String cityId = store.getCityId();
        String areaId = store.getAreaId();
        if (provinceId == null || cityId == null || areaId == null)
        {
            return;
        }
        
        Set<String> cityIdSet = provinceIdToCityIdMap.get(provinceId);
        if (cityIdSet == null)
        {
            cityIdSet = new HashSet<String>();
            provinceIdToCityIdMap.put(provinceId, cityIdSet);
        }
        cityIdSet.add(cityId);    
        
        Set<String> areaIdSet = cityIdToAreaIdMap.get(cityId);
        if (areaIdSet == null)
        {
            areaIdSet = new HashSet<String>();
            cityIdToAreaIdMap.put(cityId, areaIdSet);
        }
        areaIdSet.add(areaId);
        
        Set<Integer> storeIdSet = areaIdToStoreIdMap.get(areaId);
        if (storeIdSet == null)
        {
            storeIdSet = new HashSet<Integer>();
            areaIdToStoreIdMap.put(areaId, storeIdSet);
        }
        storeIdSet.add(store.getId());
        
        storeIdToStoreNameMap.put(store.getId(), store.getName());
    }

    @Override
    public String getStoreProvince()
    {
        // TODO Auto-generated method stub
        Map<String, String> retProvinceMap = new HashMap<String, String>();
        Set<String> provinceIdList = provinceIdToCityIdMap.keySet();
        for (String provinceId : provinceIdList)
        {
            String provinceName = locationService.getProvinceById(provinceId);
            if (provinceName == null)
            {
                continue;
            }
            retProvinceMap.put(provinceId, provinceName);
        }
        return JSONObject.fromObject(retProvinceMap).toString();
    }

    @Override
    public String getStoreCityByProvinceId(String provinceId)
    {
        // TODO Auto-generated method stub
        Map<String, String> retCityMap = new HashMap<String, String>();
        Set<String> cityIdList = provinceIdToCityIdMap.get(provinceId);
        if (cityIdList == null)
        {
            return JSONObject.fromObject(retCityMap).toString();
        }
        for(String cityId : cityIdList)
        {
            String cityName = locationService.getCityById(cityId);
            if (cityName == null)
            {
                continue;
            }
            retCityMap.put(cityId, cityName);
        }
        return JSONObject.fromObject(retCityMap).toString();
    }

    @Override
    public String getStoreAreaByCityId(String cityId)
    {
        // TODO Auto-generated method stub
        Map<String, String> retAreaMap = new HashMap<String, String>();
        Set<String> areaIdList = cityIdToAreaIdMap.get(cityId);
        if (areaIdList == null)
        {
            return JSONObject.fromObject(retAreaMap).toString();
        }
        for(String areaId : areaIdList)
        {
            String areaName = locationService.getAreaById(areaId);
            if (areaName == null)
            {
                continue;
            }
            retAreaMap.put(areaId, areaName);
        }
        return JSONObject.fromObject(retAreaMap).toString();
    }

    @Override
    public String getStoreByAreaId(String areaId)
    {
        // TODO Auto-generated method stub
        Map<String, String> retStoreMap = new HashMap<String, String>();
        Set<Integer> storeIdList = areaIdToStoreIdMap.get(areaId);
        if (storeIdList == null)
        {
            return JSONObject.fromObject(retStoreMap).toString();
        }
        for(Integer storeId : storeIdList)
        {
            String storeName = storeIdToStoreNameMap.get(storeId);
            if (storeName == null)
            {
                continue;
            }
            retStoreMap.put(String.valueOf(storeId), storeName);
        }
        return JSONObject.fromObject(retStoreMap).toString();
    }

    @Override
    public void loadStoreToLoactionMap(Set<Integer> storeIdSet)
    {
        // TODO Auto-generated method stub
        if (storeIdSet == null)
        {
            return;
        }
        List<Store> storeList = this.findAllStores();
        if (storeList == null)
        {
            return;
        }
        for (Store store : storeList)
        {
            if (!storeIdSet.contains(store.getId()))
            {
                continue;
            }
            addStoreToLoactionMap(store);
        }
    }
    
}
