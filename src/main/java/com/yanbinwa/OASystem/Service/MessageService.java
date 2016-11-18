package com.yanbinwa.OASystem.Service;

import javax.websocket.Session;

public interface MessageService
{
    public static final int ONOPEN = 1;
    public static final int ONCLOASE = 2;
    public static final int ONMESSAGE = 3;
    public void handleMessage(Session session, String message, int type);
}
