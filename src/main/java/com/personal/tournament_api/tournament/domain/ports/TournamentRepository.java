package com.personal.tournament_api.tournament.domain.ports;

import com.personal.tournament_api.tournament.domain.model.Tournament;

import java.util.List;
import java.util.Optional;

public interface TournamentRepository {

    Tournament save(Tournament tournament);

    Optional<Tournament> findById(Long id);

    List<Tournament> findAll();

    void deleteById(Long id);

    boolean existsById(Long id);

    boolean existsByName(String name);

    boolean existsByNameAndIdNot(String name, Long id);
}