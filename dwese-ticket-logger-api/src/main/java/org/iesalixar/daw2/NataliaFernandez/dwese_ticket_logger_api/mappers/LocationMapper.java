package org.iesalixar.daw2.NataliaFernandez.dwese_ticket_logger_api.mappers;

import org.iesalixar.daw2.NataliaFernandez.dwese_ticket_logger_api.dtos.LocationDTO;
import org.iesalixar.daw2.NataliaFernandez.dwese_ticket_logger_api.dtos.LocationCreateDTO;
import org.iesalixar.daw2.NataliaFernandez.dwese_ticket_logger_api.entities.Location;
import org.iesalixar.daw2.NataliaFernandez.dwese_ticket_logger_api.entities.Province;
import org.iesalixar.daw2.NataliaFernandez.dwese_ticket_logger_api.entities.Supermarket;
import org.springframework.stereotype.Component;

@Component
public class LocationMapper {

    private final ProvinceMapper provinceMapper;
    private final SupermarketMapper supermarketMapper;

    public LocationMapper(ProvinceMapper provinceMapper, SupermarketMapper supermarketMapper) {
        this.provinceMapper = provinceMapper;
        this.supermarketMapper = supermarketMapper;
    }

    public LocationDTO toDTO(Location location) {
        LocationDTO dto = new LocationDTO();
        dto.setId(location.getId());
        dto.setAddress(location.getAddress());
        dto.setCity(location.getCity());
        dto.setSupermarket(supermarketMapper.toDTO(location.getSupermarket()));
        dto.setProvince(provinceMapper.toDTO(location.getProvince()));
        return dto;
    }

    public Location toEntity(LocationDTO locationDTO) {
        Location location = new Location();
        location.setId(locationDTO.getId());
        location.setAddress(locationDTO.getAddress());
        location.setCity(locationDTO.getCity());

        location.setSupermarket(supermarketMapper.toEntity(locationDTO.getSupermarket()));
        location.setProvince(provinceMapper.toEntity(locationDTO.getProvince()));
        return location;
    }

    public Location toEntity(LocationCreateDTO locationCreateDTO, Supermarket supermarket, Province province) {
        Location location = new Location();
        location.setAddress(locationCreateDTO.getAddress());
        location.setCity(locationCreateDTO.getCity());
        location.setSupermarket(supermarket);
        location.setProvince(province);
        return location;
    }
}