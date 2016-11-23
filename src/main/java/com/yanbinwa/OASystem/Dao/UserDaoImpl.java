package com.yanbinwa.OASystem.Dao;

import org.hibernate.Criteria;
import org.hibernate.criterion.Restrictions;
import org.springframework.stereotype.Repository;

import com.yanbinwa.OASystem.Model.User;

@Repository("userDao")
public class UserDaoImpl extends AbstractDao<Integer, User> implements UserDao
{

    @Override
    public User findByName(String name)
    {
        // TODO Auto-generated method stub
        Criteria criteria = createEntityCriteria();
        criteria.add(Restrictions.eq("name", name));
        return (User)criteria.uniqueResult();
    }

    @Override
    public void saveUser(User user)
    {
        // TODO Auto-generated method stub
        persist(user);
    }
    
}
