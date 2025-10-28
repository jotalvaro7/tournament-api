package com.personal.tournament_api.application.services;

import com.personal.tournament_api.application.ports.in.tournament.*;
import com.personal.tournament_api.domain.model.Tournament;
import com.personal.tournament_api.domain.ports.out.TournamentRepository;
import com.personal.tournament_api.domain.services.TournamentDomainService;
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
                .orElseThrow(() -> new RuntimeException("Tournament not found"));

        tournamentDomainService.validateUniqueNameForUpdate(command.name(), command.id(), tournamentRepository);

        tournament.updateDetails(command.name(), command.description());
        return tournamentRepository.save(tournament);
    }

    @Override
    public Tournament start(Long tournamentId) {
        Tournament tournament = tournamentRepository.findById(tournamentId)
                .orElseThrow(() -> new RuntimeException("Tournament not found"));
        tournament.startTournament();
        return tournamentRepository.save(tournament);
    }

    @Override
    public Tournament end(Long tournamentId) {
        Tournament tournament = tournamentRepository.findById(tournamentId)
                .orElseThrow(() -> new RuntimeException("Tournament not found"));
        tournament.endTournament();
        return tournamentRepository.save(tournament);
    }

    @Override
    public Tournament cancel(Long tournamentId) {
        Tournament tournament = tournamentRepository.findById(tournamentId)
                .orElseThrow(() -> new RuntimeException("Tournament not found"));
        tournament.cancelTournament();
        return tournamentRepository.save(tournament);
    }

    @Override
    public void delete(Long tournamentId) {
        Tournament tournament = tournamentRepository.findById(tournamentId)
                .orElseThrow(() -> new RuntimeException("Tournament not found"));
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