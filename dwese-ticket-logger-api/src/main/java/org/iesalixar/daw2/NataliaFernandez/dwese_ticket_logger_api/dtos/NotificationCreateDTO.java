package org.iesalixar.daw2.NataliaFernandez.dwese_ticket_logger_api.dtos;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.Instant;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor

public class NotificationCreateDTO {

    private String subject;
    private String message;
    private boolean read;
}
