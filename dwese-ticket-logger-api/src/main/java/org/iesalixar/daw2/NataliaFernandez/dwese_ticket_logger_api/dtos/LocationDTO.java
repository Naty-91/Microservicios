package org.iesalixar.daw2.NataliaFernandez.dwese_ticket_logger_api.dtos;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter

public class LocationDTO {

private Long id;
private String address;
private String city;
private SupermarketDTO supermarket; // DTO simplificado de Supermarket
private ProvinceDTO province; // DTO simplificado de Province


}
