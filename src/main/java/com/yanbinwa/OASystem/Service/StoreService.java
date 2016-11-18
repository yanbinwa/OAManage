package com.yanbinwa.OASystem.Service;

import java.util.List;

import com.yanbinwa.OASystem.Model.Store;

public interface StoreService
{
    Store findById(int id);
    
    void saveStore(Store store);

    List<Store> findAllStores();
    
    void deleteStore(Store store);
}
