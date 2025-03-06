package org.iesalixar.daw2.NataliaFernandez.dwese_ticket_logger_api.controllers;

import org.iesalixar.daw2.NataliaFernandez.dwese_ticket_logger_api.dtos.NotificationCreateDTO;
import org.iesalixar.daw2.NataliaFernandez.dwese_ticket_logger_api.dtos.NotificationDTO;
import org.iesalixar.daw2.NataliaFernandez.dwese_ticket_logger_api.services.NotificationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@RestController
@RequestMapping("/ws/notifications")
public class NotificationController {

    @Autowired
    private NotificationService notificationService;


    @GetMapping
    public Flux<NotificationDTO> getAllNotifications() {
        // Llama al servicio para obtener todas las notificaciones
        return notificationService.getAllNotifications();
    }

/**
 * Crea una nueva notificación y la distribuye a los clientes WebSocket.
 *
 * @param notificationCreateDTO DTO con los datos necesarios para crear la notificación.
 * @return Un Mono con la notificación creada en formato DTO.
 */
@PostMapping
public Mono<NotificationDTO> createNotification(@RequestBody NotificationCreateDTO notificationCreateDTO) {
    // Llama al servicio para guardar la notificación
    return notificationService.saveNotification(notificationCreateDTO);
}
}
