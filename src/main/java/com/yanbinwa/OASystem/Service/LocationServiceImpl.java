package com.yanbinwa.OASystem.Service;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import javax.annotation.PostConstruct;

import org.apache.log4j.Logger;
import org.springframework.stereotype.Service;

import com.yanbinwa.OASystem.Utils.FileUtils;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;


@Service("locationService")
public class LocationServiceImpl implements LocationService
{
    private Map<String, Set<String>> provinceToCityIdMap = new HashMap<String, Set<String>>();
    private Map<String, Set<String>> cityToAreaIdMap = new HashMap<String, Set<String>>();
    
    private Map<String, String> provinceInfoMap = new HashMap<String, String>();
    private Map<String, String> cityInfoMap = new HashMap<String, String>();
    private Map<String, String> areaInfoMap = new HashMap<String, String>();
    
    private static final Logger logger = Logger.getLogger(LocationServiceImpl.class);
    
    @PostConstruct
    public void init()
    {
        loadLocationInfoMap();
    }
    
    private void loadLocationInfoMap()
    {
        String provinceInfoStr = FileUtils.readJsonFile(PROVINCE_INFO_FILE);
        if (provinceInfoStr == null || provinceInfoStr.equals(""))
        {
            logger.error("Read province file fail");
            return;
        }
        JSONArray provinceArray = JSONArray.fromObject(provinceInfoStr);
        
        String cityInfoStr = FileUtils.readJsonFile(CITY_INFO_FILE);
        if (cityInfoStr == null || cityInfoStr.equals(""))
        {
            logger.error("Read city file fail");
            return;
        }
        JSONObject cityMap = JSONObject.fromObject(cityInfoStr);
        
        String areaInfoStr = FileUtils.readJsonFile(AREA_INFO_FILE);
        if (areaInfoStr == null || areaInfoStr.equals(""))
        {
            logger.error("Read area file fail");
            return;
        }
        JSONObject areaMap = JSONObject.fromObject(areaInfoStr);
        
        for(int i = 0; i < provinceArray.size(); i ++)
        {
            JSONObject provinceObject = provinceArray.getJSONObject(i);
            String provinceId = provinceObject.getString("id");
            String provinceName = provinceObject.getString("name");
            Set<String> cityIdList = provinceToCityIdMap.get(provinceId);
            if (cityIdList == null)
            {
                cityIdList = new HashSet<String>();
                provinceToCityIdMap.put(provinceId, cityIdList);
            }
            provinceInfoMap.put(provinceId, provinceName);
            
            if (!cityMap.containsKey(provinceId))
            {
                continue;
            }
            JSONArray cityArray = cityMap.getJSONArray(provinceId);
            for(int j = 0; j < cityArray.size(); j ++)
            {
                JSONObject cityObject = cityArray.getJSONObject(j);
                String cityId = cityObject.getString("id");
                String cityName = cityObject.getString("name");
                if (cityIdList.contains(cityId))
                {
                    logger.error("Duplicate city id");
                    return;
                }
                cityIdList.add(cityId);
                
                Set<String> areaIdList = cityToAreaIdMap.get(cityId);
                if (areaIdList == null)
                {
                    areaIdList = new HashSet<String>();
                    cityToAreaIdMap.put(cityId, areaIdList);
                }
                cityInfoMap.put(cityId, cityName);
                
                if (!areaMap.containsKey(cityId))
                {
                    continue;
                }
                JSONArray areaArray = areaMap.getJSONArray(cityId);
                for(int k = 0; k < areaArray.size(); k ++)
                {
                    JSONObject areaObject = areaArray.getJSONObject(k);
                    String areaId = areaObject.getString("id");
                    String areaName = areaObject.getString("name");
                    if(areaIdList.contains(areaId))
                    {
                        logger.error("Duplicate area id");
                        return;
                    }
                    areaIdList.add(areaId);
                    areaInfoMap.put(areaId, areaName);
                }
            }
        }
    }

    @Override
    public String getProvinceList()
    {
        // TODO Auto-generated method stub
        JSONObject retJsonObject = JSONObject.fromObject(provinceInfoMap);
        return retJsonObject.toString();
    }

    @Override
    public String getCityList(String id)
    {
        // TODO Auto-generated method stub
        if (id == null)
        {
            return "";
        }
        Map<String, String> retCityMap = new HashMap<String, String>();
        Set<String> cityIdList = provinceToCityIdMap.get(id);
        if (cityIdList == null)
        {
            return "";
        }
        for (String cityId : cityIdList)
        {
            String cityName = cityInfoMap.get(cityId);
            if (cityName == null)
            {
                return "";
            }
            retCityMap.put(cityId, cityName);
        }
        JSONObject retJsonObject = JSONObject.fromObject(retCityMap);
        return retJsonObject.toString();
    }

    @Override
    public String getAreaList(String id)
    {
        // TODO Auto-generated method stub
        if (id == null)
        {
            return "";
        }
        Map<String, String> retAreaMap = new HashMap<String, String>();
        Set<String> areaIdList = cityToAreaIdMap.get(id);
        if (areaIdList == null)
        {
            return "";
        }
        for (String areaId : areaIdList)
        {
            String areaName = areaInfoMap.get(areaId);
            if (areaName == null)
            {
                return "";
            }
            retAreaMap.put(areaId, areaName);
        }
        JSONObject retJsonObject = JSONObject.fromObject(retAreaMap);
        return retJsonObject.toString();
    }

    @Override
    public String getProvinceById(String id)
    {
        // TODO Auto-generated method stub
        return provinceInfoMap.get(id);
    }

    @Override
    public String getCityById(String id)
    {
        // TODO Auto-generated method stub
        return cityInfoMap.get(id);
    }

    @Override
    public String getAreaById(String id)
    {
        // TODO Auto-generated method stub
        return areaInfoMap.get(id);
    }
}
