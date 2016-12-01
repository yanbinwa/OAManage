package com.yanbinwa.OASystem.Service;

import com.yanbinwa.OASystem.Message.Message;

public interface WebsocketMessageProcessorService
{
    public static final String QUEUE_CAPACITY = "WebsocketMessageProcessor_QueueCapacity";
    public static final String CORE_POOL_SIZE = "WebsocketMessageProcessor_CorePoolSize";
    public static final String MAX_POOL_SIZE = "WebsocketMessageProcessor_MaxPoolSize";
    public static final String KEEP_ALIVE_SECONDS = "WebsocketMessageProcessor_KeepAliveSeconds";
    
    public static final String MESSAGE_QUEUE_SIZE = "WebsocketMessageProcessor_MessageQueueSize";
    
    void enqueueMessage(Message message);
}
