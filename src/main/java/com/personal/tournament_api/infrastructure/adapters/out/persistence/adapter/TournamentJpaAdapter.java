package com.personal.tournament_api.infrastructure.adapters.out.persistence.adapter;

import com.personal.tournament_api.domain.model.Tournament;
import com.personal.tournament_api.domain.ports.out.TournamentRepository;
import com.personal.tournament_api.infrastructure.adapters.out.persistence.repository.TournamentJpaRepository;
import com.personal.tournament_api.infrastructure.adapters.out.persistence.entity.TournamentEntity;
import com.personal.tournament_api.infrastructure.adapters.out.persistence.mapper.TournamentPersistenceMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Optional;

@Component
@RequiredArgsConstructor
public class TournamentJpaAdapter implements TournamentRepository {

    private final TournamentJpaRepository jpaRepository;
    private final TournamentPersistenceMapper mapper;

    @Override
    public Tournament save(Tournament tournament) {
        TournamentEntity entity = mapper.toEntity(tournament);
        TournamentEntity savedEntity = jpaRepository.save(entity);
        return mapper.toDomain(savedEntity);
    }

    @Override
    public Optional<Tournament> findById(Long id) {
        return jpaRepository.findById(id)
                .map(mapper::toDomain);
    }

    @Override
    public List<Tournament> findAll() {
        List<TournamentEntity> entities = jpaRepository.findAll();
        return mapper.toDomainList(entities);
    }

    @Override
    public void deleteById(Long id) {
        jpaRepository.deleteById(id);
    }

    @Override
    public boolean existsById(Long id) {
        return jpaRepository.existsById(id);
    }

    @Override
    public boolean existsByName(String name) {
        return jpaRepository.existsByName(name);
    }

    @Override
    public boolean existsByNameAndIdNot(String name, Long id) {
        return jpaRepository.existsByNameAndIdNot(name, id);
    }
}