package org.iesalixar.daw2.NataliaFernandez.dwese_ticket_logger_api.controllers;

import jakarta.validation.Valid;
import org.iesalixar.daw2.NataliaFernandez.dwese_ticket_logger_api.entities.Product;
import org.iesalixar.daw2.NataliaFernandez.dwese_ticket_logger_api.entities.Ticket;
import org.iesalixar.daw2.NataliaFernandez.dwese_ticket_logger_api.repositories.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.util.List;
import java.util.Locale;
import java.util.Optional;

@RestController
@RequestMapping("/api/v1/ticket-logger/tickets")
public class TicketProductController {

    private static final Logger logger = LoggerFactory.getLogger(TicketProductController.class);

    @Autowired
    private TicketRepository ticketRepository;

    @Autowired
    private ProductRepository productRepository;

    /**
     * Obtiene todos los tickets.
     *
     * @return Lista de tickets.
     */
    @GetMapping
    public ResponseEntity<List<Ticket>> getAllTickets() {
        try {
            List<Ticket> tickets = ticketRepository.findAll();
            return ResponseEntity.ok(tickets);
        } catch (Exception e) {
            logger.error("Error al listar los tickets: {}", e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    /**
     * Obtiene un ticket por ID.
     *
     * @param id ID del ticket.
     * @return Detalles del ticket.
     */
    @GetMapping("/{id}")
    public ResponseEntity<Ticket> getTicketById(@PathVariable Long id) {
        Optional<Ticket> ticket = ticketRepository.findById(id);
        return ticket.map(ResponseEntity::ok)
                .orElse(ResponseEntity.status(HttpStatus.NOT_FOUND).build());
    }

    /**
     * Crea un nuevo ticket.
     *
     * @param ticket Ticket a crear.
     * @return Ticket creado.
     */
    @PostMapping
    public ResponseEntity<Ticket> createTicket(@Valid @RequestBody Ticket ticket) {
        try {
            Ticket savedTicket = ticketRepository.save(ticket);
            return ResponseEntity.status(HttpStatus.CREATED).body(savedTicket);
        } catch (Exception e) {
            logger.error("Error al crear el ticket: {}", e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    /**
     * Actualiza un ticket existente.
     *
     * @param id     ID del ticket.
     * @param ticket Datos actualizados.
     * @return Ticket actualizado.
     */
    @PutMapping("/{id}")
    public ResponseEntity<Ticket> updateTicket(@PathVariable Long id, @Valid @RequestBody Ticket ticket) {
        if (!ticketRepository.existsById(id)) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        }
        ticket.setId(id);
        try {
            Ticket updatedTicket = ticketRepository.save(ticket);
            return ResponseEntity.ok(updatedTicket);
        } catch (Exception e) {
            logger.error("Error al actualizar el ticket: {}", e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    /**
     * Elimina un ticket por ID.
     *
     * @param id ID del ticket.
     * @return Respuesta vacía.
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteTicket(@PathVariable Long id) {
        if (!ticketRepository.existsById(id)) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        }
        try {
            ticketRepository.deleteById(id);
            return ResponseEntity.noContent().build();
        } catch (Exception e) {
            logger.error("Error al eliminar el ticket: {}", e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    /**
     * Añade un producto existente a un ticket.
     *
     * @param ticketId  ID del ticket.
     * @param productId ID del producto.
     * @return Ticket actualizado.
     */
    @PostMapping("/{ticketId}/products/{productId}")
    public ResponseEntity<Ticket> addProductToTicket(@PathVariable Long ticketId, @PathVariable Long productId) {
        Optional<Ticket> ticketOpt = ticketRepository.findById(ticketId);
        Optional<Product> productOpt = productRepository.findById(productId);

        if (ticketOpt.isEmpty() || productOpt.isEmpty()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        }

        Ticket ticket = ticketOpt.get();
        ticket.getProducts().add(productOpt.get());
        Ticket updatedTicket = ticketRepository.save(ticket);
        return ResponseEntity.ok(updatedTicket);
    }

    /**
     * Añade un nuevo producto a un ticket.
     *
     * @param ticketId     ID del ticket.
     * @param productName  Nombre del producto.
     * @param productPrice Precio del producto.
     * @return Ticket actualizado.
     */
    @PostMapping("/{ticketId}/products")
    public ResponseEntity<Ticket> addNewProductToTicket(
            @PathVariable Long ticketId,
            @RequestParam String productName,
            @RequestParam BigDecimal productPrice) {

        Optional<Ticket> ticketOpt = ticketRepository.findById(ticketId);
        if (ticketOpt.isEmpty()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        }

        Ticket ticket = ticketOpt.get();
        Product newProduct = new Product();
        newProduct.setName(productName);
        newProduct.setPrice(productPrice);
        productRepository.save(newProduct);

        ticket.getProducts().add(newProduct);
        Ticket updatedTicket = ticketRepository.save(ticket);
        return ResponseEntity.ok(updatedTicket);
    }

    /**
     * Elimina un producto de un ticket.
     *
     * @param ticketId  ID del ticket.
     * @param productId ID del producto.
     * @return Ticket actualizado.
     */
    @DeleteMapping("/{ticketId}/products/{productId}")
    public ResponseEntity<Ticket> removeProductFromTicket(@PathVariable Long ticketId, @PathVariable Long productId) {
        Optional<Ticket> ticketOpt = ticketRepository.findById(ticketId);
        Optional<Product> productOpt = productRepository.findById(productId);

        if (ticketOpt.isEmpty() || productOpt.isEmpty()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        }

        Ticket ticket = ticketOpt.get();
        ticket.getProducts().remove(productOpt.get());
        Ticket updatedTicket = ticketRepository.save(ticket);
        return ResponseEntity.ok(updatedTicket);
    }
}
