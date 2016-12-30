package com.yanbinwa.OASystem.Utils;

import com.yanbinwa.OASystem.Common.JsonPersist;
import com.yanbinwa.OASystem.Message.Message;
import com.yanbinwa.OASystem.Notification.Notification;
import com.yanbinwa.OASystem.QueuePersist.AppendAction.QueueAction;
import com.yanbinwa.OASystem.QueuePersist.PersistBlockQueue;
import com.yanbinwa.OASystem.Service.QueuePersistService;
import com.yanbinwa.OASystem.Service.QueuePersistServiceImpl;

public class QueuePersistUtilTest
{
    
    public static Notification getNotification()
    {
        Notification notification = new Notification();
        notification.setExpiredTime(1000);
        notification.setMessage(getMessage());
        return notification;
    }
    
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
//        BlockingQueue<Notification> queue = new ArrayBlockingQueue<Notification>(10);
//        String filename = "/Users/yanbinwa/Documents/workspace/OAManage/src/main/resources/queueCache/TestQueuePersist.txt";
//                
//        for(int i = 0; i < 10; i ++)
//        {
//            queue.add(getNotification());
//        }
//        
//        QueuePersistUtil.persistQueueSnapshot(queue, Notification.class, filename);
//        
//        queue.clear();
//        
//        QueuePersistUtil.loadQueueSnapshot(queue, Notification.class, filename);
//        
//        System.out.println(queue);
        String filename = "/Users/yanbinwa/Documents/workspace/OAManage/src/main/resources/queueCache/TestQueuePersist.txt";
        PersistBlockQueue<JsonPersist> queue = new PersistBlockQueue<JsonPersist>(30, Notification.class, filename);
        
        QueuePersistService service = new QueuePersistServiceImpl();
        service.registerQueue(queue, Notification.class, filename);
        for(int i = 0; i < 10; i ++)
        {
            service.appendAction(getNotification(), QueueAction.PUSH, filename);
        }
        service.loadQueue(filename);
        System.out.println(queue);
    }
}
