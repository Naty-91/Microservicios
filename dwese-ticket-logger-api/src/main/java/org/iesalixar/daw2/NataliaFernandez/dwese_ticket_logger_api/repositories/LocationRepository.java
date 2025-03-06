package org.iesalixar.daw2.NataliaFernandez.dwese_ticket_logger_api.repositories;

import jakarta.validation.constraints.NotEmpty;
import org.iesalixar.daw2.NataliaFernandez.dwese_ticket_logger_api.entities.Location;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface LocationRepository extends JpaRepository<Location, Long> {

    List<Location> findAll();

    void deleteById(Long id);

    Optional<Location> findById(Long id);

    boolean existsLocationByAddress(String address); // Verifica si una ubicación con la dirección existe


    @Query("SELECT CASE WHEN COUNT(l) > 0 THEN TRUE ELSE FALSE END " +
            "FROM Location l WHERE l.address = :address AND l.id != :id")
    boolean existsLocationByAddressAndNotId(@Param("address") String address, @Param("id") Long id);

    boolean existsByAddress(@NotEmpty(message = "{msg.location.address.notEmpty}") String address);
}