package com.yanbinwa.OASystem.QueuePersist;

import java.util.ArrayDeque;
import java.util.Collection;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Queue;
import java.util.Set;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.TimeUnit;

import com.yanbinwa.OASystem.Common.JsonPersist;

public class PersistBlockQueue<T extends JsonPersist> implements BlockingQueue<T>
{
    private BlockingQueue<T> queue;
    private Set<T> waitCommitSet;
    @SuppressWarnings("rawtypes")
    private Class clazz;
    private String filename;
    
    @SuppressWarnings("rawtypes")
    public PersistBlockQueue(int size, Class clazz, String filename)
    {
        this.queue = new ArrayBlockingQueue<T>(size);
        this.waitCommitSet = new HashSet<T>();
        this.clazz = clazz;
        this.filename = filename;
    }
    
    public T pollWithoutCommit(long timeout, TimeUnit unit)
    {
        T result = null;
        try
        {
            result = queue.poll(timeout, TimeUnit.MILLISECONDS);
            if (result == null)
            {
                return null;
            }
            waitCommitSet.add(result);
        } 
        catch (InterruptedException e)
        {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        return result;
    }
    
    public boolean commitPoll(JsonPersist jsonPersist)
    {
        waitCommitSet.remove(jsonPersist);
        return true;
    }
    
    public boolean cancelPoll(T jsonPersist) 
    {
        boolean ret = queue.offer(jsonPersist);
        if (!ret)
        {
            return ret;
        }
        waitCommitSet.remove(jsonPersist);
        return true;
    }
    
    public Queue<T> getPersistQueue()
    {
        Queue<T> persistQueue = new ArrayDeque<T>(queue);
        for(T jsonPersist : waitCommitSet)
        {
            persistQueue.add(jsonPersist);
        }
        return persistQueue;
    }
    
    @SuppressWarnings("rawtypes")
    public Class getClazz()
    {
        return this.clazz;
    }
    
    public String getFilename()
    {
        return filename;
    }

    @Override
    public T remove()
    {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public T poll()
    {
        // TODO Auto-generated method stub
        return queue.poll();
    }

    @Override
    public T element()
    {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public T peek()
    {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public int size()
    {
        // TODO Auto-generated method stub
        return queue.size();
    }

    @Override
    public boolean isEmpty()
    {
        // TODO Auto-generated method stub
        return false;
    }

    @Override
    public Iterator<T> iterator()
    {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public Object[] toArray()
    {
        // TODO Auto-generated method stub
        return null;
    }

    @SuppressWarnings("hiding")
    @Override
    public <T> T[] toArray(T[] a)
    {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public boolean containsAll(Collection<?> c)
    {
        // TODO Auto-generated method stub
        return false;
    }

    @Override
    public boolean addAll(Collection<? extends T> c)
    {
        // TODO Auto-generated method stub
        return false;
    }

    @Override
    public boolean removeAll(Collection<?> c)
    {
        // TODO Auto-generated method stub
        return false;
    }

    @Override
    public boolean retainAll(Collection<?> c)
    {
        // TODO Auto-generated method stub
        return false;
    }

    @Override
    public void clear()
    {
        // TODO Auto-generated method stub
        
    }

    @Override
    public boolean add(T e)
    {
        // TODO Auto-generated method stub
        return queue.add(e);
    }

    @Override
    public boolean offer(T e)
    {
        // TODO Auto-generated method stub
        return queue.offer(e);
    }

    @Override
    public void put(T e) throws InterruptedException
    {
        // TODO Auto-generated method stub
        
    }

    @Override
    public boolean offer(T e, long timeout, TimeUnit unit) throws InterruptedException
    {
        // TODO Auto-generated method stub
        return queue.offer(e, timeout, unit);
    }

    @Override
    public T take() throws InterruptedException
    {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public T poll(long timeout, TimeUnit unit) throws InterruptedException
    {
        // TODO Auto-generated method stub
        return queue.poll(timeout, unit);
    }

    @Override
    public int remainingCapacity()
    {
        // TODO Auto-generated method stub
        return 0;
    }

    @Override
    public boolean remove(Object o)
    {
        // TODO Auto-generated method stub
        return false;
    }

    @Override
    public boolean contains(Object o)
    {
        // TODO Auto-generated method stub
        return false;
    }

    @Override
    public int drainTo(Collection<? super T> c)
    {
        // TODO Auto-generated method stub
        return 0;
    }

    @Override
    public int drainTo(Collection<? super T> c, int maxElements)
    {
        // TODO Auto-generated method stub
        return 0;
    }
    
    @Override
    public String toString()
    {
        return queue.toString();
    }

}
