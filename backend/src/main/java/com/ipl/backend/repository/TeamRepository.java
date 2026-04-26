package com.ipl.backend.repository;

import com.ipl.backend.entity.Team;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface TeamRepository extends JpaRepository<Team, Integer> {

    Optional<Team> findByTeamId(Integer teamId);

    Optional<Team> findByTeamNameIgnoreCase(String teamName);

    boolean existsByTeamNameIgnoreCase(String teamName);

    List<Team> findAllByOrderByTeamNameAsc();
}