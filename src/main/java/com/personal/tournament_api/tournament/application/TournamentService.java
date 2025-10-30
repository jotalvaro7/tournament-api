package com.personal.tournament_api.tournament.application;

import com.personal.tournament_api.tournament.application.usecases.*;
import com.personal.tournament_api.tournament.domain.TournamentDomainService;
import com.personal.tournament_api.tournament.domain.model.Tournament;
import com.personal.tournament_api.tournament.domain.exceptions.TournamentNotFoundException;
import com.personal.tournament_api.tournament.domain.ports.TournamentRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
@Transactional
public class TournamentService implements
        CreateTournamentUseCase,
        UpdateTournamentUseCase,
        StartTournamentUseCase,
        endTournamentUseCase,
        CancelTournamentUseCase,
        DeleteTournamentUseCase,
        GetTournamentUseCase {

    private final TournamentRepository tournamentRepository;
    private final TournamentDomainService tournamentDomainService;

    @Override
    public Tournament create(CreateTournamentCommand command) {
        tournamentDomainService.validateUniqueName(command.name(), tournamentRepository);
        Tournament tournament = new Tournament(null, command.name(), command.description());
        return tournamentRepository.save(tournament);
    }

    @Override
    public Tournament update(UpdateTournamentCommand command) {
        Tournament tournament = tournamentRepository.findById(command.id())
                .orElseThrow(() -> new TournamentNotFoundException(command.id()));

        tournamentDomainService.validateUniqueNameForUpdate(command.name(), command.id(), tournamentRepository);

        tournament.updateDetails(command.name(), command.description());
        return tournamentRepository.save(tournament);
    }

    @Override
    public Tournament start(Long tournamentId) {
        Tournament tournament = tournamentRepository.findById(tournamentId)
                .orElseThrow(() -> new TournamentNotFoundException(tournamentId));
        tournament.startTournament();
        return tournamentRepository.save(tournament);
    }

    @Override
    public Tournament end(Long tournamentId) {
        Tournament tournament = tournamentRepository.findById(tournamentId)
                .orElseThrow(() -> new TournamentNotFoundException(tournamentId));
        tournament.endTournament();
        return tournamentRepository.save(tournament);
    }

    @Override
    public Tournament cancel(Long tournamentId) {
        Tournament tournament = tournamentRepository.findById(tournamentId)
                .orElseThrow(() -> new TournamentNotFoundException(tournamentId));
        tournament.cancelTournament();
        return tournamentRepository.save(tournament);
    }

    @Override
    public void delete(Long tournamentId) {
        Tournament tournament = tournamentRepository.findById(tournamentId)
                .orElseThrow(() -> new TournamentNotFoundException(tournamentId));
        tournament.validateIfCanBeDeleted();
        tournamentRepository.deleteById(tournamentId);
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<Tournament> getById(Long tournamentId) {
        return tournamentRepository.findById(tournamentId);
    }

    @Override
    @Transactional(readOnly = true)
    public List<Tournament> getAll() {
        return tournamentRepository.findAll();
    }
}