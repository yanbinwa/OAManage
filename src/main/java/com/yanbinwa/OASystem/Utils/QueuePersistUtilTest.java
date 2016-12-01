package com.yanbinwa.OASystem.Utils;

import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.BlockingQueue;

import com.yanbinwa.OASystem.Message.Message;

public class QueuePersistUtilTest
{
    
    public static Message getMessage()
    {
        Message message = new Message();
        message.setRouteKey("wyb");
        message.setFunctionKey("wyb");
        message.setResponsePayLoad("wyb");
        message.setResponseCode(1);
        return message;
    }
    
    public static void main(String[] args)
    {
        BlockingQueue<Message> queue = new ArrayBlockingQueue<Message>(10);
        String filename = "/Users/yanbinwa/Documents/workspace/OAManage/src/main/resources/TestQueuePersist.txt";
                
        for(int i = 0; i < 10; i ++)
        {
            queue.add(getMessage());
        }
        
        QueuePersistUtil.persistQueue(queue, Message.class, filename);
        
        queue.clear();
        
        QueuePersistUtil.loadQueue(queue, Message.class, filename);
        
        System.out.println(queue);
        
    }
}
