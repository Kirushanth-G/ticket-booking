package com.example.booking_server.controllers;

import com.example.booking_server.dtos.TicketDto;
import com.example.booking_server.dtos.TicketTypeRequest;
import com.example.booking_server.services.TicketTypeService;
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
@RequestMapping("/ticket-types")
public class TicketTypeController {
    private final TicketTypeService ticketTypeService;

    @PostMapping
    public ResponseEntity<TicketDto> createTicketType(
            @Valid @RequestBody TicketTypeRequest request,
            UriComponentsBuilder uriBuilder) {
        TicketDto dto = ticketTypeService.createTicketType(request);
        var uri = uriBuilder.path("/ticket-types/{id}").buildAndExpand(dto.getTicketTypeId()).toUri();
        return ResponseEntity.created(uri).body(dto);
    }
}