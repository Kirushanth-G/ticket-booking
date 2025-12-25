package com.example.booking_server.dtos;

import lombok.Data;

import java.time.LocalDateTime;

@Data
public class EventDto {
    private Long eventId;
    private String eventName;
    private LocalDateTime eventDate;
    private String venue;
    private String status;
}
