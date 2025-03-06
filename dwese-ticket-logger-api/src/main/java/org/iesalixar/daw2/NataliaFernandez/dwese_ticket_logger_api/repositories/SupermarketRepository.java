package org.iesalixar.daw2.NataliaFernandez.dwese_ticket_logger_api.repositories;

import org.iesalixar.daw2.NataliaFernandez.dwese_ticket_logger_api.entities.Supermarket;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.Optional;

public interface SupermarketRepository extends JpaRepository<Supermarket, Long> {

    List<Supermarket> findAll();

    void deleteById(Long id);

    Optional<Supermarket> findById(Long id);

    boolean existsSupermarketByName(String name);

    @Query("SELECT CASE WHEN COUNT(s) > 0 THEN TRUE ELSE FALSE END " +
            "FROM Supermarket s WHERE s.name = :name AND s.id <> :id")
    boolean existsSupermarketByNameAndNotId(String name, Long id);
}
