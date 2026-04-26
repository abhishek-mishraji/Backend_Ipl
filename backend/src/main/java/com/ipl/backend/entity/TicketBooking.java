package com.ipl.backend.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Index;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name = "ticket_booking", indexes = {
        @Index(name = "idx_ticket_booking_user_id", columnList = "user_id"),
        @Index(name = "idx_ticket_booking_match_id", columnList = "match_id")
})
@Getter
@Setter
@NoArgsConstructor
public class TicketBooking {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "booking_id")
    private Integer bookingId;

    @NotNull(message = "User is required")
    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    @NotNull(message = "Match is required")
    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "match_id", nullable = false)
    private Match match;

    @NotNull(message = "Number of tickets is required")
    @Min(value = 1, message = "At least 1 ticket must be booked")
    @Column(name = "number_of_tickets", nullable = false)
    private Integer numberOfTickets;

    public TicketBooking(User user, Match match, Integer numberOfTickets) {
        this.user = user;
        this.match = match;
        this.numberOfTickets = numberOfTickets;
    }
}