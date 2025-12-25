package com.example.booking_server.dtos;
import lombok.Data;

import java.math.BigDecimal;

@Data
public class TicketTypeRequest{
    private Long eventId;
    private String typeName;
    private BigDecimal price;
    private Integer quantity;
}
