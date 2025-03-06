package org.iesalixar.daw2.NataliaFernandez.dwese_ticket_logger_api.mappers;

import org.iesalixar.daw2.NataliaFernandez.dwese_ticket_logger_api.dtos.ProvinceDTO;
import org.iesalixar.daw2.NataliaFernandez.dwese_ticket_logger_api.dtos.ProvinceCreateDTO;
import org.iesalixar.daw2.NataliaFernandez.dwese_ticket_logger_api.entities.Province;
import org.iesalixar.daw2.NataliaFernandez.dwese_ticket_logger_api.entities.Region;
import org.springframework.stereotype.Component;

/**
 * Componente Spring que actúa como un mapper para convertir entre
 * entidades de Province y sus DTO asociados.
 */
@Component
public class ProvinceMapper {

    private final RegionMapper regionMapper;

    // Constructor para inyección de dependencias del RegionMapper.
    public ProvinceMapper(RegionMapper regionMapper) {
        this.regionMapper = regionMapper;
    }

    /**
     * Convierte una entidad de tipo Province en un objeto de tipo ProvinceDTO.
     *
     * @param province la entidad de tipo Province que se quiere convertir
     * @return un objeto ProvinceDTO que contiene los datos de la entidad
     */
    public ProvinceDTO toDTO(Province province) {
        ProvinceDTO dto = new ProvinceDTO();
        dto.setId(province.getId());
        dto.setCode(province.getCode());
        dto.setName(province.getName());
        dto.setRegion(regionMapper.toDTO(province.getRegion())); // Convierte la región a DTO
        return dto;
    }

    /**
     * Convierte un objeto ProvinceDTO en una entidad de tipo Province.
     *
     * @param provinceDTO el DTO que se quiere convertir a una entidad
     * @return una nueva instancia de Province con los datos del DTO
     */
    public Province toEntity(ProvinceDTO provinceDTO) {
        Province province = new Province();
        province.setId(provinceDTO.getId());
        province.setCode(provinceDTO.getCode());
        province.setName(provinceDTO.getName());
        province.setRegion(regionMapper.toEntity(provinceDTO.getRegion())); // Convierte el DTO de región a entidad
        return province;
    }

    /**
     * Convierte un objeto ProvinceCreateDTO en una entidad de tipo Province.
     * Este método es útil para crear nuevas entidades a partir de datos enviados desde el cliente.
     *
     * @param provinceCreateDTO el DTO que contiene los datos necesarios para crear una Province
     * @param region la región asociada a esta provincia (se debe obtener previamente del servicio/DB)
     * @return una nueva instancia de Province con los datos del ProvinceCreateDTO
     */
    public Province toEntity(ProvinceCreateDTO provinceCreateDTO, Region region) {
        Province province = new Province();
        province.setCode(provinceCreateDTO.getCode());
        province.setName(provinceCreateDTO.getName());
        province.setRegion(region); // Asigna la región existente
        return province;
    }
}