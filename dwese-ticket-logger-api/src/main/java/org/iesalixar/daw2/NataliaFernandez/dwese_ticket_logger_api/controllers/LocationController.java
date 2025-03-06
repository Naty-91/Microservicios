package org.iesalixar.daw2.NataliaFernandez.dwese_ticket_logger_api.controllers;

import jakarta.validation.Valid;
import org.iesalixar.daw2.NataliaFernandez.dwese_ticket_logger_api.dtos.LocationCreateDTO;
import org.iesalixar.daw2.NataliaFernandez.dwese_ticket_logger_api.dtos.LocationDTO;
import org.iesalixar.daw2.NataliaFernandez.dwese_ticket_logger_api.services.LocationService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Locale;
import java.util.Optional;

/**
 * Controlador que maneja las operaciones CRUD para la entidad `Location`.
 * Utiliza `LocationService` para interactuar con la lógica de negocio.
 */
@RestController
@RequestMapping("/api/v1/ticket-logger/locations")
public class LocationController {

    private static final Logger logger = LoggerFactory.getLogger(LocationController.class);

    @Autowired
    private LocationService locationService;

    @Autowired
    private MessageSource messageSource;

    @GetMapping
    public ResponseEntity<List<LocationDTO>> getAllLocations() {
        logger.info("Solicitando la lista de todas las ubicaciones...");
        try {
            List<LocationDTO> locationDTOs = locationService.getAllLocations();
            logger.info("Se han cargado {} ubicaciones.", locationDTOs.size());
            return ResponseEntity.ok(locationDTOs);
        } catch (Exception e) {
            logger.error("Error al listar las ubicaciones: {}", e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(null);
        }
    }

    @GetMapping("/{id}")
    public ResponseEntity<?> getLocationById(@PathVariable Long id) {
        logger.info("Buscando ubicación con ID {}", id);
        try {
            Optional<LocationDTO> locationDTO = locationService.getLocationById(id);
            if (locationDTO.isPresent()) {
                logger.info("Ubicación con ID {} encontrada.", id);
                return ResponseEntity.ok(locationDTO.get());
            } else {
                logger.warn("No se encontró ninguna ubicación con ID {}", id);
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body("La ubicación no existe");
            }
        } catch (Exception e) {
            logger.error("Error al buscar la ubicación con ID {}: {}", id, e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Error al buscar la ubicación");
        }
    }

    @PostMapping
    public ResponseEntity<?> createLocation(@Valid @RequestBody LocationCreateDTO locationCreateDTO, Locale locale) {
        try {
            LocationDTO createdLocation = locationService.createLocation(locationCreateDTO, locale);
            return ResponseEntity.status(HttpStatus.CREATED).body(createdLocation);
        } catch (IllegalArgumentException e) {
            logger.error("Error al crear la ubicación, ya existe: {}", e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Error al crear la ubicación");
        }
    }

    @PutMapping("/{id}")
    public ResponseEntity<?> updateLocation(@PathVariable Long id, @Valid @RequestBody LocationCreateDTO locationCreateDTO, Locale locale) {
        try {
            LocationDTO updatedLocation = locationService.updateLocation(id, locationCreateDTO, locale);
            return ResponseEntity.ok(updatedLocation);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
        } catch (Exception e) {
            logger.error("Error al actualizar la ubicación con ID {}: {}", id, e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Error al actualizar la ubicación");
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteLocation(@PathVariable Long id) {
        try {
            locationService.deleteLocation(id);
            return ResponseEntity.noContent().build();
        } catch (IllegalArgumentException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
        } catch (Exception e) {
            logger.error("Error al eliminar la ubicación con ID {}: {}", id, e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Error al eliminar la ubicación");
        }
    }
}
