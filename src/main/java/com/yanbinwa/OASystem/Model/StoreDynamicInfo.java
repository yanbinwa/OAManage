package com.yanbinwa.OASystem.Model;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name="STOREDYNAMICINFO")
public class StoreDynamicInfo
{
    @Id
    private int id;
    
    public void setId(int id)
    {
        this.id = id;
    }
    
    public int getId()
    {
        return this.id;
    }
    
    public StoreDynamicInfo()
    {
        
    }
}
