package com.yanbinwa.OASystem.WebSocketClient;

import java.net.URI;

import javax.net.ssl.SSLContext;
import javax.websocket.ClientEndpointConfig;
import javax.websocket.DeploymentException;
import javax.websocket.Endpoint;
import javax.websocket.Session;

import org.apache.tomcat.websocket.WsWebSocketContainer;

public class TomcatWebsocketContainer extends WsWebSocketContainer
{
    private final SSLContext context; 
    
    public TomcatWebsocketContainer(SSLContext context) 
    { 
        super(); 
        this.context = context;
    } 

    @Override 
    public Session connectToServer(Endpoint endpoint, ClientEndpointConfig clientEndpointConfiguration, URI url) throws DeploymentException 
    { 
        if (context != null) { 
            clientEndpointConfiguration.getUserProperties().put("org.apache.tomcat.websocket.SSL_CONTEXT", context); 
        } 
        return super.connectToServer(endpoint, clientEndpointConfiguration, url);
    } 
}
