package org.iesalixar.daw2.NataliaFernandez.dwese_ticket_logger_api.entities;


import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import lombok.Getter;
import lombok.Setter;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;

import java.time.Instant;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Document(collection = "notifications")
public class Notification {

    /**
     * Identificador único de la notificación.
     */
    @Id
    private String id;

    /**
     * Asunto o título de la notificación.
     */
    private String subject;

    /**
     * Mensaje descriptivo del contenido de la notificación.
     */
    private String message;

    /**
     * Indica si la notificación ha sido leída o no.
     */
    private boolean read;

    /**
     * Fecha y hora en la que se creó la notificación.
     * Se inicializa con la fecha y hora actual por defecto.
     */
    private Instant createdAt = Instant.now();
}
