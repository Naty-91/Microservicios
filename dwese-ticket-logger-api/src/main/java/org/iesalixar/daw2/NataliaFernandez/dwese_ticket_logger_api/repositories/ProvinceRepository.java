package org.iesalixar.daw2.NataliaFernandez.dwese_ticket_logger_api.repositories;

import org.iesalixar.daw2.NataliaFernandez.dwese_ticket_logger_api.entities.Province;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.Optional;

public interface ProvinceRepository extends JpaRepository<Province, Long> {

    List<Province> findAll();

    void deleteById(Long id);

    Optional<Province> findById(Long id);

    boolean existsProvinceByCode(String code);

    @Query("SELECT CASE WHEN COUNT(p) > 0 THEN TRUE ELSE FALSE END " +
            "FROM Province p WHERE p.code = :code AND p.id <> :id")
    boolean existsProvinceByCodeAndNotId(String code, Long id);
}
