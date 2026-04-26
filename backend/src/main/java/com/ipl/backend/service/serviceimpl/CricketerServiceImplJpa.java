package com.ipl.backend.service.serviceimpl;

import com.ipl.backend.entity.Cricketer;
import com.ipl.backend.entity.Team;
import com.ipl.backend.exception.TeamCricketerLimitExceededException;
import com.ipl.backend.exception.TeamDoesNotExistException;
import com.ipl.backend.repository.CricketerRepository;
import com.ipl.backend.repository.TeamRepository;
import com.ipl.backend.service.CricketerService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;

@Service
@Transactional
public class CricketerServiceImplJpa implements CricketerService {

    private static final Logger log = LoggerFactory.getLogger(CricketerServiceImplJpa.class);
    private static final int MAX_CRICKETERS_PER_TEAM = 11;

    private final CricketerRepository cricketerRepository;
    private final TeamRepository teamRepository;

    public CricketerServiceImplJpa(CricketerRepository cricketerRepository,
            TeamRepository teamRepository) {
        this.cricketerRepository = cricketerRepository;
        this.teamRepository = teamRepository;
    }

    @Override
    public Cricketer addCricketer(Cricketer cricketer) {
        normalizeCricketerFields(cricketer);

        Team team = resolveTeam(cricketer.getTeam());
        validateTeamCricketerLimit(team.getTeamId());

        cricketer.setTeam(team);
        Cricketer savedCricketer = cricketerRepository.save(cricketer);
        log.info("Cricketer created: cricketerId={}, name={}, teamId={}",
                savedCricketer.getCricketerId(),
                savedCricketer.getCricketerName(),
                team.getTeamId());

        return savedCricketer;
    }

    @Override
    @Transactional(readOnly = true)
    public List<Cricketer> getAllCricketers() {
        return cricketerRepository.findAll();
    }

    @Override
    @Transactional(readOnly = true)
    public List<Cricketer> getCricketersSortedByExperience() {
        return cricketerRepository.findAllByOrderByExperienceDesc();
    }

    @Override
    @Transactional(readOnly = true)
    public Cricketer getCricketerById(Integer cricketerId) {
        return cricketerRepository.findByCricketerId(cricketerId)
                .orElseThrow(() -> {
                    log.warn("Get cricketer failed: cricketerId={} not found", cricketerId);
                    return new ResponseStatusException(HttpStatus.NOT_FOUND,
                            "Cricketer not found with id: " + cricketerId);
                });
    }

    @Override
    @Transactional(readOnly = true)
    public List<Cricketer> getCricketersByTeamId(Integer teamId) {
        getTeamById(teamId);
        return cricketerRepository.findByTeam_TeamId(teamId);
    }

    @Override
    public Cricketer updateCricketer(Integer cricketerId, Cricketer updatedCricketer) {
        Cricketer existingCricketer = getCricketerById(cricketerId);
        normalizeCricketerFields(updatedCricketer);

        Team updatedTeam = resolveTeam(updatedCricketer.getTeam());
        boolean teamChanged = !existingCricketer.getTeam().getTeamId().equals(updatedTeam.getTeamId());

        if (teamChanged) {
            validateTeamCricketerLimit(updatedTeam.getTeamId());
        }

        existingCricketer.setTeam(updatedTeam);
        existingCricketer.setCricketerName(updatedCricketer.getCricketerName());
        existingCricketer.setAge(updatedCricketer.getAge());
        existingCricketer.setNationality(updatedCricketer.getNationality());
        existingCricketer.setExperience(updatedCricketer.getExperience());
        existingCricketer.setRole(updatedCricketer.getRole());
        existingCricketer.setTotalRuns(updatedCricketer.getTotalRuns());
        existingCricketer.setTotalWickets(updatedCricketer.getTotalWickets());

        Cricketer savedCricketer = cricketerRepository.save(existingCricketer);
        log.info("Cricketer updated: cricketerId={}, name={}, teamId={}",
                savedCricketer.getCricketerId(),
                savedCricketer.getCricketerName(),
                savedCricketer.getTeam().getTeamId());

        return savedCricketer;
    }

    @Override
    public void deleteCricketer(Integer cricketerId) {
        Cricketer cricketer = getCricketerById(cricketerId);

        if (!cricketer.getVotes().isEmpty()) {
            log.warn("Delete cricketer rejected: cricketerId={} has linked votes={}",
                    cricketer.getCricketerId(),
                    cricketer.getVotes().size());

            throw new ResponseStatusException(
                    HttpStatus.CONFLICT,
                    "Cannot delete cricketer because votes are linked to this cricketer");
        }

        cricketerRepository.delete(cricketer);
        log.info("Cricketer deleted: cricketerId={}, name={}",
                cricketer.getCricketerId(),
                cricketer.getCricketerName());
    }

    private Team resolveTeam(Team teamFromRequest) {
        if (teamFromRequest == null || teamFromRequest.getTeamId() == null) {
            log.warn("Cricketer request rejected: missing team.teamId");
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "team.teamId is required");
        }

        return getTeamById(teamFromRequest.getTeamId());
    }

    private Team getTeamById(Integer teamId) {
        return teamRepository.findByTeamId(teamId)
                .orElseThrow(() -> {
                    log.warn("Team not found for cricketer operation: teamId={}", teamId);
                    return new TeamDoesNotExistException("Team not found with id: " + teamId);
                });
    }

    private void validateTeamCricketerLimit(Integer teamId) {
        long cricketerCount = cricketerRepository.countByTeam_TeamId(teamId);

        if (cricketerCount >= MAX_CRICKETERS_PER_TEAM) {
            log.warn("Add/Update cricketer rejected: teamId={} already has {} players",
                    teamId, cricketerCount);

            throw new TeamCricketerLimitExceededException(
                    "Cannot add more players. Team with id " + teamId + " already has "
                            + MAX_CRICKETERS_PER_TEAM + " cricketers");
        }
    }

    private void normalizeCricketerFields(Cricketer cricketer) {
        cricketer.setCricketerName(normalize(cricketer.getCricketerName()));
        cricketer.setNationality(normalize(cricketer.getNationality()));
        cricketer.setRole(normalize(cricketer.getRole()));
    }

    private String normalize(String value) {
        return value == null ? null : value.trim().replaceAll("\\s+", " ");
    }
}