package com.example.booking_server.dtos;

import lombok.Data;

import java.math.BigDecimal;
import java.util.List;

@Data
public class BookingDto {
    private Long bookingId;
    private Long userId;
    private BigDecimal totalAmount;
    private String bookedAt;

    private String eventName;
    private List<String> seatNumbers;
}
