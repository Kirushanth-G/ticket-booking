package com.example.booking_server.repositories;

import com.example.booking_server.entities.Event;
import com.example.booking_server.entities.TicketType;
import jakarta.persistence.LockModeType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Lock;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Optional;

public interface TicketTypeRepository extends JpaRepository<TicketType, Long> {
    boolean existsByEventAndTypeName(Event event, String typeName);
}
