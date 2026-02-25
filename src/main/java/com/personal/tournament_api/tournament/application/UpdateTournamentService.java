package com.personal.tournament_api.tournament.application;

import com.personal.tournament_api.tournament.application.usecases.UpdateTournamentUseCase;
import com.personal.tournament_api.tournament.domain.TournamentDomainService;
import com.personal.tournament_api.tournament.domain.exceptions.TournamentNotFoundException;
import com.personal.tournament_api.tournament.domain.model.Tournament;
import com.personal.tournament_api.tournament.domain.ports.TournamentRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
@RequiredArgsConstructor
public class UpdateTournamentService implements UpdateTournamentUseCase {

    private final TournamentRepository tournamentRepository;
    private final TournamentDomainService tournamentDomainService;

    @Override
    public Tournament update(UpdateTournamentCommand command) {
        Tournament tournament = tournamentRepository.findById(command.id())
                .orElseThrow(() -> new TournamentNotFoundException(command.id()));

        tournamentDomainService.validateUniqueNameForUpdate(command.name(), command.id(), tournamentRepository);

        tournament.updateDetails(command.name(), command.description());
        return tournamentRepository.save(tournament);
    }
}
