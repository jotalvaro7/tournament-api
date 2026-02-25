package com.personal.tournament_api.tournament.application;

import com.personal.tournament_api.shared.domain.ports.DomainEventPublisher;
import com.personal.tournament_api.tournament.application.usecases.DeleteTournamentUseCase;
import com.personal.tournament_api.tournament.domain.events.TournamentDeletedEvent;
import com.personal.tournament_api.tournament.domain.exceptions.TournamentNotFoundException;
import com.personal.tournament_api.tournament.domain.model.Tournament;
import com.personal.tournament_api.tournament.domain.ports.TournamentRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
@RequiredArgsConstructor
public class DeleteTournamentService implements DeleteTournamentUseCase {

    private final TournamentRepository tournamentRepository;
    private final DomainEventPublisher domainEventPublisher;

    @Override
    public void delete(Long tournamentId) {
        Tournament tournament = tournamentRepository.findById(tournamentId)
                .orElseThrow(() -> new TournamentNotFoundException(tournamentId));
        tournament.validateIfCanBeDeleted();

        domainEventPublisher.publish(new TournamentDeletedEvent(tournamentId));

        tournamentRepository.deleteById(tournamentId);
    }
}
