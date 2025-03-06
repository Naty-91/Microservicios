package org.iesalixar.daw2.NataliaFernandez.dwese_ticket_logger_api.services;

import org.iesalixar.daw2.NataliaFernandez.dwese_ticket_logger_api.entities.Supermarket;
import org.iesalixar.daw2.NataliaFernandez.dwese_ticket_logger_api.mappers.SupermarketMapper;
import org.iesalixar.daw2.NataliaFernandez.dwese_ticket_logger_api.repositories.SupermarketRepository;
import org.iesalixar.daw2.NataliaFernandez.dwese_ticket_logger_api.dtos.SupermarketCreateDTO;
import org.iesalixar.daw2.NataliaFernandez.dwese_ticket_logger_api.dtos.SupermarketDTO;
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
public class SupermarketService {

    private static final Logger logger = LoggerFactory.getLogger(SupermarketService.class);

    @Autowired
    private SupermarketRepository supermarketRepository;

    @Autowired
    private SupermarketMapper supermarketMapper;

    @Autowired
    private MessageSource messageSource;

    /**
     * Lista todos los supermercados almacenados en la base de datos.
     *
     * @return Lista de SupermarketDTO.
     */
    public List<SupermarketDTO> getAllSupermarkets() {
        try {
            logger.info("Solicitando la lista de todos los supermercados...");
            List<Supermarket> supermarkets = supermarketRepository.findAll();
            logger.info("Se han encontrado {} supermercados.", supermarkets.size());

            return supermarkets.stream()
                    .map(supermarketMapper::toDTO)
                    .collect(Collectors.toList());
        } catch (Exception e) {
            logger.error("Error al listar los supermercados: {}", e.getMessage());
            throw new RuntimeException("Error al obtener todos los supermercados", e);
        }
    }

    /**
     * Obtiene un supermercado específico por su ID.
     *
     * @param id ID del supermercado solicitado.
     * @return Optional con el DTO del supermercado encontrado o vacío si no existe.
     */
    public Optional<SupermarketDTO> getSupermarketById(Long id) {
        try {
            logger.info("Buscando supermercado con ID {}", id);
            return supermarketRepository.findById(id)
                    .map(supermarketMapper::toDTO);
        } catch (Exception e) {
            logger.error("Error al buscar el supermercado con ID {}: {}", id, e.getMessage());
            throw new RuntimeException("Error al buscar el supermercado", e);
        }
    }

    /**
     * Crea un nuevo supermercado en la base de datos.
     *
     * @param supermarketCreateDTO DTO que contiene los datos del supermercado a crear.
     * @param locale               Idioma para los mensajes de error.
     * @return DTO del supermercado creado.
     */
    public SupermarketDTO createSupermarket(SupermarketCreateDTO supermarketCreateDTO, Locale locale) {
        try {
            // Comprobamos si ya existe un supermercado con el mismo nombre
            if (supermarketRepository.existsSupermarketByName(supermarketCreateDTO.getName())) {
                // Si el supermercado ya existe, se lanza una excepción con el mensaje adecuado
                String errorMessage = messageSource.getMessage("msg.supermarket-controller.insert.nameExist", null, locale);
                logger.warn("Error al crear el supermercado: el nombre {} ya existe", supermarketCreateDTO.getName());
                throw new IllegalArgumentException(errorMessage);
            }

            // Si estás creando un supermercado (usando SupermarketCreateDTO)
            Supermarket supermarket = supermarketMapper.toEntity(supermarketCreateDTO);

            // Guardamos la nueva entidad en la base de datos
            Supermarket savedSupermarket = supermarketRepository.save(supermarket);





            // Registramos la creación exitosa
            logger.info("Supermercado creado exitosamente con ID {}", savedSupermarket.getId());

            // Devolvemos el DTO correspondiente al supermercado creado
            return supermarketMapper.toDTO(savedSupermarket);
        } catch (Exception e) {
            logger.error("Error al crear el supermercado: {}", e.getMessage());
            throw new RuntimeException("Error al crear el supermercado", e);
        }
    }


    /**
     * Actualiza un supermercado existente.
     *
     * @param id                   Identificador del supermercado a actualizar.
     * @param supermarketCreateDTO DTO que contiene los nuevos datos del supermercado.
     * @param locale               Idioma para los mensajes de error.
     * @return DTO del supermercado actualizado.
     */
    public SupermarketDTO updateSupermarket(Long id, SupermarketCreateDTO supermarketCreateDTO, Locale locale) {
        Supermarket existingSupermarket = supermarketRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("El supermercado no existe."));

        if (supermarketRepository.existsSupermarketByNameAndNotId(supermarketCreateDTO.getName(), id)) {
            String errorMessage = messageSource.getMessage("msg.supermarket-controller.update.nameExist", null, locale);
            logger.warn("Error al actualizar el supermercado: el nombre {} ya está en uso por otro supermercado.", supermarketCreateDTO.getName());
            throw new IllegalArgumentException(errorMessage);
        }

        existingSupermarket.setName(supermarketCreateDTO.getName());
        // Aquí también puedes actualizar otros campos de la entidad, como la dirección si es necesario.
        Supermarket updatedSupermarket = supermarketRepository.save(existingSupermarket);

        logger.info("Supermercado con ID {} actualizado exitosamente.", id);
        return supermarketMapper.toDTO(updatedSupermarket);
    }

    /**
     * Elimina un supermercado específico por su ID.
     *
     * @param id Identificador único del supermercado.
     */
    public void deleteSupermarket(Long id) {
        if (!supermarketRepository.existsById(id)) {
            logger.warn("Error al eliminar el supermercado: el supermercado con ID {} no existe.", id);
            throw new IllegalArgumentException("El supermercado no existe.");
        }
        supermarketRepository.deleteById(id);
        logger.info("Supermercado con ID {} eliminado exitosamente.", id);
    }
}
