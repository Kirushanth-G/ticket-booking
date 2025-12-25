package com.example.booking_server.controllers;

import com.example.booking_server.dtos.TicketTypeRequest;
import com.example.booking_server.mappers.TicketTypeMapper;
import com.example.booking_server.repositories.EventRepository;
import com.example.booking_server.repositories.TicketTypeRepository;
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
@RequestMapping("/ticket-types")
public class TicketTypeController {
    private final EventRepository eventRepository;
    private final TicketTypeMapper ticketTypeMapper;
    private final TicketTypeRepository ticketTypeRepository;

    @PostMapping
    public ResponseEntity<?> createTicketType(
            @Valid @RequestBody TicketTypeRequest request,
            UriComponentsBuilder uriBuilder) {
        var event = eventRepository.findById(request.getEventId()).orElse(null);
        if (event == null){
            return ResponseEntity.notFound().build();
        }

        if (ticketTypeRepository.existsByEventAndTypeName(event, request.getTypeName())) {
            return ResponseEntity.badRequest().body(
                    Map.of("error", "Ticket type with this name already exists for this event")
            );
        }

        var ticketType = ticketTypeMapper.toEntity(request);
        ticketType.setEvent(event);
        ticketTypeRepository.save(ticketType);

        var dto = ticketTypeMapper.toDto(ticketType);
        var uri = uriBuilder.path("/ticket-types/{id}").buildAndExpand(dto.getTicketTypeId()).toUri();
        return ResponseEntity.created(uri).body(dto);
    }
}