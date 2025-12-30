package com.example.booking_server.dtos;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class BookingRequest{
    @NotNull(message = "User ID is required")
    private Long userId;

    @NotNull(message = "Ticket Type ID is required")
    private Long ticketTypeId;

    @NotNull(message = "Number of seats booked is required")
    @Min(value = 1, message = "Total seats must be at least 1")
    private Integer seatsBooked;
}
