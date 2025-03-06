package org.iesalixar.daw2.NataliaFernandez.dwese_ticket_logger_api.services;


import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Service;

import static org.iesalixar.daw2.NataliaFernandez.dwese_ticket_logger_api.config.RabbitMQConfig.QUEUE_NAME;

@Service
public class RabbitMQConsumer {
    @RabbitListener (queues = QUEUE_NAME)
    public void receiveMessage(String message){
        System.out.println("Se ha recibido el mensaje de RabbitMQ:" + message);
    }
}
