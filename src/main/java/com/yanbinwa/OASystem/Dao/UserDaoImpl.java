package com.yanbinwa.OASystem.Dao;

import java.util.List;

import org.hibernate.Criteria;
import org.hibernate.criterion.Restrictions;
import org.springframework.stereotype.Repository;

import com.yanbinwa.OASystem.Model.User;
import com.yanbinwa.OASystem.Model.User.UserState;
import com.yanbinwa.OASystem.Model.User.UserType;

@Repository("userDao")
public class UserDaoImpl extends AbstractDao<Integer, User> implements UserDao
{

    @Override
    public User findUserByName(String name)
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

    @SuppressWarnings("unchecked")
    @Override
    public List<User> findNoneAuthorizationUser()
    {
        // TODO Auto-generated method stub
        Criteria criteria = createEntityCriteria();
        criteria.add(Restrictions.eq("userState", UserState.NoneAuthorization));
        return (List<User>)criteria.list();
    }

    @Override
    public User findUserById(int id)
    {
        // TODO Auto-generated method stub
        return getByKey(id);
    }

    @SuppressWarnings("unchecked")
    @Override
    public List<User> findStoreUser()
    {
        // TODO Auto-generated method stub
        Criteria criteria = createEntityCriteria();
        criteria.add(Restrictions.eq("userType", UserType.Store));
        return (List<User>)criteria.list();
    }
  
}
