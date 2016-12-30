package com.yanbinwa.OASystem.Model;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name="EMPLOYEE")
public class Employee
{
    @Id
    private int id;

    @Column(name = "USERID", nullable = false)
    private int userId;
    
    @Column(name = "NAME", nullable = false)
    private String name;
    
    @Column(name = "SEX", nullable = false)
    private String sex;
    
    @Column(name = "BIRTHDAY", nullable = false)
    private long birthday;
    
    @Column(name = "AGE", nullable = false)
    private int age;
    
    @Column(name = "TEL", nullable = false)
    private String tel;
    
    @Column(name = "IDENTITYID", nullable = false)
    private String identityId;
    
    @Column(name = "STOREID", nullable = false)
    private int storeId;
    
    @Column(name = "EMPLOYEEDYNAMICINFOID", nullable = false)
    private int employeeDynamicInfoId = -1;
    
    public Employee(String name, String sex, long birthday, int age, String tel, String identityId)
    {
        this.id = -1;
        this.name = name;
        this.sex = sex;
        this.birthday = birthday;
        this.age = age;
        this.tel = tel;
        this.identityId = identityId;
        this.storeId = -1;
        this.employeeDynamicInfoId = -1;
    }
    
    public Employee(int id, String name, String sex, long birthday, int age, String tel, String identityId, int storeId)
    {
        this.id = id;
        this.name = name;
        this.sex = sex;
        this.birthday = birthday;
        this.age = age;
        this.tel = tel;
        this.identityId = identityId;
        this.storeId = storeId;
        this.employeeDynamicInfoId = -1;
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
    
    public int getUserId() 
    {
        return userId;
    }

    public void setUserId(int userId) 
    {
        this.userId = userId;
    }
    
    public String getName() 
    {
        return name;
    }

    public void setName(String name) 
    {
        this.name = name;
    }
    
    public String getSex() 
    {
        return sex;
    }

    public void setSex(String sex) 
    {
        this.sex = sex;
    }
    
    public long getBirthday() 
    {
        return birthday;
    }

    public void setBirthday(long birthday) 
    {
        this.birthday = birthday;
    }
    
    public int getAge()
    {
        return age;
    }
    
    public void setAge(int age)
    {
        this.age = age;
    }
    
    public String getTel() 
    {
        return tel;
    }

    public void setTel(String tel) 
    {
        this.tel = tel;
    }
    
    public String getIdentityId() 
    {
        return identityId;
    }

    public void setIdentityId(String identityId) 
    {
        this.identityId = identityId;
    }
    
    public int getStoreId()
    {
        return storeId;
    }
    
    public void setStoreId(int storeId)
    {
        this.storeId = storeId;
    }
    
    public int getEmployeeDynamicInfoId()
    {
        return employeeDynamicInfoId;
    }
    
    public void setEmployeeDynamicInfoId(int employeeDynamicInfoId)
    {
        this.employeeDynamicInfoId = employeeDynamicInfoId;
    }
    
    

    @Override
    public int hashCode() 
    {
        final int prime = 31;
        int result = 1;
        result = prime * result + id;
        result = prime * result + ((tel == null) ? 0 : tel.hashCode());
        result = prime * result + ((identityId == null) ? 0 : identityId.hashCode());
        return result;
    }

    @Override
    public boolean equals(Object obj) 
    {
        if (this == obj)
        {
            return true;
        }
        if (obj == null)
        {
            return false;
        }
        if (!(obj instanceof Employee))
        {
            return false;
        }
        
        Employee other = (Employee) obj;
        
        if (id != other.id)
        {
            return false;
        }
        if (tel == null) 
        {
            if (other.tel != null)
            {
                return false;
            }
        } 
        else if (!tel.equals(other.tel))
        {
            return false;
        }
        
        if (identityId == null) 
        {
            if (other.identityId != null)
            {
                return false;
            }
        } 
        else if (!identityId.equals(other.identityId))
        {
            return false;
        }
        
        return true;
    }

    @Override
    public String toString() 
    {
        StringBuffer buf = new StringBuffer();
        buf.append("Employee [")
            .append("id=").append(id).append(", ")
            .append("name=").append(name).append(", ")
            .append("sex=").append(sex).append(", ")
            .append("birthday=").append(birthday).append(", ")
            .append("age=").append(age).append(", ")
            .append("tel=").append(tel).append(", ")
            .append("identityId=").append(identityId).append(", ")
            .append("storeId=").append(storeId)
            .append("]");
        
        return buf.toString();
    }
    
}
