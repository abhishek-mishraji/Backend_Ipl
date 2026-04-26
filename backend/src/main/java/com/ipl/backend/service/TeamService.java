package com.ipl.backend.service;

import com.ipl.backend.entity.Team;

import java.util.List;

public interface TeamService {

    Team addTeam(Team team);

    List<Team> getAllTeams();

    List<Team> getTeamsSortedByName();

    Team getTeamById(Integer teamId);

    Team updateTeam(Integer teamId, Team updatedTeam);

    void deleteTeam(Integer teamId);
}