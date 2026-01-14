package com.example.booking_server.mappers;

import com.example.booking_server.dtos.TicketDto;
import com.example.booking_server.dtos.TicketTypeRequest;
import com.example.booking_server.entities.TicketType;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface TicketTypeMapper {
    @Mapping(target = "event", ignore = true)
    @Mapping(target = "seats", ignore = true)
    TicketType toEntity(TicketTypeRequest request);

    @Mapping(target = "eventId", source = "event.eventId")
    TicketDto toDto(TicketType ticketType);
}
