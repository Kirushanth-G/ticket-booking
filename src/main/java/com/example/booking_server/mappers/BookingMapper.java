package com.example.booking_server.mappers;

import com.example.booking_server.dtos.BookingDto;
import com.example.booking_server.dtos.BookingRequest;
import com.example.booking_server.entities.Booking;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface BookingMapper {
    @Mapping(target = "bookingId", ignore = true)
    @Mapping(target = "event", ignore = true)
    @Mapping(target = "bookedAt", ignore = true)
    @Mapping(target = "totalAmount", ignore = true)
    @Mapping(target = "items", ignore = true)
    Booking toEntity(BookingRequest request);

    @Mapping(target = "eventName", source = "event.eventName")
    @Mapping(target = "seatNumbers" , expression = "java(booking.getItems().stream().map(item -> item.getSeat().getSeatNo()).toList())")
    BookingDto toDto(Booking booking);
}
