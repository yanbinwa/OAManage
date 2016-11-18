package com.yanbinwa.OASystem.Model;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

import org.hibernate.validator.constraints.NotEmpty;


@Entity
@Table(name="EMPLOYEE")
public class Employee
{
    @Id
    private int id;

    @Column(name = "NAME", nullable = false)
    private String name;
    
    @Column(name = "STOREID", nullable = false)
    private int storeId;
    
    @Column(name = "LEADERID", nullable = false)
    private int leaderId;
    
    @NotEmpty
    @Column(name = "TEL", nullable = false)
    private String tel;
    
    public Employee(int id, String name, String tel)
    {
        this.id = id;
        this.name = name;
        this.tel = tel;
        
        this.storeId = -1;
        this.leaderId = -1;
    }
    
    public Employee(int id, int storeId, int leaderId, String name, String tel)
    {
        this.id = id;
        this.storeId = storeId;
        this.leaderId = leaderId;
        this.name = name;
        this.tel = tel;
    }
    
    public Employee()
    {
        
    }
    
    public int getId() 
    {
        return id;
    }

    public void setId(int id) 
    {
        this.id = id;
    }
    
    public int getStoreId()
    {
        return storeId;
    }
    
    public void setStoreId(int storeId)
    {
        this.storeId = storeId;
    }
    
    public int getLeaderId()
    {
        return leaderId;
    }
    
    public void setLeaderId(int leaderId)
    {
        this.leaderId = leaderId;
    }

    public String getName() 
    {
        return name;
    }

    public void setName(String name) 
    {
        this.name = name;
    }

    public String getTel() 
    {
        return tel;
    }

    public void setTel(String tel) 
    {
        this.tel = tel;
    }

    @Override
    public int hashCode() 
    {
        final int prime = 31;
        int result = 1;
        result = prime * result + id;
        result = prime * result + ((tel == null) ? 0 : tel.hashCode());
        return result;
    }

    @Override
    public boolean equals(Object obj) 
    {
        if (this == obj)
            return true;
        if (obj == null)
            return false;
        if (!(obj instanceof Employee))
            return false;
        Employee other = (Employee) obj;
        if (id != other.id)
            return false;
        if (tel == null) {
            if (other.tel != null)
                return false;
        } else if (!tel.equals(other.tel))
            return false;
        return true;
    }

    @Override
    public String toString() 
    {
        StringBuffer buf = new StringBuffer();
        buf.append("Employee [")
            .append("id=").append(id).append(", ")
            .append("storeId=").append(storeId).append(", ")
            .append("leaderId=").append(leaderId).append(", ")
            .append("name=").append(name).append(", ")
            .append("tel=").append(tel)
            .append("]");
        
        return buf.toString();
    }
    
}
