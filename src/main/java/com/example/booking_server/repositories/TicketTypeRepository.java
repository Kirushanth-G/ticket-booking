package com.example.booking_server.repositories;

import com.example.booking_server.entities.TicketType;
import org.springframework.data.jpa.repository.JpaRepository;

public interface TicketTypeRepository extends JpaRepository<TicketType, Long> {
}
