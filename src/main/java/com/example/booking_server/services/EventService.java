package com.example.booking_server.services;

import com.example.booking_server.dtos.EventDto;
import com.example.booking_server.dtos.EventRequest;
import com.example.booking_server.entities.Event;
import com.example.booking_server.exceptions.EventAlreadyExistsException;
import com.example.booking_server.mappers.EventMapper;
import com.example.booking_server.repositories.EventRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@AllArgsConstructor
public class EventService {
    //constructor injection
    private final EventRepository eventRepository;
    private final EventMapper eventMapper;

    public EventDto createEvent(EventRequest request){
        if(eventRepository.existsByEventNameAndEventDate(request.getEventName(), request.getEventDate())){
            throw new EventAlreadyExistsException("An event with the same name and date already exists.");
        }
        Event event = eventMapper.toEntity(request);
        Event savedEvent = eventRepository.save(event);
        return eventMapper.toDto(savedEvent);
    }
}
