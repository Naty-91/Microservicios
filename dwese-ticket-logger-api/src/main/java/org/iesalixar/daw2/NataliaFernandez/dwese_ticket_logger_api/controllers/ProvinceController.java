package org.iesalixar.daw2.NataliaFernandez.dwese_ticket_logger_api.controllers;

import jakarta.validation.Valid;
import org.iesalixar.daw2.NataliaFernandez.dwese_ticket_logger_api.dtos.ProvinceCreateDTO;
import org.iesalixar.daw2.NataliaFernandez.dwese_ticket_logger_api.dtos.ProvinceDTO;
import org.iesalixar.daw2.NataliaFernandez.dwese_ticket_logger_api.services.ProvinceService;
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
 * Controlador que maneja las operaciones CRUD para la entidad `Province`.
 */
@RestController
@RequestMapping("/api/v1/ticket-logger/provinces")
public class ProvinceController {

    private static final Logger logger = LoggerFactory.getLogger(ProvinceController.class);

    @Autowired
    private ProvinceService provinceService;

    @Autowired
    private MessageSource messageSource;

    /**
     * Lista todas las provincias.
     * @return ResponseEntity con la lista de provincias.
     */
    @GetMapping
    public ResponseEntity<List<ProvinceDTO>> getAllProvinces() {
        logger.info("Solicitando la lista de todas las provincias...");
        try {
            List<ProvinceDTO> provinceDTOs = provinceService.getAllProvinces();
            logger.info("Se han cargado {} provincias.", provinceDTOs.size());
            return ResponseEntity.ok(provinceDTOs);
        } catch (Exception e) {
            logger.error("Error al listar las provincias: {}", e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(null);
        }
    }

    /**
     * Obtiene una provincia por su ID.
     * @param id ID de la provincia solicitada.
     * @return ResponseEntity con la provincia encontrada o un mensaje de error.
     */
    @GetMapping("/{id}")
    public ResponseEntity<?> getProvinceById(@PathVariable Long id) {
        logger.info("Buscando provincia con ID {}", id);
        try {
            Optional<ProvinceDTO> provinceDTO = provinceService.getProvinceById(id);
            if (provinceDTO.isPresent()) {
                logger.info("Provincia con ID {} encontrada.", id);
                return ResponseEntity.ok(provinceDTO.get());
            } else {
                logger.warn("No se encontró ninguna provincia con ID {}", id);
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body("La provincia no existe.");
            }
        } catch (Exception e) {
            logger.error("Error al buscar la provincia con ID {}: {}", id, e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Error al buscar la provincia.");
        }
    }

    /**
     * Crea una nueva provincia.
     * @param provinceCreateDTO DTO con los datos de la provincia a crear.
     * @param locale Idioma de los mensajes de error.
     * @return ResponseEntity con la provincia creada o un mensaje de error.
     */
    @PostMapping
    public ResponseEntity<?> createProvince(@Valid @RequestBody ProvinceCreateDTO provinceCreateDTO, Locale locale) {
        logger.info("Insertando nueva provincia con código {}", provinceCreateDTO.getCode());
        try {
            ProvinceDTO createdProvince = provinceService.createProvince(provinceCreateDTO, locale);
            logger.info("Provincia creada exitosamente con ID {}", createdProvince.getId());
            return ResponseEntity.status(HttpStatus.CREATED).body(createdProvince);
        } catch (IllegalArgumentException e) {
            logger.error("Error al crear la provincia, ya existe: {}", e.getMessage());
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
        } catch (Exception e) {
            logger.error("Error al crear la provincia: {}", e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Error al crear la provincia.");
        }
    }

    /**
     * Actualiza una provincia existente.
     * @param id ID de la provincia a actualizar.
     * @param provinceCreateDTO DTO con los nuevos datos de la provincia.
     * @param locale Idioma de los mensajes de error.
     * @return ResponseEntity con la provincia actualizada o un mensaje de error.
     */
    @PutMapping("/{id}")
    public ResponseEntity<?> updateProvince(@PathVariable Long id, @Valid @RequestBody ProvinceCreateDTO provinceCreateDTO, Locale locale) {
        logger.info("Actualizando provincia con ID {}", id);
        try {
            ProvinceDTO updatedProvince = provinceService.updateProvince(id, provinceCreateDTO, locale);
            return ResponseEntity.ok(updatedProvince);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
        } catch (Exception e) {
            logger.error("Error al actualizar la provincia con ID {}: {}", id, e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Error al actualizar la provincia.");
        }
    }

    /**
     * Elimina una provincia por su ID.
     * @param id ID de la provincia a eliminar.
     * @return ResponseEntity indicando el resultado de la operación.
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteProvince(@PathVariable Long id) {
        logger.info("Eliminando provincia con ID {}", id);
        try {
            provinceService.deleteProvince(id);
            logger.info("Provincia con ID {} eliminada exitosamente.", id);
            return ResponseEntity.noContent().build();
        } catch (IllegalArgumentException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
        } catch (Exception e) {
            logger.error("Error al eliminar la provincia con ID {}: {}", id, e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Error al eliminar la provincia.");
        }
    }
}
