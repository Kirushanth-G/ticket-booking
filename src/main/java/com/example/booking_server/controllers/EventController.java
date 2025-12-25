package com.example.booking_server.controllers;

import com.example.booking_server.dtos.EventRequest;
import com.example.booking_server.mappers.EventMapper;
import com.example.booking_server.repositories.EventRepository;
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
@RequestMapping("/events")
public class EventController {
    private final EventRepository eventRepository;
    private final EventMapper eventMapper;

    @PostMapping
    public ResponseEntity<?> createEvent(
            @Valid @RequestBody EventRequest request,
            UriComponentsBuilder uriBuilder){
        if(eventRepository.existsByEventNameAndEventDate(request.getEventName(), request.getEventDate())){
            return ResponseEntity.badRequest().body(Map.of("event", "An event with the same name and date already exists."));
        }
        var event = eventMapper.toEntity(request);
        eventRepository.save(event);

        var dto = eventMapper.toDto(event);
        var uri = uriBuilder.path("/events/{id}").buildAndExpand(dto.getEventId()).toUri();
        return ResponseEntity.created(uri).body(dto);
    }
}