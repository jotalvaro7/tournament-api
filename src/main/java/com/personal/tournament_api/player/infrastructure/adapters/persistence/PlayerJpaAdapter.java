package com.personal.tournament_api.player.infrastructure.adapters.persistence;

import com.personal.tournament_api.player.domain.model.Player;
import com.personal.tournament_api.player.domain.ports.PlayerRepository;
import com.personal.tournament_api.player.infrastructure.adapters.persistence.mapper.PlayerPersistenceMapper;
import com.personal.tournament_api.player.infrastructure.adapters.persistence.repository.PlayerJpaRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class PlayerJpaAdapter implements PlayerRepository {

    private final PlayerJpaRepository playerJpaRepository;
    private final PlayerPersistenceMapper mapper;

    @Override
    public Player save(Player player) {
        var playerEntity = mapper.toEntity(player);
        var savedEntity = playerJpaRepository.save(playerEntity);
        return mapper.toDomain(savedEntity);
    }

    @Override
    public boolean existsByIdentificationNumber(String identificationNumber) {
        return playerJpaRepository.existsByIdentificationNumber(identificationNumber);
    }

}
