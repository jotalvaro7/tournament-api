package com.personal.tournament_api.match.application;

import com.personal.tournament_api.match.application.usecases.GetMatchUseCase;
import com.personal.tournament_api.match.domain.model.Match;
import com.personal.tournament_api.match.domain.model.MatchSearchCriteria;
import com.personal.tournament_api.match.domain.model.Page;
import com.personal.tournament_api.match.domain.model.PageRequest;
import com.personal.tournament_api.match.domain.ports.MatchRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;
import java.util.Optional;

public class GetMatchService implements GetMatchUseCase {

    private static final Logger log = LoggerFactory.getLogger(GetMatchService.class);

    private final MatchRepository matchRepository;

    public GetMatchService(MatchRepository matchRepository) {
        this.matchRepository = matchRepository;
    }

    @Override
    public Optional<Match> getById(Long matchId) {
        log.info("Fetching match with id: {}", matchId);
        return matchRepository.findById(matchId);
    }

    @Override
    public List<Match> getAllByTournamentId(Long tournamentId) {
        log.info("Fetching all matches for tournament with id: {}", tournamentId);
        return matchRepository.findAllByTournamentId(tournamentId);
    }

    @Override
    public List<Match> getAllByTeamId(Long teamId) {
        log.info("Fetching all matches for team with id: {}", teamId);
        return matchRepository.findAllByTeamId(teamId);
    }

    @Override
    public Page<Match> getByTournamentIdWithFilters(Long tournamentId, MatchSearchCriteria criteria, PageRequest pageRequest) {
        log.info("Fetching matches for tournament {} with filters: {} and pagination: {}",
                tournamentId, criteria, pageRequest);
        return matchRepository.findByTournamentIdWithFilters(tournamentId, criteria, pageRequest);
    }
}
