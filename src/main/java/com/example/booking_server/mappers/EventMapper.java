package com.example.booking_server.mappers;

import com.example.booking_server.dtos.EventDto;
import com.example.booking_server.dtos.EventRequest;
import com.example.booking_server.entities.Event;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface EventMapper {
    Event toEntity(EventRequest request);
    EventDto toDto(Event event);
}
