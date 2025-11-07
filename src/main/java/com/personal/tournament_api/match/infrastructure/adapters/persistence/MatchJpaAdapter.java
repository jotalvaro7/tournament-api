package com.personal.tournament_api.match.infrastructure.adapters.persistence;

import com.personal.tournament_api.match.domain.model.Match;
import com.personal.tournament_api.match.domain.ports.MatchRepository;
import com.personal.tournament_api.match.infrastructure.adapters.persistence.entity.MatchEntity;
import com.personal.tournament_api.match.infrastructure.adapters.persistence.mapper.MatchPersistenceMapper;
import com.personal.tournament_api.match.infrastructure.adapters.persistence.repository.MatchJpaRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Optional;

@Component
@RequiredArgsConstructor
public class MatchJpaAdapter implements MatchRepository {

    private final MatchJpaRepository matchJpaRepository;
    private final MatchPersistenceMapper mapper;

    @Override
    public Match save(Match match) {
        MatchEntity entity = mapper.toEntity(match);
        MatchEntity savedEntity = matchJpaRepository.save(entity);
        return mapper.toDomain(savedEntity);
    }

    @Override
    public Optional<Match> findById(Long id) {
        return matchJpaRepository.findById(id)
                .map(mapper::toDomain);
    }

    @Override
    public List<Match> findAllByTournamentId(Long tournamentId) {
        List<MatchEntity> entities = matchJpaRepository.findAllByTournamentId(tournamentId);
        return mapper.toDomainList(entities);
    }

    @Override
    public List<Match> findAllByTeamId(Long teamId) {
        List<MatchEntity> entities = matchJpaRepository.findAllByTeamId(teamId);
        return mapper.toDomainList(entities);
    }

    @Override
    public void deleteById(Long id) {
        matchJpaRepository.deleteById(id);
    }

    @Override
    public void deleteByTournamentId(Long tournamentId) {
        matchJpaRepository.deleteByTournamentId(tournamentId);
    }
}
