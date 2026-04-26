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
import jakarta.persistence.Table;
import jakarta.validation.constraints.AssertTrue;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name = "vote", indexes = {
        @Index(name = "idx_vote_category", columnList = "category"),
        @Index(name = "idx_vote_email", columnList = "email"),
        @Index(name = "idx_vote_team_id", columnList = "team_id"),
        @Index(name = "idx_vote_cricketer_id", columnList = "cricketer_id")
})
@Getter
@Setter
@NoArgsConstructor
public class Vote {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "vote_id")
    private Integer voteId;

    @NotBlank(message = "Email is required")
    @Email(message = "Email format is invalid")
    @Size(max = 120, message = "Email must be at most 120 characters")
    @Column(name = "email", nullable = false, length = 120)
    private String email;

    @NotBlank(message = "Category is required")
    @Pattern(regexp = "Team|Batsman|Bowler|All-rounder|Wicketkeeper", message = "Category must be Team, Batsman, Bowler, All-rounder, or Wicketkeeper")
    @Column(name = "category", nullable = false, length = 30)
    private String category;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "team_id")
    private Team team;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "cricketer_id")
    private Cricketer cricketer;

    public Vote(String email, String category, Team team, Cricketer cricketer) {
        this.email = email;
        this.category = category;
        this.team = team;
        this.cricketer = cricketer;
    }

    @JsonIgnore
    @AssertTrue(message = "If category is Team then team must be set (and cricketer must be null). For other categories, cricketer must be set (and team must be null).")
    public boolean isVoteTargetValid() {
        if (category == null) {
            return true;
        }
        if ("Team".equals(category)) {
            return team != null && cricketer == null;
        }
        return cricketer != null && team == null;
    }
}