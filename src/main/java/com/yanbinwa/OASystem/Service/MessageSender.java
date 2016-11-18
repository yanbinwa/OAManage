package com.yanbinwa.OASystem.Service;

import com.yanbinwa.OASystem.Model.Event;

public interface MessageSender
{
    public void sendMessage(final Event event);
}
