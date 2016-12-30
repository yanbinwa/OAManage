package com.yanbinwa.OASystem.Service;

public interface NotificationService
{
    public static final String QUEUE_CAPACITY = "Notification_QueueCapacity";
    public static final String CORE_POOL_SIZE = "Notification_CorePoolSize";
    public static final String MAX_POOL_SIZE = "Notification_MaxPoolSize";
    public static final String KEEP_ALIVE_SECONDS = "Notification_KeepAliveSeconds";
    public static final String EVENT_QUEUE_SIZE = "Notification_EventQueueSize";
    
    public static final String NOTIFICATION_ID_BAK = "Notification_NotificationIdBak";
    
    public static final int NOTIFICATION_NEVER_EXPIRED_TIME = -1;
    public static final int NOTIFICATION_QUICK_EXPIRED_TIME = 60000;
    public static final int NOTIFICATION_NORMAL_EXPIRED_TIME = 600000;
    public static final int NOTIFICATION_SLOW_EXPIRED_TIME = 3600000;
}
