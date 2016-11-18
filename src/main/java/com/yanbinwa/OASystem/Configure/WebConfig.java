package com.yanbinwa.OASystem.Configure;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurerAdapter;
import org.springframework.web.socket.WebSocketHandler;
import org.springframework.web.socket.config.annotation.EnableWebSocket;
import org.springframework.web.socket.config.annotation.WebSocketConfigurer;
import org.springframework.web.socket.config.annotation.WebSocketHandlerRegistry;

import com.yanbinwa.OASystem.WebSocket.WebSocketEndPointSpring;

@Configuration  
@EnableWebMvc  
@EnableWebSocket
public class WebConfig extends WebMvcConfigurerAdapter implements WebSocketConfigurer
{
    
    @Override
    public void registerWebSocketHandlers(WebSocketHandlerRegistry registry)
    {
        // TODO Auto-generated method stub
        registry.addHandler(webSocketEndPointSpring(), "/websocketSpring");
    }
    
    @Bean
    public WebSocketHandler webSocketEndPointSpring() 
    {  
        return new WebSocketEndPointSpring();  
    }
   
}


