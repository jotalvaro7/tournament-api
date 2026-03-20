package com.personal.tournament_api.tournament.application;

import com.personal.tournament_api.shared.domain.ports.DomainEventPublisher;
import com.personal.tournament_api.tournament.application.usecases.DeleteTournamentUseCase;
import com.personal.tournament_api.tournament.domain.events.TournamentDeletedEvent;
import com.personal.tournament_api.tournament.domain.exceptions.TournamentNotFoundException;
import com.personal.tournament_api.tournament.domain.model.Tournament;
import com.personal.tournament_api.tournament.domain.ports.TournamentRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class DeleteTournamentService implements DeleteTournamentUseCase {

    private static final Logger log = LoggerFactory.getLogger(DeleteTournamentService.class);

    private final TournamentRepository tournamentRepository;
    private final DomainEventPublisher domainEventPublisher;

    public DeleteTournamentService(TournamentRepository tournamentRepository,
                                   DomainEventPublisher domainEventPublisher) {
        this.tournamentRepository = tournamentRepository;
        this.domainEventPublisher = domainEventPublisher;
    }

    @Override
    public void delete(Long tournamentId) {
        log.info("Deleting tournament with id: {}", tournamentId);
        Tournament tournament = tournamentRepository.findById(tournamentId)
                .orElseThrow(() -> new TournamentNotFoundException(tournamentId));
        tournament.validateIfCanBeDeleted();

        domainEventPublisher.publish(new TournamentDeletedEvent(tournamentId));

        tournamentRepository.deleteById(tournamentId);
        log.info("Tournament deleted with id: {}", tournamentId);
    }
}
