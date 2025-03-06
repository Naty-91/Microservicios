package org.iesalixar.daw2.NataliaFernandez.dwese_ticket_logger_api.mappers;

import org.iesalixar.daw2.NataliaFernandez.dwese_ticket_logger_api.dtos.RegionCreateDTO;
import org.iesalixar.daw2.NataliaFernandez.dwese_ticket_logger_api.dtos.RegionDTO;
import org.iesalixar.daw2.NataliaFernandez.dwese_ticket_logger_api.entities.Region;
import org.springframework.stereotype.Component;

/**
 * Componente Spring que actúa como un mapper para convertir entre
 * entidades de Region y sus DTO asociados.
 */
@Component
public class RegionMapper {

    /**
     * Convierte una entidad de tipo Region en un objeto de tipo RegionDTO.
     *
     * @param region la entidad de tipo Region que se quiere convertir
     * @return un objeto RegionDTO que contiene los datos de la entidad
     */
    public RegionDTO toDTO(Region region) {
        // Crear un nuevo objeto RegionDTO
        RegionDTO dto = new RegionDTO();
        // Mapear los atributos de la entidad Region al DTO
        dto.setId(region.getId());
        dto.setCode(region.getCode());
        dto.setName(region.getName());
        return dto;
    }

    /**
     * Convierte un objeto RegionDTO en una entidad de tipo Region.
     *
     * @param regionDTO el DTO que se quiere convertir a una entidad
     * @return una nueva instancia de Region con los datos del DTO
     */
    public Region toEntity(RegionDTO regionDTO) {
        // Crear una nueva instancia de la entidad Region
        Region region = new Region();
        // Mapear los atributos del DTO a la entidad
        region.setId(regionDTO.getId());
        region.setCode(regionDTO.getCode());
        region.setName(regionDTO.getName());
        return region;
    }

    /**
     * Convierte un objeto RegionCreateDTO en una entidad de tipo Region.
     * Este método es útil para crear nuevas entidades a partir de datos
     * enviados desde el cliente.
     *
     * @param regionCreateDTO el DTO que contiene los datos necesarios para crear una Region
     * @return una nueva instancia de Region con los datos del RegionCreateDTO
     */
    public Region toEntity(RegionCreateDTO regionCreateDTO) {
        // Crear una nueva instancia de la entidad Region
        Region region = new Region();
        // Mapear los atributos del RegionCreateDTO a la entidad
        region.setCode(regionCreateDTO.getCode());
        region.setName(regionCreateDTO.getName());
        return region;
    }
}