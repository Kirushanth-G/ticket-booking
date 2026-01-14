package com.example.booking_server.services;

import com.example.booking_server.dtos.BookingDto;
import com.example.booking_server.dtos.BookingRequest;
import com.example.booking_server.entities.*;
import com.example.booking_server.mappers.BookingMapper;
import com.example.booking_server.repositories.BookingRepository;
import com.example.booking_server.repositories.SeatRepository;
import com.example.booking_server.repositories.TicketTypeRepository;
import jakarta.transaction.Transactional;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.List;

@Service
@AllArgsConstructor
public class BookingService {
    private final TicketTypeRepository ticketTypeRepository;
    private final SeatRepository seatRepository;
    private final BookingMapper bookingMapper;
    private final BookingRepository bookingRepository;
    @Transactional
    public BookingDto bookTicket(BookingRequest request){
        TicketType ticketType = ticketTypeRepository.findById(request.getTicketTypeId()).orElseThrow(() -> new IllegalArgumentException("Ticket type not found."));

        List<Seat> availableSeats = seatRepository.findAvailableSeatsForUpdate(request.getTicketTypeId(), request.getQuantity());

        if(availableSeats.size() < request.getQuantity()){
            throw new IllegalArgumentException("Not enough available seats. Found: " + availableSeats.size());
        }

        BigDecimal totalPrice = ticketType.getPrice().multiply(BigDecimal.valueOf(request.getQuantity()));

        Booking booking = bookingMapper.toEntity(request);
        booking.setEvent(ticketType.getEvent());
        booking.setTotalAmount(totalPrice);

        for(Seat seat : availableSeats){
            seat.setStatus(SeatStatus.BOOKED);
            seat.setReservedBy(request.getUserId());

            BookingItem item = new BookingItem();
            item.setBooking(booking);
            item.setSeat(seat);
            booking.getItems().add(item);
        }

        Booking savedBooking = bookingRepository.save(booking);
        return bookingMapper.toDto(savedBooking);
    }
}
