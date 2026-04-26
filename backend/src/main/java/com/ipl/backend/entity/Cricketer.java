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
@Table(name = "cricketer", indexes = {
        @Index(name = "idx_cricketer_team_id", columnList = "team_id"),
        @Index(name = "idx_cricketer_name", columnList = "cricketer_name"),
        @Index(name = "idx_cricketer_role", columnList = "role")
})
@Getter
@Setter
@NoArgsConstructor
public class Cricketer {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "cricketer_id")
    private Integer cricketerId;

    @NotNull(message = "Team is required")
    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "team_id", nullable = false)
    private Team team;

    @NotBlank(message = "Cricketer name is required")
    @Size(min = 2, max = 100, message = "Cricketer name must be between 2 and 100 characters")
    @Column(name = "cricketer_name", nullable = false, length = 100)
    private String cricketerName;

    @NotNull(message = "Age is required")
    @Min(value = 15, message = "Age must be at least 15")
    @Max(value = 60, message = "Age must be at most 60")
    @Column(name = "age", nullable = false)
    private Integer age;

    @NotBlank(message = "Nationality is required")
    @Size(min = 2, max = 60, message = "Nationality must be between 2 and 60 characters")
    @Column(name = "nationality", nullable = false, length = 60)
    private String nationality;

    @NotNull(message = "Experience is required")
    @Min(value = 0, message = "Experience cannot be negative")
    @Column(name = "experience", nullable = false)
    private Integer experience;

    @NotBlank(message = "Role is required")
    @Size(min = 3, max = 30, message = "Role must be between 3 and 30 characters")
    @Column(name = "role", nullable = false, length = 30)
    private String role;

    @NotNull(message = "Total runs is required")
    @Min(value = 0, message = "Total runs cannot be negative")
    @Column(name = "total_runs", nullable = false)
    private Integer totalRuns;

    @NotNull(message = "Total wickets is required")
    @Min(value = 0, message = "Total wickets cannot be negative")
    @Column(name = "total_wickets", nullable = false)
    private Integer totalWickets;

    @JsonIgnore
    @OneToMany(mappedBy = "cricketer", fetch = FetchType.LAZY)
    private List<Vote> votes = new ArrayList<>();

    public Cricketer(Team team, String cricketerName, Integer age, String nationality,
            Integer experience, String role, Integer totalRuns, Integer totalWickets) {
        this.team = team;
        this.cricketerName = cricketerName;
        this.age = age;
        this.nationality = nationality;
        this.experience = experience;
        this.role = role;
        this.totalRuns = totalRuns;
        this.totalWickets = totalWickets;
    }
}