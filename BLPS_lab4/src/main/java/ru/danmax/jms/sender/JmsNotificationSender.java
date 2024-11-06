package ru.danmax.jms.sender;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jms.core.JmsTemplate;
import org.springframework.stereotype.Component;
import ru.danmax.jms.message.NotificationJmsMessage;

@Component
public class JmsNotificationSender {
    @Autowired
    private JmsTemplate jmsTemplate;

    public void sendNotification(NotificationJmsMessage notificationJmsMessage) throws Exception {
        if (!notificationJmsMessage.check()){
            throw new Exception("Incorrect notification message");
        }
        jmsTemplate.convertAndSend("notificationQueue", notificationJmsMessage);
    }
}
