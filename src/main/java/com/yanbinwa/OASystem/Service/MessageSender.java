package com.yanbinwa.OASystem.Service;

import com.yanbinwa.OASystem.Event.Event;

public interface MessageSender
{
    public void sendMessage(final Event event);
}
