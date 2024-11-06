package org.example.blps_lab3_notificationservice.entity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Message {
    private String to; // Почта получателя
    private String theme; // Тема письма
    private String text; // Тело сообщения
}
