package com.personal.tournament_api.tournament.application;

import com.personal.tournament_api.tournament.application.usecases.CreateTournamentUseCase;
import com.personal.tournament_api.tournament.domain.TournamentDomainService;
import com.personal.tournament_api.tournament.domain.model.Tournament;
import com.personal.tournament_api.tournament.domain.ports.TournamentRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
@RequiredArgsConstructor
public class CreateTournamentService implements CreateTournamentUseCase {

    private final TournamentRepository tournamentRepository;
    private final TournamentDomainService tournamentDomainService;

    @Override
    public Tournament create(CreateTournamentCommand command) {
        tournamentDomainService.validateUniqueName(command.name(), tournamentRepository);
        Tournament tournament = new Tournament(null, command.name(), command.description());
        return tournamentRepository.save(tournament);
    }
}
