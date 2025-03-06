package org.iesalixar.daw2.NataliaFernandez.dwese_ticket_logger_api.dtos;


import jakarta.validation.constraints.NotEmpty;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class SupermarketCreateDTO {

    @NotEmpty(message = "{msg.supermarket.name.notEmpty}")
    private String name;



}
