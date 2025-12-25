package com.example.booking_server.repositories;

import com.example.booking_server.entities.Event;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDateTime;

public interface EventRepository extends JpaRepository<Event, Long> {
    boolean existsByEventNameAndEventDate(String eventName, LocalDateTime eventDate);
}
