package com.ipl.backend.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Index;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import jakarta.persistence.UniqueConstraint;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "team", uniqueConstraints = {
        @UniqueConstraint(name = "uk_team_name", columnNames = "team_name")
}, indexes = {
        @Index(name = "idx_team_name", columnList = "team_name"),
        @Index(name = "idx_team_location", columnList = "location")
})
@Getter
@Setter
@NoArgsConstructor
public class Team {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "team_id")
    private Integer teamId;

    @NotBlank(message = "Team name is required")
    @Size(min = 2, max = 50, message = "Team name must be between 2 and 50 characters")
    @Column(name = "team_name", nullable = false, length = 50)
    private String teamName;

    @NotBlank(message = "Location is required")
    @Size(min = 2, max = 60, message = "Location must be between 2 and 60 characters")
    @Column(name = "location", nullable = false, length = 60)
    private String location;

    @NotBlank(message = "Owner name is required")
    @Size(min = 2, max = 100, message = "Owner name must be between 2 and 100 characters")
    @Column(name = "owner_name", nullable = false, length = 100)
    private String ownerName;

    @NotNull(message = "Establishment year is required")
    @Min(value = 1800, message = "Establishment year must be realistic")
    @Max(value = 2100, message = "Establishment year must be realistic")
    @Column(name = "establishment_year", nullable = false)
    private Integer establishmentYear;

    @JsonIgnore
    @OneToMany(mappedBy = "team", fetch = FetchType.LAZY)
    private List<Cricketer> cricketers = new ArrayList<>();

    @JsonIgnore
    @OneToMany(mappedBy = "firstTeam", fetch = FetchType.LAZY)
    private List<Match> firstTeamMatches = new ArrayList<>();

    @JsonIgnore
    @OneToMany(mappedBy = "secondTeam", fetch = FetchType.LAZY)
    private List<Match> secondTeamMatches = new ArrayList<>();

    @JsonIgnore
    @OneToMany(mappedBy = "winnerTeam", fetch = FetchType.LAZY)
    private List<Match> winnerTeamMatches = new ArrayList<>();

    @JsonIgnore
    @OneToMany(mappedBy = "team", fetch = FetchType.LAZY)
    private List<Vote> votes = new ArrayList<>();

    public Team(String teamName, String location, String ownerName, Integer establishmentYear) {
        this.teamName = teamName;
        this.location = location;
        this.ownerName = ownerName;
        this.establishmentYear = establishmentYear;
    }
}