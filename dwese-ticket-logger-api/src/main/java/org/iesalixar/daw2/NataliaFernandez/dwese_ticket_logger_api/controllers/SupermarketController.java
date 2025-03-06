package org.iesalixar.daw2.NataliaFernandez.dwese_ticket_logger_api.controllers;
import jakarta.validation.Valid;
import org.iesalixar.daw2.NataliaFernandez.dwese_ticket_logger_api.dtos.SupermarketCreateDTO;
import org.iesalixar.daw2.NataliaFernandez.dwese_ticket_logger_api.dtos.SupermarketDTO;
import org.iesalixar.daw2.NataliaFernandez.dwese_ticket_logger_api.services.SupermarketService;
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
 * Controlador que maneja las operaciones CRUD para la entidad `Supermarket`.
 */
@RestController
@RequestMapping("/api/v1/ticket-logger/supermarkets")
public class SupermarketController {

    private static final Logger logger = LoggerFactory.getLogger(SupermarketController.class);

    @Autowired
    private SupermarketService supermarketService;

    @Autowired
    private MessageSource messageSource;

    /**
     * Lista todos los supermercados.
     * @return ResponseEntity con la lista de supermercados.
     */
    @GetMapping
    public ResponseEntity<List<SupermarketDTO>> getAllSupermarkets() {
        logger.info("Solicitando la lista de todos los supermercados...");
        try {
            List<SupermarketDTO> supermarketDTOs = supermarketService.getAllSupermarkets();
            logger.info("Se han cargado {} supermercados.", supermarketDTOs.size());
            return ResponseEntity.ok(supermarketDTOs);
        } catch (Exception e) {
            logger.error("Error al listar los supermercados: {}", e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(null);
        }
    }

    /**
     * Obtiene un supermercado por su ID.
     * @param id ID del supermercado solicitado.
     * @return ResponseEntity con el supermercado encontrado o un mensaje de error.
     */
    @GetMapping("/{id}")
    public ResponseEntity<?> getSupermarketById(@PathVariable Long id) {
        logger.info("Buscando supermercado con ID {}", id);
        try {
            Optional<SupermarketDTO> supermarketDTO = supermarketService.getSupermarketById(id);
            if (supermarketDTO.isPresent()) {
                logger.info("Supermercado con ID {} encontrado.", id);
                return ResponseEntity.ok(supermarketDTO.get());
            } else {
                logger.warn("No se encontró ningún supermercado con ID {}", id);
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body("El supermercado no existe.");
            }
        } catch (Exception e) {
            logger.error("Error al buscar el supermercado con ID {}: {}", id, e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Error al buscar el supermercado.");
        }
    }

    /**
     * Crea un nuevo supermercado.
     * @param supermarketCreateDTO DTO con los datos del supermercado a crear.
     * @param locale Idioma de los mensajes de error.
     * @return ResponseEntity con el supermercado creado o un mensaje de error.
     */
    @PostMapping
    public ResponseEntity<?> createSupermarket(@Valid @RequestBody SupermarketCreateDTO supermarketCreateDTO, Locale locale) {
        try {
            // Intentamos crear el supermercado usando el servicio
            SupermarketDTO createdSupermarket = supermarketService.createSupermarket(supermarketCreateDTO, locale);
            return ResponseEntity.status(HttpStatus.CREATED).body(createdSupermarket);
        } catch (IllegalArgumentException e) {
            // En caso de error por conflicto (por ejemplo, supermercado ya existe)
            logger.error("Error al crear el supermercado, ya existe: {}", e.getMessage());
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
        } catch (Exception e) {
            // En caso de otros errores inesperados
            logger.error("Error al crear el supermercado: {}", e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Error al crear el supermercado.");
        }
    }


    /**
     * Actualiza un supermercado existente.
     * @param id ID del supermercado a actualizar.
     * @param supermarketCreateDTO DTO con los nuevos datos del supermercado.
     * @param locale Idioma de los mensajes de error.
     * @return ResponseEntity con el supermercado actualizado o un mensaje de error.
     */
    @PutMapping("/{id}")
    public ResponseEntity<?> updateSupermarket(@PathVariable Long id, @Valid @RequestBody SupermarketCreateDTO supermarketCreateDTO, Locale locale) {
        logger.info("Actualizando supermercado con ID {}", id);
        try {
            SupermarketDTO updatedSupermarket = supermarketService.updateSupermarket(id, supermarketCreateDTO, locale);
            return ResponseEntity.ok(updatedSupermarket);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
        } catch (Exception e) {
            logger.error("Error al actualizar el supermercado con ID {}: {}", id, e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Error al actualizar el supermercado.");
        }
    }

    /**
     * Elimina un supermercado por su ID.
     * @param id ID del supermercado a eliminar.
     * @return ResponseEntity indicando el resultado de la operación.
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteSupermarket(@PathVariable Long id) {
        logger.info("Eliminando supermercado con ID {}", id);
        try {
            supermarketService.deleteSupermarket(id);
            logger.info("Supermercado con ID {} eliminado exitosamente.", id);
            return ResponseEntity.noContent().build();
        } catch (IllegalArgumentException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
        } catch (Exception e) {
            logger.error("Error al eliminar el supermercado con ID {}: {}", id, e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Error al eliminar el supermercado.");
        }
    }
}
