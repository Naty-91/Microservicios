package org.iesalixar.daw2.NataliaFernandez.dwese_ticket_logger_api.services;

import org.iesalixar.daw2.NataliaFernandez.dwese_ticket_logger_api.entities.Province;
import org.iesalixar.daw2.NataliaFernandez.dwese_ticket_logger_api.entities.Region;
import org.iesalixar.daw2.NataliaFernandez.dwese_ticket_logger_api.mappers.ProvinceMapper;
import org.iesalixar.daw2.NataliaFernandez.dwese_ticket_logger_api.repositories.ProvinceRepository;
import org.iesalixar.daw2.NataliaFernandez.dwese_ticket_logger_api.dtos.ProvinceCreateDTO;
import org.iesalixar.daw2.NataliaFernandez.dwese_ticket_logger_api.dtos.ProvinceDTO;
import org.iesalixar.daw2.NataliaFernandez.dwese_ticket_logger_api.repositories.RegionRepository;
import org.slf4j.LoggerFactory;
import org.slf4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Locale;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class ProvinceService {

    private static final Logger logger = LoggerFactory.getLogger(ProvinceService.class);

    @Autowired
    private ProvinceRepository provinceRepository;

    @Autowired
    private RegionRepository regionRepository;

    @Autowired
    private ProvinceMapper provinceMapper;

    @Autowired
    private MessageSource messageSource;

    /**
     * Lista todas las provincias almacenadas en la base de datos.
     *
     * @return Lista de ProvinceDTO.
     */
    public List<ProvinceDTO> getAllProvinces() {
        try {
            logger.info("Solicitando la lista de todas las provincias...");
            List<Province> provinces = provinceRepository.findAll();
            logger.info("Se han encontrado {} provincias.", provinces.size());

            return provinces.stream()
                    .map(provinceMapper::toDTO)
                    .collect(Collectors.toList());
        } catch (Exception e) {
            logger.error("Error al listar las provincias: {}", e.getMessage());
            throw new RuntimeException("Error al obtener todas las provincias", e);
        }
    }

    /**
     * Obtiene una provincia específica por su ID.
     *
     * @param id ID de la provincia solicitada.
     * @return Optional con el DTO de la provincia encontrada o vacío si no existe.
     */
    public Optional<ProvinceDTO> getProvinceById(Long id) {
        try {
            logger.info("Buscando provincia con ID {}", id);
            return provinceRepository.findById(id)
                    .map(provinceMapper::toDTO);
        } catch (Exception e) {
            logger.error("Error al buscar la provincia con ID {}: {}", id, e.getMessage());
            throw new RuntimeException("Error al buscar la provincia", e);
        }
    }

    /**
     * Crea una nueva provincia en la base de datos.
     *
     * @param provinceCreateDTO DTO que contiene los datos de la provincia a crear.
     * @param locale            Idioma para los mensajes de error.
     * @return DTO de la provincia creada.
     */
    public ProvinceDTO createProvince(ProvinceCreateDTO provinceCreateDTO, Locale locale) {
        // Verifica si ya existe una provincia con el mismo código.
        if (provinceRepository.existsProvinceByCode(provinceCreateDTO.getCode())) {
            String errorMessage = messageSource.getMessage("msg.province-controller.insert.codeExist", null, locale);
            logger.warn("Error al crear la provincia: el código {} ya existe", provinceCreateDTO.getCode());
            throw new IllegalArgumentException(errorMessage);
        }

        // Obtiene la región asociada a la provincia.
        Region region = regionRepository.findById(provinceCreateDTO.getRegionId())
                .orElseThrow(() -> new IllegalArgumentException("La región no existe"));

        // Mapea el DTO a la entidad y la guarda.
        Province province = provinceMapper.toEntity(provinceCreateDTO, region);
        Province savedProvince = provinceRepository.save(province);

        logger.info("Provincia creada exitosamente con ID {}", savedProvince.getId());
        return provinceMapper.toDTO(savedProvince);
    }

    /**
     * Actualiza una provincia existente.
     *
     * @param id                Identificador de la provincia a actualizar.
     * @param provinceCreateDTO DTO que contiene los nuevos datos de la provincia.
     * @param locale            Idioma para los mensajes de error.
     * @return DTO de la provincia actualizada.
     */
    public ProvinceDTO updateProvince(Long id, ProvinceCreateDTO provinceCreateDTO, Locale locale) {
        // Obtiene la provincia existente.
        Province existingProvince = provinceRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("La provincia no existe"));

        // Verifica si ya existe una provincia con el mismo código, pero diferente ID.
        if (provinceRepository.existsProvinceByCodeAndNotId(provinceCreateDTO.getCode(), id)) {
            String errorMessage = messageSource.getMessage("msg.province-controller.update.codeExist", null, locale);
            logger.warn("Error al actualizar la provincia: el código {} ya existe", provinceCreateDTO.getCode());
            throw new IllegalArgumentException(errorMessage);
        }

        // Actualiza la provincia con los nuevos datos.
        Region region = regionRepository.findById(provinceCreateDTO.getRegionId())
                .orElseThrow(() -> new IllegalArgumentException("La región no existe"));

        existingProvince.setCode(provinceCreateDTO.getCode());
        existingProvince.setName(provinceCreateDTO.getName());
        existingProvince.setRegion(region);

        Province updatedProvince = provinceRepository.save(existingProvince);
        return provinceMapper.toDTO(updatedProvince);
    }

    /**
     * Elimina una provincia específica por su ID.
     *
     * @param id Identificador único de la provincia.
     */
    public void deleteProvince(Long id) {
        // Verifica si la provincia existe antes de eliminarla.
        if (!provinceRepository.existsById(id)) {
            throw new IllegalArgumentException("La provincia no existe");
        }
        provinceRepository.deleteById(id);
        logger.info("Provincia con ID {} eliminada exitosamente.", id);
    }
}

