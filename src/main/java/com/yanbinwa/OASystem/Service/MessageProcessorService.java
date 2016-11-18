package com.yanbinwa.OASystem.Service;

import com.yanbinwa.OASystem.Message.Message;

public interface MessageProcessorService
{
    
    public static final String QUEUE_CAPACITY = "QueueCapacity";
    public static final String CORE_POOL_SIZE = "CorePoolSize";
    public static final String MAX_POOL_SIZE = "MaxPoolSize";
    public static final String KEEP_ALIVE_SECONDS = "KeepAliveSeconds";
    
    public static final String MESSAGE_PROCESSOR_QUEUE_SIZE = "MessageProcessorQueueSize";
    
    public boolean enqueue(Message message);
}
