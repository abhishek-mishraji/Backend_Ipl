package com.ipl.backend.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Index;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "match", indexes = {
        @Index(name = "idx_match_status", columnList = "status"),
        @Index(name = "idx_match_date", columnList = "match_date")
})
@Getter
@Setter
@NoArgsConstructor
public class Match {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "match_id")
    private Integer matchId;

    @NotNull(message = "First team is required")
    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "first_team_id", nullable = false)
    private Team firstTeam;

    @NotNull(message = "Second team is required")
    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "second_team_id", nullable = false)
    private Team secondTeam;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "winner_team_id")
    private Team winnerTeam;

    @NotNull(message = "Match date is required")
    @Column(name = "match_date", nullable = false)
    private LocalDate matchDate;

    @NotBlank(message = "Venue is required")
    @Size(min = 2, max = 100, message = "Venue must be between 2 and 100 characters")
    @Column(name = "venue", nullable = false, length = 100)
    private String venue;

    @Size(max = 150, message = "Result must be at most 150 characters")
    @Column(name = "result", length = 150)
    private String result;

    @NotBlank(message = "Status is required")
    @Pattern(regexp = "Pending|Scheduled|Completed", message = "Status must be Pending, Scheduled, or Completed")
    @Column(name = "status", nullable = false, length = 20)
    private String status;

    @JsonIgnore
    @OneToMany(mappedBy = "match", fetch = FetchType.LAZY)
    private List<TicketBooking> bookings = new ArrayList<>();

    public Match(Team firstTeam, Team secondTeam, Team winnerTeam, LocalDate matchDate,
            String venue, String result, String status) {
        this.firstTeam = firstTeam;
        this.secondTeam = secondTeam;
        this.winnerTeam = winnerTeam;
        this.matchDate = matchDate;
        this.venue = venue;
        this.result = result;
        this.status = status;
    }
}