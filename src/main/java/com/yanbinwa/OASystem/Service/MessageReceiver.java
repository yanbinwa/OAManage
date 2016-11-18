package com.yanbinwa.OASystem.Service;

import com.yanbinwa.OASystem.Common.EventListener;

public interface MessageReceiver
{
    public void register(EventListener listener, String[] keys);
}
