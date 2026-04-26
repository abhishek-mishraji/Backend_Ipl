package com.ipl.backend.service.serviceimpl;

import com.ipl.backend.entity.Team;
import com.ipl.backend.exception.TeamAlreadyExistsException;
import com.ipl.backend.exception.TeamDoesNotExistException;
import com.ipl.backend.repository.TeamRepository;
import com.ipl.backend.service.TeamService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;

@Service
@Transactional
public class TeamServiceImplJpa implements TeamService {

    private static final Logger log = LoggerFactory.getLogger(TeamServiceImplJpa.class);

    private final TeamRepository teamRepository;

    public TeamServiceImplJpa(TeamRepository teamRepository) {
        this.teamRepository = teamRepository;
    }

    @Override
    public Team addTeam(Team team) {
        normalizeTeamFields(team);

        if (teamRepository.existsByTeamNameIgnoreCase(team.getTeamName())) {
            log.warn("Add team rejected: duplicate teamName={}", team.getTeamName());
            throw new TeamAlreadyExistsException("Team with name '" + team.getTeamName() + "' already exists");
        }

        Team savedTeam = teamRepository.save(team);
        log.info("Team created: teamId={}, teamName={}", savedTeam.getTeamId(), savedTeam.getTeamName());
        return savedTeam;
    }

    @Override
    @Transactional(readOnly = true)
    public List<Team> getAllTeams() {
        return teamRepository.findAll();
    }

    @Override
    @Transactional(readOnly = true)
    public List<Team> getTeamsSortedByName() {
        return teamRepository.findAllByOrderByTeamNameAsc();
    }

    @Override
    @Transactional(readOnly = true)
    public Team getTeamById(Integer teamId) {
        return teamRepository.findByTeamId(teamId)
                .orElseThrow(() -> {
                    log.warn("Get team failed: teamId={} not found", teamId);
                    return new TeamDoesNotExistException("Team not found with id: " + teamId);
                });
    }

    @Override
    public Team updateTeam(Integer teamId, Team updatedTeam) {
        Team existingTeam = getTeamById(teamId);
        normalizeTeamFields(updatedTeam);

        boolean teamNameChanged = !existingTeam.getTeamName().equalsIgnoreCase(updatedTeam.getTeamName());
        if (teamNameChanged && teamRepository.existsByTeamNameIgnoreCase(updatedTeam.getTeamName())) {
            log.warn("Update team rejected: duplicate teamName={} for teamId={}",
                    updatedTeam.getTeamName(), teamId);
            throw new TeamAlreadyExistsException("Team with name '" + updatedTeam.getTeamName() + "' already exists");
        }

        existingTeam.setTeamName(updatedTeam.getTeamName());
        existingTeam.setLocation(updatedTeam.getLocation());
        existingTeam.setOwnerName(updatedTeam.getOwnerName());
        existingTeam.setEstablishmentYear(updatedTeam.getEstablishmentYear());

        Team savedTeam = teamRepository.save(existingTeam);
        log.info("Team updated: teamId={}, teamName={}", savedTeam.getTeamId(), savedTeam.getTeamName());
        return savedTeam;
    }

    @Override
    public void deleteTeam(Integer teamId) {
        Team team = getTeamById(teamId);

        if (hasLinkedRecords(team)) {
            log.warn(
                    "Delete team rejected: teamId={} linkedRecords cricketers={}, firstTeamMatches={}, secondTeamMatches={}, winnerTeamMatches={}, votes={}",
                    team.getTeamId(),
                    team.getCricketers().size(),
                    team.getFirstTeamMatches().size(),
                    team.getSecondTeamMatches().size(),
                    team.getWinnerTeamMatches().size(),
                    team.getVotes().size());

            throw new ResponseStatusException(
                    HttpStatus.CONFLICT,
                    "Cannot delete team because it is linked with cricketers, matches, or votes");
        }

        teamRepository.delete(team);
        log.info("Team deleted: teamId={}, teamName={}", team.getTeamId(), team.getTeamName());
    }

    private boolean hasLinkedRecords(Team team) {
        return !team.getCricketers().isEmpty()
                || !team.getFirstTeamMatches().isEmpty()
                || !team.getSecondTeamMatches().isEmpty()
                || !team.getWinnerTeamMatches().isEmpty()
                || !team.getVotes().isEmpty();
    }

    private void normalizeTeamFields(Team team) {
        team.setTeamName(normalize(team.getTeamName()));
        team.setLocation(normalize(team.getLocation()));
        team.setOwnerName(normalize(team.getOwnerName()));
    }

    private String normalize(String value) {
        return value == null ? null : value.trim().replaceAll("\\s+", " ");
    }
}