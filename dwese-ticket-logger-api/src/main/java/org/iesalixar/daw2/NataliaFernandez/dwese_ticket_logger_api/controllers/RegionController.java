package org.iesalixar.daw2.NataliaFernandez.dwese_ticket_logger_api.controllers;

import jakarta.validation.Valid;

import org.iesalixar.daw2.NataliaFernandez.dwese_ticket_logger_api.dtos.RegionCreateDTO;
import org.iesalixar.daw2.NataliaFernandez.dwese_ticket_logger_api.dtos.RegionDTO;
import org.iesalixar.daw2.NataliaFernandez.dwese_ticket_logger_api.entities.Region;
import org.iesalixar.daw2.NataliaFernandez.dwese_ticket_logger_api.mappers.RegionMapper;
import org.iesalixar.daw2.NataliaFernandez.dwese_ticket_logger_api.repositories.RegionRepository;
import org.iesalixar.daw2.NataliaFernandez.dwese_ticket_logger_api.services.RegionService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Locale;
import java.util.Optional;

/**
 * Controlador que maneja las operaciones CRUD para la entidad `Region`.
 * Utiliza `RegionDAO` para interactuar con la base de datos.
 */
@RestController
@RequestMapping("/api/v1/ticket-logger/regions")
public class RegionController {

    private static final Logger logger = LoggerFactory.getLogger(RegionController.class);

    @Autowired
    private RegionService regionService;

    @Autowired
    private MessageSource messageSource;
    @Autowired
    private RegionRepository regionRepository;
    @Autowired
    private RegionMapper regionMapper;

    @GetMapping
    public ResponseEntity<Page<RegionDTO>> getAllRegions(@PageableDefault(size = 10, sort = "name") Pageable pageable) {
        logger.info("Solicitando todas las regiones con paginación: página {}, tamaño {}", pageable.getPageNumber(), pageable.getPageSize());
        try {
            Page<RegionDTO> regions = regionService.getAllRegions(pageable);
            logger.info("Se han encontrado {} regiones.", regions.getTotalElements());
            return ResponseEntity.ok(regions);
        } catch (Exception e) {
            logger.error("Error al listar las regiones: {}", e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(null);
        }
    }



    @GetMapping("/{id}")
    public ResponseEntity<?> getRegionById(@PathVariable Long id) {
        logger.info("Buscando región con ID {}", id);
        try {
            Optional<RegionDTO> regionDTO = regionService.getRegionById(id);
            if (regionDTO.isPresent()) {
                logger.info("Región con ID {} encontrada.", id);
                return ResponseEntity.ok(regionDTO.get());
            } else {
                logger.warn("No se encontró ninguna región con ID {}", id);
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body("La región no existe");
            }
        } catch (Exception e) {
            logger.error("Error al buscar la región con ID {}: {}", id, e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Error al buscar la región");
        }
    }

    @PostMapping
    public ResponseEntity<?> createRegion(@Valid @RequestBody RegionCreateDTO regionCreateDTO, Locale locale) {
        try {
            RegionDTO createdRegion = regionService.createRegion(regionCreateDTO, locale);
            return ResponseEntity.status(HttpStatus.CREATED).body(createdRegion);
        } catch (IllegalArgumentException e) {
            logger.error("Error al crear la región, ya existe: {}", e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Error al crear la región");
        }
    }

    @PutMapping("/{id}")
    public ResponseEntity<?> updateRegion(@PathVariable Long id, @Valid @RequestBody RegionCreateDTO regionCreateDTO, Locale locale) {
        try {
            RegionDTO updatedRegion = regionService.updateRegion(id, regionCreateDTO, locale);
            return ResponseEntity.ok(updatedRegion);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
        } catch (Exception e) {
            logger.error("Error al actualizar la región con ID {}: {}", id, e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Error al actualizar la región");
        }
    } // <- Cierra correctamente el método updateRegion

    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteRegion(@PathVariable Long id) {
        try {
            regionService.deleteRegion(id);
            return ResponseEntity.noContent().build();
        } catch (IllegalArgumentException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
        } catch (Exception e) {
            logger.error("Error al eliminar la región con ID {}: {}", id, e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Error al eliminar la región");
        }
    } // <- Cierra correctamente el método deleteRegion
}