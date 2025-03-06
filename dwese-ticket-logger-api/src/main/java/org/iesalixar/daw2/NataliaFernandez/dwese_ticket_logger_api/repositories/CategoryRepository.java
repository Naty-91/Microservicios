package org.iesalixar.daw2.NataliaFernandez.dwese_ticket_logger_api.repositories;

import jakarta.validation.constraints.NotEmpty;
import org.iesalixar.daw2.NataliaFernandez.dwese_ticket_logger_api.entities.Category;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface CategoryRepository extends JpaRepository<Category, Long> {

    List<Category> findAll();

    void deleteById(Long id);

    Optional<Category> findById(Long id);

    boolean existsByName(@NotEmpty(message = "{msg.category.name.notEmpty}") String name);

    @Query("SELECT CASE WHEN COUNT(c) > 0 THEN TRUE ELSE FALSE END " +
            "FROM Category c WHERE c.name = :name AND c.id != :id")
    boolean existsCategoryByNameAndNotId(@Param("name") String name, @Param("id") Long id);
}
