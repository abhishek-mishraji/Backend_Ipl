package com.ipl.backend.controller;

import com.ipl.backend.entity.Team;
import com.ipl.backend.service.TeamService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/team")
public class TeamController {

    private final TeamService teamService;

    public TeamController(TeamService teamService) {
        this.teamService = teamService;
    }

    @GetMapping
    public ResponseEntity<List<Team>> getAllTeams() {
        return ResponseEntity.ok(teamService.getAllTeams());
    }

    @GetMapping("/sorted/name")
    public ResponseEntity<List<Team>> getTeamsSortedByName() {
        return ResponseEntity.ok(teamService.getTeamsSortedByName());
    }

    @GetMapping("/{id}")
    public ResponseEntity<Team> getTeamById(@PathVariable("id") Integer teamId) {
        return ResponseEntity.ok(teamService.getTeamById(teamId));
    }

    @PostMapping
    public ResponseEntity<Team> addTeam(@Valid @RequestBody Team team) {
        Team savedTeam = teamService.addTeam(team);
        return ResponseEntity.status(HttpStatus.CREATED).body(savedTeam);
    }

    @PutMapping("/{id}")
    public ResponseEntity<Team> updateTeam(@PathVariable("id") Integer teamId,
            @Valid @RequestBody Team updatedTeam) {
        Team team = teamService.updateTeam(teamId, updatedTeam);
        return ResponseEntity.ok(team);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Map<String, String>> deleteTeam(@PathVariable("id") Integer teamId) {
        teamService.deleteTeam(teamId);
        return ResponseEntity.ok(Map.of("message", "Team deleted successfully"));
    }
}