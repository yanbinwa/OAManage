package com.yanbinwa.OASystem.Model;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name="USERDYNAMICINFO")
public class UserDynamicInfo
{
    public enum LoginStatus
    {
        unLogin, login
    }
    
    @Id
    private int id;
    
    @Column(name = "LOGINTIME", nullable = false)
    private long loginTime = -1;
    
    @Column(name = "LOGOUTTIME", nullable = false)
    private long logoutTime = -1;
    
    @Column(name = "LOGINSTATUS", nullable = false)
    private LoginStatus loginStatus;
    
    public UserDynamicInfo()
    {
        
    }
    
    public void setId(int id)
    {
        this.id = id;
    }
    
    public int getId()
    {
        return this.id;
    }
    
    public void setLoginTime(long loginTime)
    {
        this.loginTime = loginTime;
    }
    
    public long getLoginTime()
    {
        return loginTime;
    }
    
    public void setLogoutTime(long logoutTime)
    {
        this.logoutTime = logoutTime;
    }
    
    public long getLogoutTime()
    {
        return logoutTime;
    }
    
    public void setLoginStatus(LoginStatus loginStatus)
    {
        this.loginStatus = loginStatus;
    }
    
    @Enumerated(EnumType.ORDINAL)
    public LoginStatus getLoginStatus()
    {
        return loginStatus;
    }
    
    @Override
    public String toString()
    {
        StringBuffer buf = new StringBuffer();
        buf.append("id = " + id).append("; ")
            .append("loginTime = " + loginTime).append("; ")
            .append("loginStatus = " + loginStatus);
        return buf.toString();
    }
}
