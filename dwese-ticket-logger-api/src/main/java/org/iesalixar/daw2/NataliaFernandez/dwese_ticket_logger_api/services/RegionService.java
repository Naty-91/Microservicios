package org.iesalixar.daw2.NataliaFernandez.dwese_ticket_logger_api.services;
import org.iesalixar.daw2.NataliaFernandez.dwese_ticket_logger_api.entities.Region;
import org.iesalixar.daw2.NataliaFernandez.dwese_ticket_logger_api.mappers.RegionMapper;
import org.iesalixar.daw2.NataliaFernandez.dwese_ticket_logger_api.repositories.RegionRepository;
import org.iesalixar.daw2.NataliaFernandez.dwese_ticket_logger_api.dtos.RegionCreateDTO;
import org.iesalixar.daw2.NataliaFernandez.dwese_ticket_logger_api.dtos.RegionDTO;
import org.slf4j.LoggerFactory;
import org.slf4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.Locale;
import java.util.Optional;


@Service
public class RegionService {

    private static final Logger logger = LoggerFactory.getLogger(RegionService.class);

    @Autowired
    private RegionRepository regionRepository;

    @Autowired
    private RegionMapper regionMapper;

    @Autowired
    private MessageSource messageSource;

    /**
     * Lista todas las regiones almacenadas en la base de datos.
     *
     * @return Lista de RegionDTO.
     */
    public Page<RegionDTO> getAllRegions(Pageable pageable) {
        logger.info("Solicitando todas las regiones con paginación: página {}, tamaño {}",
                pageable.getPageNumber(), pageable.getPageSize());
        try {
            Page<Region> regions = regionRepository.findAll(pageable);
            logger.info("Se han encontrado {} regiones en la página actual.", regions.getNumberOfElements());
            return regions.map(regionMapper::toDTO);
        } catch (Exception e) {
            logger.info("Error al obtener la lista paginada de regiones: {}", e.getMessage());
            throw e;
        }
    }

    /**
     * Obtiene una región específica por su ID.
     *
     * @param id ID de la región solicitada.
     * @return Optional con el DTO de la región encontrada o vacío si no existe.
     */
    public Optional<RegionDTO> getRegionById(Long id) {
        try {
            logger.info("Buscando región con ID {}", id);
            return regionRepository.findById(id)
                    .map(regionMapper::toDTO);
        } catch (Exception e) {
            logger.error("Error al buscar la región con ID {}: {}", id, e.getMessage());
            throw new RuntimeException("Error al buscar la región", e);
        }
    }

    /**
     * Crea una nueva región en la base de datos.
     *
     * @param regionCreateDTO DTO que contiene los datos de la región a crear.
     * @param locale          Idioma para los mensajes de error.
     * @return DTO de la región creada.
     */
    public RegionDTO createRegion(RegionCreateDTO regionCreateDTO, Locale locale) {


        if (regionRepository.existsByCode(regionCreateDTO.getCode())) {
            String errorMessage = messageSource.getMessage("msg.region-controller.insert.codeExist", null, locale);
            logger.warn("Error al crear la región: el código {} ya existe", regionCreateDTO.getCode());
            throw new IllegalArgumentException(errorMessage);
        }

        Region region = regionMapper.toEntity(regionCreateDTO);
        Region savedRegion = regionRepository.save(region);

        logger.info("Región creada exitosamente con ID {}", savedRegion.getId());
        return regionMapper.toDTO(savedRegion);

    }

    /**
     * Actualiza una región existente.
     *
     * @param id              Identificador de la región a actualizar.
     * @param regionCreateDTO DTO que contiene los nuevos datos de la región.
     * @param locale          Idioma para los mensajes de error.
     * @return DTO de la región actualizada.
     */
    public RegionDTO updateRegion(Long id, RegionCreateDTO regionCreateDTO, Locale locale) {
        Region existingRegion = regionRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("La región no existe."));

        if (regionRepository.existsRegionByCodeAndNotId(regionCreateDTO.getCode(), id)) {
            String errorMessage = messageSource.getMessage("msg.region-coontroller.update.codeExist", null, locale);
        }

        existingRegion.setCode(regionCreateDTO.getCode());
        existingRegion.setCode(regionCreateDTO.getName());
        Region updatedRegion = regionRepository.save(existingRegion);
        return regionMapper.toDTO(updatedRegion);
    }

    /**
     * Elimina una región específica por su ID.
     *
     * @param id Identificador único de la región.
     * @return
     */
    public void deleteRegion(Long id) {

        if (!regionRepository.existsById(id)) {
            throw new IllegalArgumentException("La región no existe");
        }
        regionRepository.deleteById(id);
    }
}