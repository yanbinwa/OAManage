package com.yanbinwa.OASystem.Model;

import java.io.Serializable;

public class Event implements Serializable
{
    private static final long serialVersionUID = 1L;
    
    String name;
    int age;
    
    public Event(String name, int age)
    {
        this.name = name;
        this.age = age;
    }
    
    public Event()
    {
        
    }
    
    public String getName()
    {
        return this.name;
    }
    
    public void setName(String name)
    {
        this.name = name;
    }
    
    public int getAge()
    {
        return this.age;
    }
    
    public void setAge(int age)
    {
        this.age = age;
    }
    
    @Override
    public String toString()
    {
        StringBuffer sb = new StringBuffer();
        sb.append("name = " + name).append("; ")
            .append("age = " + age);
        return sb.toString();
    }
}
