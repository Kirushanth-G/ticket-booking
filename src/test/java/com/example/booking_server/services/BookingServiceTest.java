package com.example.booking_server.services;

import com.example.booking_server.dtos.BookingRequest;
import com.example.booking_server.entities.Event;
import com.example.booking_server.entities.EventStatus;
import com.example.booking_server.entities.Seat;
import com.example.booking_server.entities.TicketType;
import com.example.booking_server.repositories.EventRepository;
import com.example.booking_server.repositories.SeatRepository;
import com.example.booking_server.repositories.TicketTypeRepository;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.atomic.AtomicInteger;

@SpringBootTest
public class BookingServiceTest {
    @Autowired
    private EventRepository eventRepository;

    @Autowired
    private TicketTypeRepository ticketTypeRepository;

    @Autowired
    private SeatRepository seatRepository;

    @Autowired
    private BookingService bookingService;
    @Test
    public void testConcurrentBookings() throws InterruptedException{
        Event event = new Event();
        event.setEventName("Concurrent Event Test");
        event.setVenue("Test Venue");
        event.setEventDate(LocalDateTime.now().plusDays(10));
        event.setStatus(EventStatus.ON_SALE);
        eventRepository.save(event);

        TicketType ticketType = new TicketType();
        ticketType.setEvent(event);
        ticketType.setTypeName("General Admission");
        ticketType.setPrice(java.math.BigDecimal.valueOf(50.00));
        ticketTypeRepository.save(ticketType);

        List<Seat> seats = new ArrayList<>();
        for(int i = 1; i <= 5; i++){
            Seat seat = new Seat();
            seat.setSeatNo("A" + i);
            seat.setTicketType(ticketType);
            seat.setStatus(com.example.booking_server.entities.SeatStatus.AVAILABLE);
            seats.add(seat);
        }
        seatRepository.saveAll(seats);

        int numberOfThreads = 10;
        ExecutorService executorService = Executors.newFixedThreadPool(numberOfThreads);
        CountDownLatch latch = new CountDownLatch(numberOfThreads);

        AtomicInteger successfulBookings = new AtomicInteger(0);
        AtomicInteger failedBookings = new AtomicInteger(0);

        for(int i = 0; i < numberOfThreads; i++){
            final int userId = i + 1;
            executorService.submit(() -> {
                try{
                    BookingRequest request = new BookingRequest();
                    request.setUserId((long) userId);
                    request.setTicketTypeId(ticketType.getTicketTypeId());
                    request.setQuantity(1);

                    bookingService.bookTicket(request);
                    successfulBookings.incrementAndGet();
                } catch (Exception e){
                    failedBookings.incrementAndGet();
                } finally {
                    latch.countDown();
                }
            });
        }
        latch.await();
        executorService.shutdown();
        System.out.println("Successful bookings: " + successfulBookings.get());
        System.out.println("Failed bookings: " + failedBookings.get());
        Assertions.assertEquals(5, successfulBookings.get());
        Assertions.assertEquals(5, failedBookings.get());

//        Long bookedSeats = seatRepository.countByTicketType_TicketTypeIdAndStatus(ticketType.getTicketTypeId(), com.example.booking_server.entities.SeatStatus.BOOKED);
//        Assertions.assertEquals(5L, bookedSeats);
    }

    @Test
    public void testOptimisticLocking() throws InterruptedException{
        Event event = new Event();
        event.setEventName("Concurrent Event Test");
        event.setVenue("Test Venue");
        event.setEventDate(LocalDateTime.now().plusDays(10));
        event.setStatus(EventStatus.ON_SALE);
        eventRepository.save(event);

        TicketType ticketType = new TicketType();
        ticketType.setEvent(event);
        ticketType.setTypeName("General Admission");
        ticketType.setPrice(java.math.BigDecimal.valueOf(50.00));
        ticketTypeRepository.save(ticketType);

        List<Seat> seats = new ArrayList<>();
        for(int i = 1; i <= 5; i++){
            Seat seat = new Seat();
            seat.setSeatNo("A" + i);
            seat.setTicketType(ticketType);
            seat.setStatus(com.example.booking_server.entities.SeatStatus.AVAILABLE);
            seats.add(seat);
        }
        seatRepository.saveAll(seats);

        int numberOfThreads = 10;
        ExecutorService executorService = Executors.newFixedThreadPool(numberOfThreads);
        CountDownLatch latch = new CountDownLatch(numberOfThreads);

        AtomicInteger successfulBookings = new AtomicInteger(0);
        AtomicInteger failedBookings = new AtomicInteger(0);

        for(int i = 0; i < numberOfThreads; i++){
            final int userId = i + 1;
            executorService.submit(() -> {
                try{
                    BookingRequest request = new BookingRequest();
                    request.setUserId((long) userId);
                    request.setTicketTypeId(ticketType.getTicketTypeId());
                    request.setQuantity(1);

                    bookingService.bookTicketOptimistic(request);
                    successfulBookings.incrementAndGet();
                } catch (Exception e){
                    failedBookings.incrementAndGet();
                } finally {
                    latch.countDown();
                }
            });
        }
        latch.await();
        executorService.shutdown();
        System.out.println("Successful bookings: " + successfulBookings.get());
        System.out.println("Failed bookings: " + failedBookings.get());
        Assertions.assertEquals(5, successfulBookings.get());
        Assertions.assertEquals(5, failedBookings.get());
    }
}
