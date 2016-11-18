//package com.yanbinwa.OASystem.Service;
//
//import javax.jms.JMSException;
//import javax.jms.Message;
//import javax.jms.ObjectMessage;
//import javax.jms.Session;
//
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.jms.core.JmsTemplate;
//import org.springframework.jms.core.MessageCreator;
//import org.springframework.stereotype.Service;
//
//import com.yanbinwa.OASystem.Model.Event;
//
//@Service("messageSender")
//public class MessageSenderImpl implements MessageSender
//{
//    @Autowired
//    private JmsTemplate jmsTemplate;
//
//    @Override
//    public void sendMessage(Event event)
//    {
//        // TODO Auto-generated method stub
//        jmsTemplate.send(new MessageCreator() {
//
//            @Override
//            public Message createMessage(Session session) throws JMSException
//            {
//                // TODO Auto-generated method stub
//                ObjectMessage objectMessage = session.createObjectMessage(event);
//                return objectMessage;
//            }
//            
//        });
//    }
//
//}
