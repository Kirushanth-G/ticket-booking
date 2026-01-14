package com.example.booking_server.services;

import com.example.booking_server.dtos.TicketDto;
import com.example.booking_server.dtos.TicketTypeRequest;
import com.example.booking_server.entities.Event;
import com.example.booking_server.entities.Seat;
import com.example.booking_server.entities.SeatStatus;
import com.example.booking_server.entities.TicketType;
import com.example.booking_server.exceptions.EventNotFoundException;
import com.example.booking_server.mappers.TicketTypeMapper;
import com.example.booking_server.repositories.EventRepository;
import com.example.booking_server.repositories.SeatRepository;
import com.example.booking_server.repositories.TicketTypeRepository;
import jakarta.transaction.Transactional;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
@AllArgsConstructor
public class TicketTypeService {
    private final EventRepository eventRepository;
    private final TicketTypeRepository ticketTypeRepository;
    private final TicketTypeMapper ticketTypeMapper;
    private final SeatRepository seatRepository;

    @Transactional
    public TicketDto createTicketType(TicketTypeRequest request) {
        Event event = eventRepository.findById(request.getEventId())
                .orElseThrow(() -> new EventNotFoundException("Event not found with ID: " + request.getEventId()));
        if(ticketTypeRepository.existsByEventAndTypeName(event, request.getTypeName())) {
            throw new IllegalArgumentException("Ticket type with this name already exists for this event");
        }
        TicketType ticketType = ticketTypeMapper.toEntity(request);
        ticketType.setEvent(event);
        TicketType savedTicketType = ticketTypeRepository.save(ticketType);

        List<Seat> seats = new ArrayList<>();
        for (int i = 0; i < request.getQuantity(); i++) {
            Seat seat = new Seat();
            seat.setTicketType(savedTicketType);
            seat.setSeatNo(request.getTypeName() + "-" + (i + 1));
            seat.setStatus(SeatStatus.AVAILABLE);
            seats.add(seat);
        }
        seatRepository.saveAll(seats);
        savedTicketType.setSeats(seats);
        return ticketTypeMapper.toDto(savedTicketType);
    }
}
