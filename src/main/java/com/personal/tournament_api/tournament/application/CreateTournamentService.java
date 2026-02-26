package com.personal.tournament_api.tournament.application;

import com.personal.tournament_api.tournament.application.usecases.CreateTournamentUseCase;
import com.personal.tournament_api.tournament.domain.TournamentDomainService;
import com.personal.tournament_api.tournament.domain.model.Tournament;
import com.personal.tournament_api.tournament.domain.ports.TournamentRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
@Slf4j
@RequiredArgsConstructor
public class CreateTournamentService implements CreateTournamentUseCase {

    private final TournamentRepository tournamentRepository;
    private final TournamentDomainService tournamentDomainService;

    @Override
    public Tournament create(CreateTournamentCommand command) {
        log.info("Creating tournament with name: {}", command.name());
        tournamentDomainService.validateUniqueName(command.name(), tournamentRepository);
        Tournament tournament = new Tournament(null, command.name(), command.description());
        Tournament saved = tournamentRepository.save(tournament);
        log.info("Tournament created with id: {}", saved.getId());
        return saved;
    }
}
