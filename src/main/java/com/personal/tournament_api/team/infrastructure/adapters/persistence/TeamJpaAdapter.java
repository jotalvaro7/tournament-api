package com.personal.tournament_api.team.infrastructure.adapters.persistence;

import com.personal.tournament_api.team.domain.model.Team;
import com.personal.tournament_api.team.domain.ports.TeamRepository;
import com.personal.tournament_api.team.infrastructure.adapters.persistence.entity.TeamEntity;
import com.personal.tournament_api.team.infrastructure.adapters.persistence.mapper.TeamPersistenceMapper;
import com.personal.tournament_api.team.infrastructure.adapters.persistence.repository.TeamJpaRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Optional;

@Component
@RequiredArgsConstructor
public class TeamJpaAdapter implements TeamRepository {

    private final TeamJpaRepository teamJpaRepository;
    private final TeamPersistenceMapper mapper;

    @Override
    public Team save(Team team) {
        TeamEntity entity = mapper.toEntity(team);
        TeamEntity savedEntity = teamJpaRepository.save(entity);
        return mapper.toDomain(savedEntity);
    }

    @Override
    public Optional<Team> findById(Long id) {
        return teamJpaRepository.findById(id)
                .map(mapper::toDomain);
    }

    @Override
    public List<Team> findAll() {
        List<TeamEntity> entities = teamJpaRepository.findAll();
        return mapper.toDomainList(entities);
    }

    @Override
    public void deleteById(Long id) {
        teamJpaRepository.deleteById(id);
    }

    @Override
    public boolean existsByName(String name) {
        return teamJpaRepository.existsByName(name);
    }

    @Override
    public boolean existsByNameAndIdNot(String name, Long id) {
       return teamJpaRepository.existsByNameAndIdNot(name, id);
    }
}
