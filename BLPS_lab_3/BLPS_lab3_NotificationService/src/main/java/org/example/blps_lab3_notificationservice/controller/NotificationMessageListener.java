package org.example.blps_lab3_notificationservice.controller;


import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.jms.TextMessage;
import lombok.AllArgsConstructor;
import org.example.blps_lab3_notificationservice.dto.MessageDTO;
import org.example.blps_lab3_notificationservice.entity.Message;
import org.example.blps_lab3_notificationservice.service.MessageService;
import org.springframework.jms.annotation.JmsListener;
import org.springframework.mail.MailException;
import org.springframework.stereotype.Component;
import org.w3c.dom.Text;

@Component
@AllArgsConstructor
public class NotificationMessageListener {
    private final MessageService messageService;


    @JmsListener(destination = "notificationQueue")
    public void receiveNotificationMessage(TextMessage textMessage) throws Exception {

        MessageDTO messageDTO = convertTextMessageToObject(textMessage.getText(), MessageDTO.class);
        System.out.println(messageDTO);

        try {
            messageService.sendMessage(Message.builder()
                    .to(messageDTO.getTo())
                    .theme(messageDTO.getTheme())
                    .text(messageDTO.getText())
                    .build());
        } catch (MailException exception) {
            System.err.println("Ошибка отправки сообщения: " + exception.getLocalizedMessage());
        }
    }


    private <T> T convertTextMessageToObject(String jsonMessage, Class<T> clazz) {
        try {
            return new ObjectMapper().readValue(jsonMessage, clazz);
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }
    }


}
