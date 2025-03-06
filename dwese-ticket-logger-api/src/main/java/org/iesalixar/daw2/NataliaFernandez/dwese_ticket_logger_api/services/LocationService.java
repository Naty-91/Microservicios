package org.iesalixar.daw2.NataliaFernandez.dwese_ticket_logger_api.services;

import org.iesalixar.daw2.NataliaFernandez.dwese_ticket_logger_api.dtos.LocationCreateDTO;
import org.iesalixar.daw2.NataliaFernandez.dwese_ticket_logger_api.dtos.LocationDTO;
import org.iesalixar.daw2.NataliaFernandez.dwese_ticket_logger_api.entities.Location;
import org.iesalixar.daw2.NataliaFernandez.dwese_ticket_logger_api.entities.Province;
import org.iesalixar.daw2.NataliaFernandez.dwese_ticket_logger_api.entities.Supermarket;
import org.iesalixar.daw2.NataliaFernandez.dwese_ticket_logger_api.mappers.LocationMapper;
import org.iesalixar.daw2.NataliaFernandez.dwese_ticket_logger_api.repositories.LocationRepository;
import org.iesalixar.daw2.NataliaFernandez.dwese_ticket_logger_api.repositories.ProvinceRepository;
import org.iesalixar.daw2.NataliaFernandez.dwese_ticket_logger_api.repositories.SupermarketRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Locale;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class LocationService {

    private static final Logger logger = LoggerFactory.getLogger(LocationService.class);

    @Autowired
    private LocationRepository locationRepository;

    @Autowired
    private LocationMapper locationMapper;

    @Autowired
    private MessageSource messageSource;

    @Autowired
    private ProvinceRepository provinceRepository;

    @Autowired
    private SupermarketRepository supermarketRepository;

    /**
     * Lista todas las ubicaciones almacenadas en la base de datos.
     *
     * @return Lista de LocationDTO.
     */
    public List<LocationDTO> getAllLocations() {
        try {
            logger.info("Solicitando la lista de todas las ubicaciones...");
            List<Location> locations = locationRepository.findAll();
            logger.info("Se han encontrado {} ubicaciones.", locations.size());

            return locations.stream()
                    .map(locationMapper::toDTO)
                    .collect(Collectors.toList());
        } catch (Exception e) {
            logger.error("Error al listar las ubicaciones: {}", e.getMessage());
            throw new RuntimeException("Error al obtener todas las ubicaciones", e);
        }
    }

    /**
     * Obtiene una ubicación específica por su ID.
     *
     * @param id ID de la ubicación solicitada.
     * @return Optional con el DTO de la ubicación encontrada o vacío si no existe.
     */
    public Optional<LocationDTO> getLocationById(Long id) {
        try {
            logger.info("Buscando ubicación con ID {}", id);
            return locationRepository.findById(id)
                    .map(locationMapper::toDTO);
        } catch (Exception e) {
            logger.error("Error al buscar la ubicación con ID {}: {}", id, e.getMessage());
            throw new RuntimeException("Error al buscar la ubicación", e);
        }
    }

    /**
     * Crea una nueva ubicación en la base de datos.
     *
     * @param locationCreateDTO DTO que contiene los datos de la ubicación a crear.
     * @param locale            Idioma para los mensajes de error.
     * @return DTO de la ubicación creada.
     */
    public LocationDTO createLocation(LocationCreateDTO locationCreateDTO, Locale locale) {
        try {
            // Verificar si la dirección ya existe
            if (locationRepository.existsByAddress(locationCreateDTO.getAddress())) {
                String errorMessage = messageSource.getMessage("msg.location-controller.insert.addressExist", null, locale);
                logger.warn("Error al crear la ubicación: la dirección {} ya existe", locationCreateDTO.getAddress());
                throw new IllegalArgumentException(errorMessage);
            }

            // Validar y obtener Supermarket relacionado
            Supermarket supermarket = supermarketRepository.findById(locationCreateDTO.getSupermarketId())
                    .orElseThrow(() -> new IllegalArgumentException("Supermercado no encontrado con ID: " + locationCreateDTO.getSupermarketId()));

            // Validar y obtener Province relacionada
            Province province = provinceRepository.findById(locationCreateDTO.getProvinceId())
                    .orElseThrow(() -> new IllegalArgumentException("Provincia no encontrada con ID: " + locationCreateDTO.getProvinceId()));

            // Mapear Location usando el método adecuado del mapper
            Location location = locationMapper.toEntity(locationCreateDTO, supermarket, province);

            // Guardar Location en la base de datos
            Location savedLocation = locationRepository.save(location);

            logger.info("Ubicación creada exitosamente con ID {}", savedLocation.getId());
            return locationMapper.toDTO(savedLocation);

        } catch (Exception e) {
            logger.error("Error al crear la ubicación: {}", e.getMessage());
            throw new RuntimeException("Error al crear la ubicación", e);
        }
    }


    /**
     * Actualiza una ubicación existente.
     *
     * @param id                  Identificador de la ubicación a actualizar.
     * @param locationCreateDTO   DTO que contiene los nuevos datos de la ubicación.
     * @param locale              Idioma para los mensajes de error.
     * @return DTO de la ubicación actualizada.
     */
    public LocationDTO updateLocation(Long id, LocationCreateDTO locationCreateDTO, Locale locale) {
        try {
            Location existingLocation = locationRepository.findById(id)
                    .orElseThrow(() -> new IllegalArgumentException("La ubicación no existe."));

            // Verificar si ya existe otra ubicación con la misma dirección y diferente ID
            if (locationRepository.existsLocationByAddressAndNotId(locationCreateDTO.getAddress(), id)) {
                String errorMessage = messageSource.getMessage("msg.location-controller.update.addressExist", null, locale);
                logger.warn("La dirección de la ubicación {} ya existe para otra ubicación.", locationCreateDTO.getAddress());
                throw new IllegalArgumentException(errorMessage);
            }

            existingLocation.setAddress(locationCreateDTO.getAddress());
            existingLocation.setProvince(locationCreateDTO.getProvince());
            existingLocation.setSupermarket(locationCreateDTO.getSupermarket());

            Location updatedLocation = locationRepository.save(existingLocation);
            return locationMapper.toDTO(updatedLocation);

        } catch (Exception e) {
            logger.error("Error al actualizar la ubicación con ID {}: {}", id, e.getMessage());
            throw new RuntimeException("Error al actualizar la ubicación", e);
        }
    }

    /**
     * Elimina una ubicación específica por su ID.
     *
     * @param id Identificador único de la ubicación.
     */
    public void deleteLocation(Long id) {
        try {
            if (!locationRepository.existsById(id)) {
                throw new IllegalArgumentException("La ubicación no existe");
            }
            locationRepository.deleteById(id);
            logger.info("Ubicación con ID {} eliminada exitosamente.", id);
        } catch (Exception e) {
            logger.error("Error al eliminar la ubicación con ID {}: {}", id, e.getMessage());
            throw new RuntimeException("Error al eliminar la ubicación", e);
        }
    }
}
