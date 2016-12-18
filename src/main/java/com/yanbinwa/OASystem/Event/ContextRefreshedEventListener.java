package com.yanbinwa.OASystem.Event;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationListener;
import org.springframework.context.event.ContextRefreshedEvent;
import org.springframework.stereotype.Component;

import com.yanbinwa.OASystem.Service.UserService;

@Component("contextRefreshedEventListener")
public class ContextRefreshedEventListener implements ApplicationListener<ContextRefreshedEvent>
{
    @Autowired
    private UserService userService;
    
    @Override
    public void onApplicationEvent(ContextRefreshedEvent event)
    {
        // TODO Auto-generated method stub
       if(event.getApplicationContext().getParent() == null)
       {  
           userService.loadUserToLoactionMap();
       }  
    }

}
