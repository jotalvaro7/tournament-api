package com.personal.tournament_api.tournament.application;

import com.personal.tournament_api.tournament.application.usecases.CreateTournamentUseCase;
import com.personal.tournament_api.tournament.domain.TournamentDomainService;
import com.personal.tournament_api.tournament.domain.model.Tournament;
import com.personal.tournament_api.tournament.domain.ports.TournamentRepository;
import jakarta.transaction.Transactional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@Transactional
public class CreateTournamentService implements CreateTournamentUseCase {

    private static final Logger log = LoggerFactory.getLogger(CreateTournamentService.class);

    private final TournamentRepository tournamentRepository;
    private final TournamentDomainService tournamentDomainService;

    public CreateTournamentService(TournamentRepository tournamentRepository,
                                   TournamentDomainService tournamentDomainService) {
        this.tournamentRepository = tournamentRepository;
        this.tournamentDomainService = tournamentDomainService;
    }

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
