package com.personal.tournament_api.infrastructure.adapters.in.web;

import com.personal.tournament_api.application.ports.in.tournament.*;
import com.personal.tournament_api.domain.model.Tournament;
import com.personal.tournament_api.infrastructure.adapters.in.web.dto.TournamentRequest;
import com.personal.tournament_api.infrastructure.adapters.in.web.dto.TournamentResponse;
import com.personal.tournament_api.infrastructure.adapters.in.web.mapper.TournamentMapper;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/tournaments")
@RequiredArgsConstructor
public class TournamentController {

    private final CreateTournamentUseCase createTournamentUseCase;
    private final UpdateTournamentUseCase updateTournamentUseCase;
    private final GetTournamentUseCase getTournamentUseCase;
    private final StartTournamentUseCase startTournamentUseCase;
    private final endTournamentUseCase endTournamentUseCase;
    private final CancelTournamentUseCase cancelTournamentUseCase;
    private final DeleteTournamentUseCase deleteTournamentUseCase;
    private final TournamentMapper tournamentMapper;

    @PostMapping
    public ResponseEntity<TournamentResponse> create(@Valid @RequestBody TournamentRequest request) {
        Tournament tournament = createTournamentUseCase.create(tournamentMapper.toCreateCommand(request));
        return ResponseEntity.status(HttpStatus.CREATED).body(tournamentMapper.toResponse(tournament));
    }

    @GetMapping
    public ResponseEntity<List<TournamentResponse>> getAll() {
        List<Tournament> tournaments = getTournamentUseCase.getAll();
        return ResponseEntity.ok(tournamentMapper.toResponseList(tournaments));
    }

    @GetMapping("/{id}")
    public ResponseEntity<TournamentResponse> getById(@PathVariable Long id) {
        return getTournamentUseCase.getById(id)
                .map(tournament -> ResponseEntity.ok(tournamentMapper.toResponse(tournament)))
                .orElse(ResponseEntity.notFound().build());
    }

    @PutMapping("/{id}")
    public ResponseEntity<TournamentResponse> update(
            @PathVariable Long id,
            @Valid @RequestBody TournamentRequest request) {
        Tournament tournament = updateTournamentUseCase.update(tournamentMapper.toUpdateCommand(id, request));
        return ResponseEntity.ok(tournamentMapper.toResponse(tournament));
    }

    @PatchMapping("/{id}/start")
    public ResponseEntity<TournamentResponse> start(@PathVariable Long id) {
        Tournament tournament = startTournamentUseCase.start(id);
        return ResponseEntity.ok(tournamentMapper.toResponse(tournament));
    }

    @PatchMapping("/{id}/end")
    public ResponseEntity<TournamentResponse> end(@PathVariable Long id) {
        Tournament tournament = endTournamentUseCase.end(id);
        return ResponseEntity.ok(tournamentMapper.toResponse(tournament));
    }

    @PatchMapping("/{id}/cancel")
    public ResponseEntity<TournamentResponse> cancel(@PathVariable Long id) {
        Tournament tournament = cancelTournamentUseCase.cancel(id);
        return ResponseEntity.ok(tournamentMapper.toResponse(tournament));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        deleteTournamentUseCase.delete(id);
        return ResponseEntity.noContent().build();
    }
}