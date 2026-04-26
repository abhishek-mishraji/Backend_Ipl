package com.ipl.backend.repository;

import com.ipl.backend.entity.Cricketer;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface CricketerRepository extends JpaRepository<Cricketer, Integer> {

    Optional<Cricketer> findByCricketerId(Integer cricketerId);

    List<Cricketer> findByTeam_TeamId(Integer teamId);

    long countByTeam_TeamId(Integer teamId);

    List<Cricketer> findAllByOrderByExperienceDesc();
}