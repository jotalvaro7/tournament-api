package com.personal.tournament_api.team.domain.ports;

import com.personal.tournament_api.team.domain.model.Team;

import java.util.List;
import java.util.Optional;

public interface TeamRepository {

    Team save(Team team);

    Optional<Team> findById(Long id);

    List<Team> findAllByOrderByNameAsc();

    void deleteById(Long id);

    boolean existsByName(String name);

    boolean existsByNameAndIdNot(String name, Long id);

}
