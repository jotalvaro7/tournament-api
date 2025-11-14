package com.personal.tournament_api.match.infrastructure.adapters.persistence;

import com.personal.tournament_api.match.domain.model.Match;
import com.personal.tournament_api.match.domain.model.MatchSearchCriteria;
import com.personal.tournament_api.match.domain.model.Page;
import com.personal.tournament_api.match.domain.model.PageRequest;
import com.personal.tournament_api.match.domain.ports.MatchRepository;
import com.personal.tournament_api.match.infrastructure.adapters.persistence.entity.MatchEntity;
import com.personal.tournament_api.match.infrastructure.adapters.persistence.mapper.MatchPersistenceMapper;
import com.personal.tournament_api.match.infrastructure.adapters.persistence.mapper.PaginationMapper;
import com.personal.tournament_api.match.infrastructure.adapters.persistence.repository.MatchJpaRepository;
import com.personal.tournament_api.match.infrastructure.adapters.persistence.repository.MatchSpecifications;
import lombok.RequiredArgsConstructor;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Optional;

@Component
@RequiredArgsConstructor
public class MatchJpaAdapter implements MatchRepository {

    private final MatchJpaRepository matchJpaRepository;
    private final MatchPersistenceMapper mapper;
    private final PaginationMapper paginationMapper;

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
    public Page<Match> findByTournamentIdWithFilters(Long tournamentId, MatchSearchCriteria criteria, PageRequest pageRequest) {
        Specification<MatchEntity> spec = MatchSpecifications.fromCriteria(tournamentId, criteria);
        org.springframework.data.domain.Pageable pageable = paginationMapper.toSpringPageable(pageRequest);

        org.springframework.data.domain.Page<MatchEntity> entityPage = matchJpaRepository.findAll(spec, pageable);

        List<Match> matches = mapper.toDomainList(entityPage.getContent());
        return paginationMapper.toDomainPage(entityPage, matches);
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
