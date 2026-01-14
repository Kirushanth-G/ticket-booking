package com.example.booking_server.controllers;

import com.example.booking_server.dtos.EventDto;
import com.example.booking_server.dtos.EventRequest;
import com.example.booking_server.services.EventService;
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
@RequestMapping("/events")
public class EventController {
    private EventService eventService;

    @PostMapping
    public ResponseEntity<?> createEvent(
            @Valid @RequestBody EventRequest request,
            UriComponentsBuilder uriBuilder){
        EventDto dto = eventService.createEvent(request);
        var uri = uriBuilder.path("/events/{id}").buildAndExpand(dto.getEventId()).toUri();
        return ResponseEntity.created(uri).body(dto);
    }
}