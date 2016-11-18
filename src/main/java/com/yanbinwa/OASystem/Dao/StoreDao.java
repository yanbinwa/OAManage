package com.yanbinwa.OASystem.Dao;

import java.util.List;

import com.yanbinwa.OASystem.Model.Store;

public interface StoreDao
{
    Store findById(int id);

    void saveStore(Store store);
    
    List<Store> findAllStores();
    
    void deleteStore(Store store);
}
