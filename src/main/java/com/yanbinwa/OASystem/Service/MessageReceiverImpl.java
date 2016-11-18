//package com.yanbinwa.OASystem.Service;
//
//import java.util.HashMap;
//import java.util.HashSet;
//import java.util.Map;
//import java.util.Set;
//
//import javax.annotation.PostConstruct;
//import javax.jms.JMSException;
//import javax.jms.Message;
//import javax.jms.MessageListener;
//
//import org.apache.log4j.Logger;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.jms.support.converter.MessageConversionException;
//import org.springframework.jms.support.converter.MessageConverter;
//import org.springframework.stereotype.Service;
//
//import com.yanbinwa.OASystem.Common.EventListener;
//import com.yanbinwa.OASystem.Model.Event;
//
//
///**
// * Subscribe for the service
// * @author yanbinwa
// *
// */
//
//@Service("messageReceiver")
//public class MessageReceiverImpl implements MessageReceiver, MessageListener
//{
//    @Autowired
//    MessageConverter messageConverter;
//    
//    private static final Logger logger = Logger.getLogger(MessageReceiverImpl.class);
//    
//    private static Map<String, Set<EventListener>> keyToListener;
//
//    @PostConstruct
//    public void init()
//    {
//        keyToListener = new HashMap<String, Set<EventListener>>();
//    }
//    
//    @Override
//    public void onMessage(Message message)
//    {
//        // TODO Auto-generated method stub
//        try
//        {
//            Event response = (Event) messageConverter.fromMessage(message);
//            logger.info(response);
//            String key = response.getName();
//            Set<EventListener> listenerSet = keyToListener.get(key);
//            if(listenerSet == null)
//            {
//                return;
//            }
//            for(EventListener listener : listenerSet)
//            {
//                listener.callBack(response);
//            }
//        } 
//        catch (MessageConversionException e)
//        {
//            // TODO Auto-generated catch block
//            e.printStackTrace();
//        } catch (JMSException e)
//        {
//            // TODO Auto-generated catch block
//            e.printStackTrace();
//        }
//        
//    }
//
//    @Override
//    public void register(EventListener listener, String[] keys)
//    {
//        // TODO Auto-generated method stub
//        if (keys == null)
//        {
//            return;
//        }
//        for(String key : keys)
//        {
//            Set<EventListener> listenerSet = keyToListener.get(key);
//            if(listenerSet == null)
//            {
//                listenerSet = new HashSet<EventListener>();
//                keyToListener.put(key, listenerSet);
//            }
//            listenerSet.add(listener);
//        }
//    }
//
//}
