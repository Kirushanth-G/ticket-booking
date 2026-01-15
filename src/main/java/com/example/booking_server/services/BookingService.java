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
import org.redisson.api.RedissonClient;
import org.springframework.stereotype.Service;
import org.springframework.transaction.support.TransactionTemplate;

import java.math.BigDecimal;
import java.util.List;
import java.util.concurrent.TimeUnit;

@Service
@AllArgsConstructor
public class BookingService {
    private final TicketTypeRepository ticketTypeRepository;
    private final SeatRepository seatRepository;
    private final BookingMapper bookingMapper;
    private final BookingRepository bookingRepository;

    private final RedissonClient redissonClient;
    private final TransactionTemplate transactionTemplate;

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

    @Transactional
    public BookingDto bookTicketOptimistic(BookingRequest request) {
        TicketType ticketType = ticketTypeRepository.findById(request.getTicketTypeId()).orElseThrow(() -> new IllegalArgumentException("Ticket type not found."));
        List<Seat> availableSeats = seatRepository.findAvailableSeatsOptimistic(request.getTicketTypeId(), request.getQuantity());

        if (availableSeats.size() < request.getQuantity()) {
            throw new IllegalArgumentException("Not enough available seats. Found: " + availableSeats.size());
        }
        BigDecimal totalPrice = ticketType.getPrice().multiply(BigDecimal.valueOf(request.getQuantity()));

        Booking booking = bookingMapper.toEntity(request);
        booking.setEvent(ticketType.getEvent());
        booking.setTotalAmount(totalPrice);

        for (Seat seat : availableSeats) {
            seat.setStatus(SeatStatus.BOOKED);
            seat.setReservedBy(request.getUserId());

            BookingItem item = new BookingItem();
            item.setBooking(booking);
            item.setSeat(seat);
            booking.getItems().add(item);
        }

        try {
            Booking savedBooking = bookingRepository.save(booking);
            return bookingMapper.toDto(savedBooking);
        } catch (Exception e) {
            throw new IllegalStateException("Someone else grabbed the ticket. Please try again.");
        }
    }

    public BookingDto bookTicketWithRedisLock(BookingRequest request) {
        var lock = redissonClient.getLock("booking_lock_" + request.getTicketTypeId());
        try {
            boolean isLocked = lock.tryLock(5,10, TimeUnit.SECONDS);
            if(!isLocked){
                throw new IllegalStateException("Could not acquire lock, please try again.");
            }
            return transactionTemplate.execute(status -> bookTicketRedis(request));
        } catch(InterruptedException e){
            Thread.currentThread().interrupt();
            throw new IllegalStateException("Lock interrupted, please try again.");
        }finally {
            if(lock.isHeldByCurrentThread()){
                lock.unlock();
            }
        }
    }

    public BookingDto bookTicketRedis(BookingRequest request) {
        TicketType ticketType = ticketTypeRepository.findById(request.getTicketTypeId()).orElseThrow(() -> new IllegalArgumentException("Ticket type not found."));
        List<Seat> availableSeats = seatRepository.findAvailableSeats(request.getTicketTypeId(), request.getQuantity());

        if (availableSeats.size() < request.getQuantity()) {
            throw new IllegalArgumentException("Not enough available seats. Found: " + availableSeats.size());
        }
        BigDecimal totalPrice = ticketType.getPrice().multiply(BigDecimal.valueOf(request.getQuantity()));

        Booking booking = bookingMapper.toEntity(request);
        booking.setEvent(ticketType.getEvent());
        booking.setTotalAmount(totalPrice);

        for (Seat seat : availableSeats) {
            seat.setStatus(SeatStatus.BOOKED);
            seat.setReservedBy(request.getUserId());

            BookingItem item = new BookingItem();
            item.setBooking(booking);
            item.setSeat(seat);
            booking.getItems().add(item);
        }

        try {
            Booking savedBooking = bookingRepository.save(booking);
            return bookingMapper.toDto(savedBooking);
        } catch (Exception e) {
            throw new IllegalStateException("Someone else grabbed the ticket. Please try again.");
        }
    }
}
