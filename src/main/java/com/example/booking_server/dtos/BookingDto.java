package com.example.booking_server.dtos;

import lombok.Data;

@Data
public class BookingDto {
    private Long bookingId;
    private Long userId;
    private Long ticketTypeId;
    private Integer seatsBooked;
    private String bookedAt;
}
