package com.example.booking_server.repositories;

import com.example.booking_server.entities.Booking;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface BookingRepository extends JpaRepository<Booking, Long> {
//    Optional<Booking> findByUserIdAndTicketType_TicketTypeId(Long userId, Long ticketTypeId);
}
