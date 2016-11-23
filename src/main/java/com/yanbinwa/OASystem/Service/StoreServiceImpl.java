package com.yanbinwa.OASystem.Service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.yanbinwa.OASystem.Dao.StoreDao;
import com.yanbinwa.OASystem.Model.Store;

@Service("storeService")
@Transactional
public class StoreServiceImpl implements StoreService
{
    @Autowired
    private StoreDao dao;

    @Override
    public Store findById(int id)
    {
        // TODO Auto-generated method stub
        return dao.findById(id);
    }

    @Override
    public void saveStore(Store store)
    {
        // TODO Auto-generated method stub
        dao.saveStore(store);
    }

    @Override
    public List<Store> findAllStores()
    {
        // TODO Auto-generated method stub
        return dao.findAllStores();
    }

    @Override
    public void deleteStore(Store store)
    {
        // TODO Auto-generated method stub
        dao.deleteStore(store);
    }

    @Override
    public Store vailadeAndGetStoreFromPayLoad(Object storeObj)
    {
        // TODO Auto-generated method stub
        return null;
    }
    
}
