package com.yanbinwa.OASystem.QueuePersist;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.List;
import java.util.Queue;

import com.yanbinwa.OASystem.Common.JsonPersist;
import com.yanbinwa.OASystem.QueuePersist.AppendAction.QueueAction;
import com.yanbinwa.OASystem.Utils.QueuePersistUtil;

public class QueuePersist
{
    public static final String SNAPSHOT_FILE_SUFFIX = "snapshot";
    public static final String APPEND_ACTION_FILE_SUFFIX = "append";
    
    PersistBlockQueue<JsonPersist> queue;
    String queueFileName;
    String snapShotFileName;
    String appendActionFileName;
    
    @SuppressWarnings("rawtypes")
    Class clazz;
    
    @SuppressWarnings("rawtypes")
    public QueuePersist(PersistBlockQueue<JsonPersist> queue, String queueFileName, Class clazz)
    {
        this.queue = queue;
        this.queueFileName = queueFileName;
        this.snapShotFileName = queueFileName + "_" + SNAPSHOT_FILE_SUFFIX;
        this.appendActionFileName = queueFileName + "_" + APPEND_ACTION_FILE_SUFFIX;
        this.clazz = clazz;
        init();
    }
    
    private void init()
    {
        if (queueFileName == null)
        {
            return;
        }
        try
        {
            File snapShotFile = new File(snapShotFileName);
            if (!snapShotFile.exists())
            {
                snapShotFile.createNewFile();
                PrintWriter writer = new PrintWriter(snapShotFileName);
                writer.print("[]");
                writer.close();
            }
            File appendActionFile = new File(appendActionFileName);
            if (!appendActionFile.exists())
            {
                appendActionFile.createNewFile();
            }
        } 
        catch (IOException e)
        {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }
    
    private void emptyAppendActionFile()
    {
        try
        {
            PrintWriter writer;
            writer = new PrintWriter(appendActionFileName);
            writer.print("");
            writer.close();
        } 
        catch (FileNotFoundException e)
        {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }
    
    public void updateSnapshot()
    {
        Queue<JsonPersist> snapShotQueue = queue.getPersistQueue();
        QueuePersistUtil.persistQueueSnapshot(snapShotQueue, clazz, snapShotFileName);
        emptyAppendActionFile();
    }
    
    public void loadSnapshot()
    {
        QueuePersistUtil.loadQueueSnapshot(queue, clazz, snapShotFileName);
        loadAppendAction();
        updateSnapshot();
    }
    
    private void excuteAppendAction(AppendAction<JsonPersist> appendAction)
    {
        switch(appendAction.getAppendAction())
        {
        case POLL:
            queue.remove(appendAction.getPayLoad());
            break;
        case PUSH:
            queue.offer(appendAction.getPayLoad());
            break;
        default:
            break;
        }
    }
    
    private void loadAppendAction()
    {
        List<AppendAction<JsonPersist>> appendActionList = QueuePersistUtil.loadAppendAction(clazz, appendActionFileName);
        if (appendActionList == null)
        {
            return;
        }
        for (AppendAction<JsonPersist> appendAction : appendActionList)
        {
            excuteAppendAction(appendAction);
        }
    }
    
    public void persistAppendAction(JsonPersist obj, QueueAction action)
    {
        AppendAction<JsonPersist> appendAction = new AppendAction<JsonPersist>();
        appendAction.setPayLoad(obj);
        appendAction.setClazz(clazz);
        appendAction.setAppendAction(action);
        QueuePersistUtil.persistAppendAction(appendAction, appendActionFileName);
    }
}
