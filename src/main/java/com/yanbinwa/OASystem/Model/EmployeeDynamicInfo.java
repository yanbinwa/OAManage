package com.yanbinwa.OASystem.Model;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name="EMPLOYEEDYNAMICINFO")
public class EmployeeDynamicInfo
{
    public enum CheckinStatus
    {
        unCheckin, checkin
    }
    
    @Id
    private int id;
    
    @Column(name = "CHECKINTIME", nullable = false)
    private long checkinTime = -1;
    
    @Column(name = "CHECKOUTTIME", nullable = false)
    private long checkoutTime = -1;
    
    @Column(name = "CHECKINSTATUS", nullable = false)
    private CheckinStatus checkinStatus;
    
    @Column(name = "CHECKINSTOREID", nullable = false)
    private int checkinStoreId = -1;
    
    public EmployeeDynamicInfo()
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
    
    public void setCheckinTime(long checkinTime)
    {
        this.checkinTime = checkinTime;
    }
    
    public long getCheckinTime()
    {
        return checkinTime;
    }
    
    public void setCheckoutTime(long checkoutTime)
    {
        this.checkoutTime = checkoutTime;
    }
    
    public long getCheckoutTime()
    {
        return checkoutTime;
    }
    
    public void setCheckinStatus(CheckinStatus checkinStatus)
    {
        this.checkinStatus = checkinStatus;
    }
    
    @Enumerated(EnumType.ORDINAL)
    public CheckinStatus getCheckinStatus()
    {
        return checkinStatus;
    }
    
    public void setCheckinStoreId(int checkinStoreId)
    {
        this.checkinStoreId = checkinStoreId;
    }
    
    public int getCheckinStoreId()
    {
        return checkinStoreId;
    }
    
    @Override
    public String toString()
    {
        StringBuffer buf = new StringBuffer();
        buf.append("id = " + id).append("; ")
            .append("checkinTime = " + checkinTime).append("; ")
            .append("checkinStatus = " + checkinStatus);
        return buf.toString();
    }
    
}
