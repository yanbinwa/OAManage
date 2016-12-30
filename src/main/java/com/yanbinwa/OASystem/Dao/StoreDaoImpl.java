package com.yanbinwa.OASystem.Dao;

import java.util.List;

import org.hibernate.Criteria;
import org.springframework.stereotype.Repository;

import com.yanbinwa.OASystem.Model.Store;

@Repository("storeDao")
public class StoreDaoImpl extends AbstractDao<Integer, Store> implements StoreDao
{

    @Override
    public Store findStoreById(int id)
    {
        // TODO Auto-generated method stub
        return getByKey(id);
    }

    @Override
    public void saveStore(Store store)
    {
        // TODO Auto-generated method stub
        persist(store);
    }

    @SuppressWarnings("unchecked")
    @Override
    public List<Store> findAllStores()
    {
        // TODO Auto-generated method stub
        Criteria criteria = createEntityCriteria();
        return (List<Store>) criteria.list();
    }

    @Override
    public void deleteStore(Store store)
    {
        // TODO Auto-generated method stub
        delete(store);
    }

}
