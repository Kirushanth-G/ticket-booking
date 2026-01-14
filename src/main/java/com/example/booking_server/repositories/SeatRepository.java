package com.example.booking_server.repositories;

import com.example.booking_server.entities.Seat;
import jakarta.persistence.LockModeType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Lock;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Optional;

public interface SeatRepository extends JpaRepository<Seat, Long> {
    // 1. Standard find (No lock)
    Optional<Seat> findByTicketTypeTicketTypeIdAndSeatNo(Long ticketTypeId, String seatNo);

    // 2. 🔒 PESSIMISTIC LOCK METHOD
    // "SELECT ... FOR UPDATE" in SQL
    @Lock(LockModeType.PESSIMISTIC_WRITE)
    @Query("SELECT s FROM Seat s WHERE s.ticketType.ticketTypeId = :ticketTypeId AND s.seatNo = :seatNo")
    Optional<Seat> findByTicketTypeIdAndSeatNoWithLock(@Param("ticketTypeId") Long ticketTypeId,
                                                       @Param("seatNo") String seatNo);
}
