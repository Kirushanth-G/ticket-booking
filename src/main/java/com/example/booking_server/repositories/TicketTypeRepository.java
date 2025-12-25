package com.example.booking_server.repositories;

import com.example.booking_server.entities.Event;
import com.example.booking_server.entities.TicketType;
import jakarta.validation.constraints.NotBlank;
import org.springframework.data.jpa.repository.JpaRepository;

public interface TicketTypeRepository extends JpaRepository<TicketType, Long> {
    boolean existsByEventAndTypeName(Event event, String typeName);
}
