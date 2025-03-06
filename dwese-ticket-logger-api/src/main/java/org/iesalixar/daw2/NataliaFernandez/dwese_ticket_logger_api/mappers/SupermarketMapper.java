package org.iesalixar.daw2.NataliaFernandez.dwese_ticket_logger_api.mappers;

import org.iesalixar.daw2.NataliaFernandez.dwese_ticket_logger_api.dtos.SupermarketCreateDTO;
import org.iesalixar.daw2.NataliaFernandez.dwese_ticket_logger_api.dtos.SupermarketDTO;
import org.iesalixar.daw2.NataliaFernandez.dwese_ticket_logger_api.entities.Supermarket;
import org.springframework.stereotype.Component;


@Component
public class SupermarketMapper {

    /**
     * Convierte una entidad de tipo Supermarket en un objeto de tipo SupermarketDTO.
     *
     * @param supermarket la entidad de tipo Supermarket que se quiere convertir
     * @return un objeto SupermarketDTO que contiene los datos de la entidad
     */
    public SupermarketDTO toDTO(Supermarket supermarket) {
        // Crear un nuevo objeto SupermarketDTO
        SupermarketDTO dto = new SupermarketDTO();
        // Mapear los atributos de la entidad Supermarket al DTO
        dto.setName(supermarket.getName());
        return dto;
    }

    /**
     * Convierte un objeto SupermarketDTO en una entidad de tipo Supermarket.
     *
     * @param dto el DTO que se quiere convertir a una entidad
     * @return una nueva instancia de Supermarket con los datos del DTO
     */
    public Supermarket toEntity(SupermarketDTO dto) {
        // Crear una nueva instancia de la entidad Supermarket
        Supermarket supermarket = new Supermarket();
        // Mapear los atributos del DTO a la entidad
        supermarket.setName(dto.getName());
        return supermarket;
    }

    /**
     * Convierte un objeto SupermarketCreateDTO en una entidad de tipo Supermarket.
     *
     * @param dto el DTO que se quiere convertir a una entidad
     * @return una nueva instancia de Supermarket con los datos del DTO
     */
    public Supermarket toEntity(SupermarketCreateDTO dto) {
        // Crear una nueva instancia de la entidad Supermarket
        Supermarket supermarket = new Supermarket();
        // Mapear los atributos del DTO a la entidad
        supermarket.setName(dto.getName());
        return supermarket;
    }
}
