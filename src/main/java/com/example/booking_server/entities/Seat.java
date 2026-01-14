package com.example.booking_server.entities;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.JdbcType;
import org.hibernate.dialect.type.PostgreSQLEnumJdbcType;

import java.time.LocalDateTime;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "seats")
public class Seat {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "seat_id")
    private Long seatId;

    @ManyToOne
    @JoinColumn(name = "ticket_type_id", nullable = false)
    private TicketType ticketType;

    @Column(name = "seat_no", nullable = false, length = 10)
    private String seatNo;

    @Enumerated(EnumType.STRING)
    @Column(name = "status", columnDefinition = "seat_status DEFAULT 'AVAILABLE'")
    @JdbcType(PostgreSQLEnumJdbcType.class)
    private SeatStatus status;

    @Column(name = "reserved_by")
    private Long reservedBy; // User ID holding the lock

    @Column(name = "reserved_until")
    private LocalDateTime reservedUntil;

    @Version
    private Integer version;
}
