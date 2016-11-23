package com.yanbinwa.OASystem.Model;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name="USER")
public class User
{
    public enum UserType 
    {
        Employee, Store
    }
    
    public enum AuthType
    {
        Normal, Admin
    }
    
    public enum UserState
    {
        Authorization, NoneAuthorization
    }
    
    @Id
    private int id;
    
    @Column(name = "NAME", unique = true, nullable = false)
    private String name;
    
    @Column(name = "PASSWORD", nullable = false)
    private String password;
    
    @Column(name = "USERTYPE", nullable = false)
    private UserType userType;
    
    @Column(name = "AUTHTYPE", nullable = false)
    private AuthType authType;
    
    @Column(name = "USERSTATE", nullable = false)
    private UserState userState;
    
    @Column(name = "USERID", nullable = false)
    private int userId = -1;
    
    public User()
    {
        
    }
    
    public User(int id, String name, String password, UserType userType, AuthType authType, UserState userState, int userId)
    {
        this.id = id;
        this.name = name;
        this.password = password;
        this.userType = userType;
        this.authType = authType;
        this.userState = userState;
        this.userId = userId;
    }
    
    public void setId(int id)
    {
        this.id = id;
    }
    
    public int getId()
    {
        return this.id;
    }
    
    public void setName(String name)
    {
        this.name = name;
    }
    
    public String getName()
    {
        return this.name;
    }
    
    public void setPassword(String password)
    {
        this.password = password;
    }
    
    public String getPassword()
    {
        return this.password;
    }
    
    public void setUserType(UserType userType)
    {
        this.userType = userType;
    }
    
    @Enumerated(EnumType.ORDINAL)
    public UserType getUserType()
    {
        return this.userType;
    }
    
    public void setAuthType(AuthType authType)
    {
        this.authType = authType;
    }
    
    @Enumerated(EnumType.ORDINAL)
    public AuthType getAuthType()
    {
        return this.authType;
    }
    
    public void setUserState(UserState userState)
    {
        this.userState = userState;
    }
    
    @Enumerated(EnumType.ORDINAL)
    public UserState getUserState()
    {
        return this.userState;
    }
    
    public void setUserId(int userId)
    {
        this.userId = userId;
    }
    
    public int getUserId()
    {
        return this.userId;
    }
    
    @Override
    public String toString()
    {
        StringBuffer buf = new StringBuffer();
        buf.append("id = " + id).append("; ")
            .append("name = " + name).append("; ");
        return buf.toString();
    }
}
