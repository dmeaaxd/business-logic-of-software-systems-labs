package org.example.blps_lab3_notificationservice.controller;

import lombok.AllArgsConstructor;
import org.example.blps_lab3_notificationservice.dto.MessageDTO;
import org.example.blps_lab3_notificationservice.entity.Message;
import org.example.blps_lab3_notificationservice.service.MessageService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.mail.MailException;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/")
@AllArgsConstructor
public class RestMessageController {
    private MessageService messageService;

    @PostMapping("/send")
    public ResponseEntity<?> sendMessage(@RequestBody MessageDTO messageDTO) {

        Map<String, String> response = new HashMap<>();

        if (!messageDTO.check()) {
            response.put("error", "Переданы неверные параметры в запросе");
            return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
        }

        try {
            messageService.sendMessage(Message.builder()
                    .to(messageDTO.getTo())
                    .theme(messageDTO.getTheme())
                    .text(messageDTO.getText())
                    .build());
        } catch (MailException exception) {
            response.put("error", exception.getLocalizedMessage());
            return new ResponseEntity<>(response, HttpStatus.INTERNAL_SERVER_ERROR);
        }

        response.put("result", "Сообщение отправлено");
        return new ResponseEntity<>(response, HttpStatus.OK);
    }
}
