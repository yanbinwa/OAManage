package com.yanbinwa.OASystem.Service;

import com.yanbinwa.OASystem.Common.JsonPersist;
import com.yanbinwa.OASystem.QueuePersist.PersistBlockQueue;
import com.yanbinwa.OASystem.QueuePersist.AppendAction.QueueAction;

public interface QueuePersistService
{
    public static final String QUEUE_CREATE_SHAPSHOT_TIME = "QueuePersist_QueueCreateSnapshotTime";
    
    @SuppressWarnings("rawtypes")
    void registerQueue(PersistBlockQueue<JsonPersist> queue, Class clazz, String filename);
    
    void loadQueue(String filename);
    
    void appendAction(JsonPersist obj, QueueAction action, String filename);
}
