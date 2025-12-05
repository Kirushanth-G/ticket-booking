package com.example.booking_server.entities;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "events")
public class Event {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "event_id")
    private Long eventId;

    @Column(name = "event_name")
    private String eventName;

    @Column(name = "event_date")
    private LocalDateTime eventDate;

    @Column(name = "venue")
    private String venue;

    @Enumerated(EnumType.STRING)
    @Column(name = "status", columnDefinition = "VARCHAR(255) DEFAULT 'UPCOMING'")
    private EventStatus status;

    @OneToMany
    private List<TicketType> ticketTypes = new ArrayList<>();
}
