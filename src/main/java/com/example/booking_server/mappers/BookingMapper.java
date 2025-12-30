package com.example.booking_server.mappers;

import com.example.booking_server.dtos.BookingDto;
import com.example.booking_server.dtos.BookingRequest;
import com.example.booking_server.entities.Booking;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface BookingMapper {
    Booking toEntity(BookingRequest request);

    @Mapping(target = "ticketTypeId", source = "ticketType.ticketTypeId")
    BookingDto toDto(Booking booking);
}
