package com.example.booking_server.dtos;
import lombok.Data;

@Data
public class BookingRequest{
    private Long userId;
    private Long ticketTypeId;
    private Integer seatsBooked;
}
