package com.yanbinwa.OASystem.Service;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import javax.annotation.PostConstruct;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.yanbinwa.OASystem.Common.JsonPersist;
import com.yanbinwa.OASystem.QueuePersist.AppendAction.QueueAction;
import com.yanbinwa.OASystem.QueuePersist.PersistBlockQueue;
import com.yanbinwa.OASystem.QueuePersist.QueuePersist;

@Service("queuePersistService")
public class QueuePersistServiceImpl implements QueuePersistService
{
    private Map<String, QueuePersist> fileNameToQueuePersistMap = new ConcurrentHashMap<String, QueuePersist>();
    
    @Autowired
    private PropertyService propertyService;
    
    private int queueCreateSnapshotTime;
    
    @PostConstruct
    public void init()
    {
        queueCreateSnapshotTime = (Integer)propertyService.getProperty(QUEUE_CREATE_SHAPSHOT_TIME, Integer.class);
        
        new Thread(new Runnable() {

            @Override
            public void run()
            {
                // TODO Auto-generated method stub
                updateSnapshot();
            }
            
        }).start();
    }

    @SuppressWarnings("rawtypes")
    @Override
    public void registerQueue(PersistBlockQueue<JsonPersist> queue, Class clazz, String filename)
    {
        // TODO Auto-generated method stub
        if (queue == null || filename == null)
        {
            return;
        }
        QueuePersist queuePersist = new QueuePersist(queue, filename, clazz);
        fileNameToQueuePersistMap.put(filename, queuePersist);
    }

    @Override
    public void loadQueue(String filename)
    {
        // TODO Auto-generated method stub
        QueuePersist queuePersist = fileNameToQueuePersistMap.get(filename);
        if (queuePersist == null)
        {
            return;
        }
        queuePersist.loadSnapshot();
    }
    
    private void updateSnapshot()
    {
        while(true)
        {
            for(QueuePersist queuePersist : fileNameToQueuePersistMap.values())
            {
                queuePersist.updateSnapshot();
            }
            try
            {
                Thread.sleep(queueCreateSnapshotTime);
            } 
            catch (InterruptedException e)
            {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
        }
    }

    @Override
    public void appendAction(JsonPersist obj, QueueAction action, String filename)
    {
        // TODO Auto-generated method stub
        if (obj == null || action == null)
        {
            return;
        }
        QueuePersist queuePersist = fileNameToQueuePersistMap.get(filename);
        if (queuePersist == null)
        {
            return;
        }
        queuePersist.persistAppendAction(obj, action);
    }  
}
