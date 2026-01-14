package com.example.booking_server.controllers;

import com.example.booking_server.dtos.BookingRequest;
import com.example.booking_server.entities.Event;
import com.example.booking_server.entities.TicketType;
import com.example.booking_server.repositories.BookingRepository;
import com.example.booking_server.repositories.EventRepository;
import com.example.booking_server.repositories.TicketTypeRepository;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.ResponseEntity;
import org.springframework.web.util.UriComponentsBuilder;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicInteger;

import static org.junit.jupiter.api.Assertions.assertTrue;

@SpringBootTest
class BookingControllerTest {

//    @Autowired
//    private BookingController bookingController;
//    @Autowired
//    private BookingRepository bookingRepository;
//    @Autowired
//    private TicketTypeRepository ticketTypeRepository;
//    @Autowired
//    private EventRepository eventRepository;
//
//    private ExecutorService executorService;
//    private Long ticketTypeId;
//    private final int TOTAL_SEATS = 10;
//    private final int TOTAL_THREADS = 20;
//
//    @BeforeEach
//    void setUp() {
//        // Initialize the thread pool
//        executorService = Executors.newFixedThreadPool(TOTAL_THREADS);
//
//        // 1. Setup Data Manually (Bypassing DTOs/Mappers to reduce points of failure)
//        Event event = new Event();
//        event.setEventName("Flash Sale Concert");
//        event.setEventDate(LocalDateTime.now().plusDays(10));
//        event.setVenue("Stadium");
//        event = eventRepository.save(event);
//
//        TicketType ticketType = new TicketType();
//        ticketType.setEvent(event);
//        ticketType.setTypeName("VIP");
//        ticketType.setPrice(BigDecimal.valueOf(100.00));
//        ticketType.setQuantity(TOTAL_SEATS); // Setting strictly to 10
//        ticketType = ticketTypeRepository.save(ticketType);
//
//        this.ticketTypeId = ticketType.getTicketTypeId(); // Ensure this matches your Entity's getter name!
//
//        System.out.println("--- SETUP COMPLETE ---");
//        System.out.println("Created TicketType ID: " + this.ticketTypeId);
//        System.out.println("Initial Quantity: " + ticketType.getQuantity());
//    }
//
//    @AfterEach
//    void tearDown() {
//        if (executorService != null) {
//            executorService.shutdownNow();
//        }
//        bookingRepository.deleteAll();
//        ticketTypeRepository.deleteAll();
//        eventRepository.deleteAll();
//    }
//
//    @Test
//    void testRaceCondition_ShouldFailIfCodeIsBuggy() throws InterruptedException {
//        AtomicInteger successCount = new AtomicInteger(0);
//        AtomicInteger failCount = new AtomicInteger(0);
//        CountDownLatch startLatch = new CountDownLatch(1);
//        CountDownLatch doneLatch = new CountDownLatch(TOTAL_THREADS);
//
//        System.out.println("=== STARTING RACE CONDITION TEST ===");
//
//        for (int i = 0; i < TOTAL_THREADS; i++) {
//            final Long userId = (long) (i + 1);
//            executorService.submit(() -> {
//                try {
//                    startLatch.await(); // Wait for signal
//
//                    BookingRequest request = new BookingRequest();
//                    request.setTicketTypeId(ticketTypeId);
//                    request.setUserId(userId);
//                    request.setSeatsBooked(1);
//
//                    // Call the controller with a UriComponentsBuilder
//                    UriComponentsBuilder uriBuilder = UriComponentsBuilder.fromPath("");
//                    ResponseEntity<?> response = bookingController.bookTicket(request, uriBuilder);
//
//                    if (response.getStatusCode().is2xxSuccessful()) {
//                        successCount.incrementAndGet();
//                    } else {
//                        System.err.println("Request Failed with Status: " + response.getStatusCode());
//                        failCount.incrementAndGet();
//                    }
//                } catch (Exception e) {
//                    // --- CRITICAL: PRINT THE ERROR ---
//                    System.err.println("Exception in thread " + userId + ":");
//                    e.printStackTrace();
//                    failCount.incrementAndGet();
//                } finally {
//                    doneLatch.countDown();
//                }
//            });
//        }
//
//        // Unleash the threads
//        startLatch.countDown();
//        boolean finished = doneLatch.await(10, TimeUnit.SECONDS);
//
//        // Wait a moment for DB to commit
//        Thread.sleep(1000);
//
//        // Verify Data
//        long totalBookings = bookingRepository.count();
//        TicketType updatedTicketType = ticketTypeRepository.findById(ticketTypeId).orElseThrow();
//
//        System.out.println("--- RESULT ---");
//        System.out.println("Available Seats: " + TOTAL_SEATS);
//        System.out.println("Users Trying: " + TOTAL_THREADS);
//        System.out.println("Successful Bookings: " + totalBookings);
//        System.out.println("Remaining Inventory: " + updatedTicketType.getQuantity());
//        System.out.println("--------------");
//
//        // The assertion: We EXPECT this to fail (proving the race condition)
//        // If totalBookings > 10, the code is buggy (which is what we want to prove right now)
//        assertTrue(totalBookings <= TOTAL_SEATS,
//                "FAILURE: System oversold! Booked " + totalBookings + " seats but only " + TOTAL_SEATS + " were available.");
//    }
}