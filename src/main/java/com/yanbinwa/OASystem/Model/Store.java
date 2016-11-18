package com.yanbinwa.OASystem.Model;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name="STORE")
public class Store
{
    @Id
    private int id;

    @Column(name = "NAME", nullable = false)
    private String name;
    
    @Column(name = "ADDRESS", nullable = false)
    private String address;
    
    @Column(name = "TEL", nullable = false)
    private String tel;
    
    public Store(int id, String name, String address, String tel)
    {
        this.id = id;
        this.name = name;
        this.address = address;
        this.tel = tel;
    }
    
    public Store()
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

    public String getName() 
    {
        return name;
    }

    public void setName(String name) 
    {
        this.name = name;
    }
    
    public String getAddress()
    {
        return address;
    }
    
    public void setAddress(String address)
    {
        this.address = address;
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
        if (!(obj instanceof Store))
            return false;
        Store other = (Store) obj;
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
        buf.append("Store [")
            .append("id=").append(id).append(", ")
            .append("address=").append(address).append(", ")
            .append("name=").append(name).append(", ")
            .append("tel=").append(tel)
            .append("]");
        
        return buf.toString();
    }
}
