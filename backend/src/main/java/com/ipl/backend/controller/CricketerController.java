package com.ipl.backend.controller;

import com.ipl.backend.entity.Cricketer;
import com.ipl.backend.service.CricketerService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/cricketer")
public class CricketerController {

    private final CricketerService cricketerService;

    public CricketerController(CricketerService cricketerService) {
        this.cricketerService = cricketerService;
    }

    @GetMapping
    public ResponseEntity<List<Cricketer>> getAllCricketers() {
        return ResponseEntity.ok(cricketerService.getAllCricketers());
    }

    @GetMapping("/sorted/experience")
    public ResponseEntity<List<Cricketer>> getCricketersSortedByExperience() {
        return ResponseEntity.ok(cricketerService.getCricketersSortedByExperience());
    }

    @GetMapping("/{id}")
    public ResponseEntity<Cricketer> getCricketerById(@PathVariable("id") Integer cricketerId) {
        return ResponseEntity.ok(cricketerService.getCricketerById(cricketerId));
    }

    @GetMapping("/team/{teamId}")
    public ResponseEntity<List<Cricketer>> getCricketersByTeam(@PathVariable("teamId") Integer teamId) {
        return ResponseEntity.ok(cricketerService.getCricketersByTeamId(teamId));
    }

    @PostMapping
    public ResponseEntity<Cricketer> addCricketer(@Valid @RequestBody Cricketer cricketer) {
        Cricketer savedCricketer = cricketerService.addCricketer(cricketer);
        return ResponseEntity.status(HttpStatus.CREATED).body(savedCricketer);
    }

    @PutMapping("/{id}")
    public ResponseEntity<Cricketer> updateCricketer(@PathVariable("id") Integer cricketerId,
            @Valid @RequestBody Cricketer updatedCricketer) {
        Cricketer cricketer = cricketerService.updateCricketer(cricketerId, updatedCricketer);
        return ResponseEntity.ok(cricketer);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Map<String, String>> deleteCricketer(@PathVariable("id") Integer cricketerId) {
        cricketerService.deleteCricketer(cricketerId);
        return ResponseEntity.ok(Map.of("message", "Cricketer deleted successfully"));
    }
}