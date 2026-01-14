package com.example.booking_server.controllers;

import com.example.booking_server.dtos.BookingDto;
import com.example.booking_server.dtos.BookingRequest;
import com.example.booking_server.services.BookingService;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.util.UriComponentsBuilder;


@RestController
@AllArgsConstructor
@RequestMapping("/bookings")
public class BookingController {
    private final BookingService bookingService;

    @PostMapping
    public ResponseEntity<?> bookTicket(
            @Valid @RequestBody BookingRequest request,
            UriComponentsBuilder uriBuilder) {
        BookingDto dto = bookingService.bookTicket(request);
        var uri = uriBuilder.path("/bookings/{id}").buildAndExpand(dto.getBookingId()).toUri();
        return ResponseEntity.created(uri).body(dto);
    }

    @PostMapping("/optimistic")
    public ResponseEntity<?> bookTicketOptimistic(
            @Valid @RequestBody BookingRequest request,
            UriComponentsBuilder uriBuilder) {
        BookingDto dto = bookingService.bookTicketOptimistic(request);
        var uri = uriBuilder.path("/bookings/{id}").buildAndExpand(dto.getBookingId()).toUri();
        return ResponseEntity.created(uri).body(dto);
    }
}
