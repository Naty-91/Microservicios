package org.iesalixar.daw2.NataliaFernandez.dwese_ticket_logger_api.services;

import org.iesalixar.daw2.NataliaFernandez.dwese_ticket_logger_api.dtos.NotificationCreateDTO;
import org.iesalixar.daw2.NataliaFernandez.dwese_ticket_logger_api.dtos.NotificationDTO;
import org.iesalixar.daw2.NataliaFernandez.dwese_ticket_logger_api.entities.Notification;
import org.iesalixar.daw2.NataliaFernandez.dwese_ticket_logger_api.mappers.NotificationMapper;
import org.iesalixar.daw2.NataliaFernandez.dwese_ticket_logger_api.repositories.NotificationRepository;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import reactor.core.scheduler.Schedulers;


@Service
public class NotificationService {

    // Logger de la clase
    private static final Logger logger = LoggerFactory.getLogger(NotificationService.class);

    // Inyección de dependencias
    @Autowired
    private NotificationRepository notificationRepository;

    @Autowired
    private SimpMessagingTemplate messagingTemplate;

    public Mono<NotificationDTO> saveNotification(NotificationCreateDTO notificationCreateDTO) {
        // Convertimos el DTO de entrada a la entidad Notification
        Notification notification = NotificationMapper.toEntity(notificationCreateDTO);

        // Guardamos la notificación en la base de datos
        return notificationRepository.save(notification)
                // Se ejecuta cuando la notificación ha sido guardada con éxito
                .doOnSuccess(savedNotification -> {
                    // Enviamos la notificación por WebSocket usando SimpMessagingTemplate
                    Mono.fromRunnable(() ->
                                    messagingTemplate.convertAndSend(
                                            "/topic/notifications", // Canal de WebSocket donde se enviará la notificación
                                            NotificationMapper.toDTO(savedNotification) // Convertimos la entidad guardada a DTO
                                    )
                            )
                            // Ejecutamos la tarea en un pool de hilos elástico para evitar bloqueos
                            .subscribeOn(Schedulers.boundedElastic())
                            .subscribe(); // Nos suscribimos para ejecutar la tarea asíncrona de envío
                })
                // Convertimos la notificación guardada en DTO antes de devolverla
                .map(NotificationMapper::toDTO);
    }


    public Flux <NotificationDTO>getAllNotifications(){
    return notificationRepository.findAll().map(NotificationMapper::toDTO);



    }
}
