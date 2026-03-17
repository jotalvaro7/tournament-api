package com.personal.tournament_api.tournament.application;

import com.personal.tournament_api.tournament.application.usecases.UpdateTournamentUseCase;
import com.personal.tournament_api.tournament.domain.TournamentDomainService;
import com.personal.tournament_api.tournament.domain.exceptions.TournamentNotFoundException;
import com.personal.tournament_api.tournament.domain.model.Tournament;
import com.personal.tournament_api.tournament.domain.ports.TournamentRepository;
import jakarta.transaction.Transactional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@Transactional
public class UpdateTournamentService implements UpdateTournamentUseCase {

    private static final Logger log = LoggerFactory.getLogger(UpdateTournamentService.class);

    private final TournamentRepository tournamentRepository;
    private final TournamentDomainService tournamentDomainService;

    public UpdateTournamentService(TournamentRepository tournamentRepository,
                                   TournamentDomainService tournamentDomainService) {
        this.tournamentRepository = tournamentRepository;
        this.tournamentDomainService = tournamentDomainService;
    }

    @Override
    public Tournament update(UpdateTournamentCommand command) {
        log.info("Updating tournament with id: {}", command.id());
        Tournament tournament = tournamentRepository.findById(command.id())
                .orElseThrow(() -> new TournamentNotFoundException(command.id()));

        tournamentDomainService.validateUniqueNameForUpdate(command.name(), command.id(), tournamentRepository);

        tournament.updateDetails(command.name(), command.description());
        Tournament updated = tournamentRepository.save(tournament);
        log.info("Tournament updated with id: {}", updated.getId());
        return updated;
    }
}
