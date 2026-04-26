package com.ipl.backend.service;

import com.ipl.backend.entity.Cricketer;

import java.util.List;

public interface CricketerService {

    Cricketer addCricketer(Cricketer cricketer);

    List<Cricketer> getAllCricketers();

    List<Cricketer> getCricketersSortedByExperience();

    Cricketer getCricketerById(Integer cricketerId);

    List<Cricketer> getCricketersByTeamId(Integer teamId);

    Cricketer updateCricketer(Integer cricketerId, Cricketer updatedCricketer);

    void deleteCricketer(Integer cricketerId);
}