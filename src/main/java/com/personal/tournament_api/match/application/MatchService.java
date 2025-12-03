package com.personal.tournament_api.match.application;

import com.personal.tournament_api.match.application.usecases.*;
import com.personal.tournament_api.match.domain.exceptions.MatchNotFoundException;
import com.personal.tournament_api.match.domain.model.Match;
import com.personal.tournament_api.match.domain.model.MatchSearchCriteria;
import com.personal.tournament_api.match.domain.model.Page;
import com.personal.tournament_api.match.domain.model.PageRequest;
import com.personal.tournament_api.match.domain.ports.MatchRepository;
import com.personal.tournament_api.match.domain.services.MatchResultService;
import com.personal.tournament_api.team.domain.exceptions.TeamNotFoundException;
import com.personal.tournament_api.team.domain.model.Team;
import com.personal.tournament_api.team.domain.ports.TeamRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
@Transactional
@Slf4j
@RequiredArgsConstructor
public class MatchService implements
        CreateMatchUseCase,
        UpdateMatchUseCase,
        FinishMatchUseCase,
        GetMatchUseCase,
        DeleteMatchUseCase,
        PostponeMatchUseCase {

    private final MatchRepository matchRepository;
    private final TeamRepository teamRepository;
    private final MatchResultService matchResultService;

    @Override
    public Match create(CreateMatchCommand command) {
        log.info("Creating match between teams {} and {} for tournament {}",
                command.homeTeamId(), command.awayTeamId(), command.tournamentId());

        Match match = new Match(
                null,
                command.tournamentId(),
                command.homeTeamId(),
                command.awayTeamId(),
                command.matchDate(),
                command.field()
        );

        Match savedMatch = matchRepository.save(match);
        log.info("Match created with id: {}", savedMatch.getId());
        return savedMatch;
    }

    @Override
    public Match update(UpdateMatchCommand command) {
        log.info("Updating match with id: {}", command.matchId());

        Match match = matchRepository.findById(command.matchId())
                .orElseThrow(() -> new MatchNotFoundException(command.matchId()));

        match.updateMatchDetails(command.matchDate(), command.field());
        Match updatedMatch = matchRepository.save(match);

        log.info("Match updated with id: {}", updatedMatch.getId());
        return updatedMatch;
    }

    @Override
    public Match finishMatch(FinishMatchCommand command) {
        log.info("Setting result for match with id: {}", command.matchId());

        Match match = matchRepository.findById(command.matchId())
                .orElseThrow(() -> new MatchNotFoundException(command.matchId()));

        Team homeTeam = teamRepository.findById(match.getHomeTeamId())
                .orElseThrow(() -> new TeamNotFoundException(match.getHomeTeamId()));
        Team awayTeam = teamRepository.findById(match.getAwayTeamId())
                .orElseThrow(() -> new TeamNotFoundException(match.getAwayTeamId()));

        var outcome = matchResultService.registerResult(match, homeTeam, awayTeam, command.homeTeamScore(), command.awayTeamScore());

        matchRepository.save(match);
        teamRepository.save(homeTeam);
        teamRepository.save(awayTeam);

        if (outcome.isCorrection()) {
            log.warn("Match {} result CORRECTED from {}-{} to {}-{}. Teams statistics updated.",
                    match.getId(), outcome.previousHomeScore(), outcome.previousAwayScore(),
                    command.homeTeamScore(), command.awayTeamScore());
        } else {
            log.info("Match {} result SET to {}-{}. Teams statistics updated.",
                    match.getId(), command.homeTeamScore(), command.awayTeamScore());
        }

        return match;
    }

    @Override
    public Match postponeMatch(Long matchId) {
        log.info("Postponing match with id: {}", matchId);

        Match match = matchRepository.findById(matchId)
                .orElseThrow(() -> new MatchNotFoundException(matchId));

        match.postponeMatch();
        Match postponedMatch = matchRepository.save(match);

        log.info("Match postponed with id: {}", postponedMatch.getId());
        return postponedMatch;
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<Match> getById(Long matchId) {
        log.info("Fetching match with id: {}", matchId);
        return matchRepository.findById(matchId);
    }

    @Override
    @Transactional(readOnly = true)
    public List<Match> getAllByTournamentId(Long tournamentId) {
        log.info("Fetching all matches for tournament with id: {}", tournamentId);
        return matchRepository.findAllByTournamentId(tournamentId);
    }

    @Override
    @Transactional(readOnly = true)
    public List<Match> getAllByTeamId(Long teamId) {
        log.info("Fetching all matches for team with id: {}", teamId);
        return matchRepository.findAllByTeamId(teamId);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<Match> getByTournamentIdWithFilters(Long tournamentId, MatchSearchCriteria criteria, PageRequest pageRequest) {
        log.info("Fetching matches for tournament {} with filters: {} and pagination: {}",
                tournamentId, criteria, pageRequest);
        return matchRepository.findByTournamentIdWithFilters(tournamentId, criteria, pageRequest);
    }

    @Override
    public void delete(Long matchId) {
        log.info("Deleting match with id: {}", matchId);

        Match match = matchRepository.findById(matchId)
                .orElseThrow(() -> new MatchNotFoundException(matchId));
        Team homeTeam = teamRepository.findById(match.getHomeTeamId())
                .orElseThrow(() -> new TeamNotFoundException(match.getHomeTeamId()));
        Team awayTeam = teamRepository.findById(match.getAwayTeamId())
                .orElseThrow(() -> new TeamNotFoundException(match.getAwayTeamId()));

        matchResultService.prepareMatchForDeletion(match, homeTeam, awayTeam);

        teamRepository.save(homeTeam);
        teamRepository.save(awayTeam);
        matchRepository.deleteById(match.getId());

        log.info("Match deleted with id: {}", matchId);
    }
}
