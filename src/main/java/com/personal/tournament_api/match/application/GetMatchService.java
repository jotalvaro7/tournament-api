package com.personal.tournament_api.match.application;

import com.personal.tournament_api.match.application.usecases.GetMatchUseCase;
import com.personal.tournament_api.match.domain.model.Match;
import com.personal.tournament_api.match.domain.model.MatchSearchCriteria;
import com.personal.tournament_api.match.domain.model.Page;
import com.personal.tournament_api.match.domain.model.PageRequest;
import com.personal.tournament_api.match.domain.ports.MatchRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
@Transactional(readOnly = true)
@Slf4j
@RequiredArgsConstructor
public class GetMatchService implements GetMatchUseCase {

    private final MatchRepository matchRepository;

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
