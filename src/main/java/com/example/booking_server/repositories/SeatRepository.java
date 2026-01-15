package com.example.booking_server.repositories;

import com.example.booking_server.entities.Seat;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface SeatRepository extends JpaRepository<Seat, Long> {
    // 1. Standard find (No lock)
    @Query(value = "SELECT * FROM seats WHERE ticket_type_id = :ticketTypeId AND status = 'AVAILABLE' LIMIT :limit", nativeQuery = true)
    List<Seat> findAvailableSeats(@Param("ticketTypeId") Long ticketTypeId, @Param("limit") int limit);

    // 2. 🔒 PESSIMISTIC LOCK METHOD
    // "FOR UPDATE SKIP LOCKED" is already in the SQL query
    @Query(value = "SELECT * FROM seats WHERE ticket_type_id = :ticketTypeId AND status = 'AVAILABLE' LIMIT :limit FOR UPDATE SKIP LOCKED",
           nativeQuery = true)
    List<Seat> findAvailableSeatsForUpdate(@Param("ticketTypeId") Long ticketTypeId,
                                           @Param("limit") int limit);

    // 3. OPTIMISTIC LOCK METHOD
    @Query(value = "SELECT * FROM seats WHERE ticket_type_id = :ticketTypeId AND status = 'AVAILABLE' LIMIT :limit", nativeQuery = true)
    List<Seat> findAvailableSeatsOptimistic(@Param("ticketTypeId") Long ticketTypeId,
                                           @Param("limit") int limit);
}
