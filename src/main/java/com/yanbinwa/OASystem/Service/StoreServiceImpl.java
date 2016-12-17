package com.yanbinwa.OASystem.Service;

import java.util.List;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.yanbinwa.OASystem.Dao.StoreDao;
import com.yanbinwa.OASystem.Dao.StoreDynamicInfoDao;
import com.yanbinwa.OASystem.Model.Store;
import com.yanbinwa.OASystem.Model.StoreDynamicInfo;

import net.sf.json.JSONObject;

@Service("storeService")
@Transactional
public class StoreServiceImpl implements StoreService
{
    private static final Logger logger = Logger.getLogger(EmployeeServiceImpl.class);
    
    @Autowired
    private StoreDao storeDao;
    
    @Autowired
    private StoreDynamicInfoDao storeDynamicInfoDao;

    @Override
    public Store findById(int id)
    {
        // TODO Auto-generated method stub
        return storeDao.findById(id);
    }

    @Override
    public void saveStore(Store store)
    {
        // TODO Auto-generated method stub
        storeDao.saveStore(store);
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
        storeDao.deleteStore(store);
    }

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
        
        return store;
    }

    @Override
    public void saveStoreDynamicInfo(StoreDynamicInfo storeDynamicInfo)
    {
        // TODO Auto-generated method stub
        storeDynamicInfoDao.saveStoreDynamicInfo(storeDynamicInfo);
    }
    
}
