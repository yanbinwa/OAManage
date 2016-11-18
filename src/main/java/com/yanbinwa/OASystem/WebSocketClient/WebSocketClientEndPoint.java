package com.yanbinwa.OASystem.WebSocketClient;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.URI;
import java.security.KeyStore;

import javax.net.ssl.KeyManagerFactory;
import javax.net.ssl.SSLContext;
import javax.net.ssl.TrustManagerFactory;
import javax.websocket.ClientEndpoint;
import javax.websocket.ClientEndpointConfig;
import javax.websocket.CloseReason;
import javax.websocket.Endpoint;
import javax.websocket.EndpointConfig;
import javax.websocket.OnClose;
import javax.websocket.OnOpen;
import javax.websocket.Session;
import javax.websocket.WebSocketContainer;

@ClientEndpoint
public class WebSocketClientEndPoint extends Endpoint
{
    
    public static final String STORE_TYPE = "JKS";
    public static final String JKS_PASSWORD = "password";
    public static final String KEY_PASSWORD = "password";
    public static final String KEYSTORE_PATH = "/Users/yanbinwa/.keystore";
    
    Session userSession = null;
    private MessageHandler messageHandler;
    
    public WebSocketClientEndPoint(URI endpointURI) {
        
        try 
        {
            KeyStore keyStore = KeyStore.getInstance(STORE_TYPE);
            InputStream is = new FileInputStream(KEYSTORE_PATH);
            keyStore.load(is, JKS_PASSWORD.toCharArray());
            final KeyManagerFactory kmf = KeyManagerFactory.getInstance(KeyManagerFactory.getDefaultAlgorithm());
            kmf.init(keyStore, KEY_PASSWORD.toCharArray());
            final TrustManagerFactory tmf = TrustManagerFactory.getInstance(TrustManagerFactory.getDefaultAlgorithm());
            tmf.init(keyStore);

            final SSLContext sc = SSLContext.getInstance("TLS");
            sc.init(kmf.getKeyManagers(), tmf.getTrustManagers(), new java.security.SecureRandom());
            
            ClientEndpointConfig clientEndpointConfig = ClientEndpointConfig.Builder.create().build();            
            WebSocketContainer container = new TomcatWebsocketContainer(sc);
            container.connectToServer(this, clientEndpointConfig, endpointURI);
        } 
        catch (Exception e) 
        {
            throw new RuntimeException(e);
        }
    }
    
    @OnOpen
    public void onOpen(Session session, EndpointConfig config)
    {
        System.out.println("opening websocket");
        this.userSession = session;
        userSession.addMessageHandler(new javax.websocket.MessageHandler.Whole<String>() {

            @Override
            public void onMessage(String message)
            {
                // TODO Auto-generated method stub
                if (messageHandler != null) 
                {
                    messageHandler.handleMessage(message);
                }
            }
            
        });
    }

    @OnClose
    public void onClose(Session userSession, CloseReason reason) 
    {
        System.out.println("closing websocket");
        this.userSession = null;
    }
    
    public void addMessageHandler(MessageHandler msgHandler) 
    {
        this.messageHandler = msgHandler;
    }

    public void sendMessage(String message) 
    {
        this.userSession.getAsyncRemote().sendText(message);
    }
    
    public void closeSession()
    {
        try
        {
            this.userSession.close();
        } 
        catch (IOException e)
        {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }
    
    public static interface MessageHandler
    {
        public void handleMessage(String message);
    }
    
}
