package org.example.blps_lab3_notificationservice.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class MessageDTO {
    private String to; // Почта получателя
    private String theme; // Тема письма
    private String text; // Тело сообщения

    public boolean check(){
        return (to != null && !to.isEmpty()) &&
                (theme != null && !theme.isEmpty()) &&
                (text != null && !text.isEmpty());
    }
}
