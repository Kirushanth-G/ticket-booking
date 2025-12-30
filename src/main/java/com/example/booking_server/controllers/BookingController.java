package com.example.booking_server.controllers;

import com.example.booking_server.dtos.BookingRequest;
import com.example.booking_server.entities.Booking;
import com.example.booking_server.mappers.BookingMapper;
import com.example.booking_server.repositories.BookingRepository;
import com.example.booking_server.repositories.TicketTypeRepository;
import jakarta.transaction.Transactional;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.util.UriComponentsBuilder;

import java.util.Map;

@RestController
@AllArgsConstructor
@RequestMapping("/bookings")
public class BookingController {

    private final TicketTypeRepository ticketTypeRepository;
    private final BookingRepository bookingRepository;
    private final BookingMapper bookingMapper;

    @PostMapping
    @Transactional
    public ResponseEntity<?> bookTicket(
            @Valid @RequestBody BookingRequest request,
            UriComponentsBuilder uriBuilder) {

        var ticketType = ticketTypeRepository.findById(request.getTicketTypeId()).orElse(null);
        if (ticketType == null) {
            return ResponseEntity.badRequest().body(Map.of("ticketType", "Ticket type not found."));
        }

        // Check if user already has a booking for this ticket type
        var existingBooking = bookingRepository.findByUserIdAndTicketType_TicketTypeId(
                request.getUserId(), request.getTicketTypeId());

        if (existingBooking.isPresent()) {
            // Update existing booking
            Booking booking = existingBooking.get();
            int totalSeats = booking.getSeatsBooked() + request.getSeatsBooked();
            booking.setSeatsBooked(totalSeats);

            // Update ticket quantity
            int newQuantity = ticketType.getQuantity() - request.getSeatsBooked();
            ticketType.setQuantity(newQuantity);

            ticketTypeRepository.save(ticketType);
            bookingRepository.save(booking);

            var dto = bookingMapper.toDto(booking);
            return ResponseEntity.ok(dto);
        } else {
            // Create new booking
            int newQuantity = ticketType.getQuantity() - request.getSeatsBooked();
            ticketType.setQuantity(newQuantity);
            ticketTypeRepository.save(ticketType);

            var booking = bookingMapper.toEntity(request);
            booking.setTicketType(ticketType);
            bookingRepository.save(booking);

            var dto = bookingMapper.toDto(booking);
            var uri = uriBuilder.path("/bookings/{id}").buildAndExpand(booking.getBookingId()).toUri();
            return ResponseEntity.created(uri).body(dto);
        }
    }
}
