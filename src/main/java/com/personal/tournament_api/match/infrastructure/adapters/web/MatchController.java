package com.personal.tournament_api.match.infrastructure.adapters.web;

import com.personal.tournament_api.match.application.usecases.*;
import com.personal.tournament_api.match.domain.model.Match;
import com.personal.tournament_api.match.infrastructure.adapters.web.dto.FinishMatchRequestDTO;
import com.personal.tournament_api.match.infrastructure.adapters.web.dto.MatchRequestDTO;
import com.personal.tournament_api.match.infrastructure.adapters.web.dto.MatchResponseDTO;
import com.personal.tournament_api.match.infrastructure.adapters.web.mapper.MatchMapper;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/tournaments/{tournamentId}/matches")
@RequiredArgsConstructor
public class MatchController {

    private final CreateMatchUseCase createMatchUseCase;
    private final UpdateMatchUseCase updateMatchUseCase;
    private final FinishMatchUseCase finishMatchUseCase;
    private final GetMatchUseCase getMatchUseCase;
    private final DeleteMatchUseCase deleteMatchUseCase;
    private final PostponeMatchUseCase postponeMatchUseCase;
    private final MatchMapper matchMapper;

    @PostMapping
    public ResponseEntity<MatchResponseDTO> create(@PathVariable Long tournamentId,
                                                    @Valid @RequestBody MatchRequestDTO request) {
        Match match = createMatchUseCase.create(matchMapper.toCreateCommand(tournamentId, request));
        return ResponseEntity.status(HttpStatus.CREATED).body(matchMapper.toResponse(match));
    }

    @GetMapping("/{matchId}")
    public ResponseEntity<MatchResponseDTO> getById(@PathVariable Long matchId) {
        return getMatchUseCase.getById(matchId)
                .map(match -> ResponseEntity.ok(matchMapper.toResponse(match)))
                .orElse(ResponseEntity.notFound().build());
    }

    @GetMapping
    public ResponseEntity<List<MatchResponseDTO>> getAllByTournamentId(@PathVariable Long tournamentId) {
        List<Match> matches = getMatchUseCase.getAllByTournamentId(tournamentId);
        return ResponseEntity.ok(matchMapper.toResponseList(matches));
    }

    @PutMapping("/{matchId}")
    public ResponseEntity<MatchResponseDTO> update(@PathVariable Long matchId,
                                                    @Valid @RequestBody MatchRequestDTO request) {
        Match match = updateMatchUseCase.update(matchMapper.toUpdateCommand(matchId, request));
        return ResponseEntity.ok(matchMapper.toResponse(match));
    }

    @PutMapping("/{matchId}/result")
    public ResponseEntity<MatchResponseDTO> setResult(@PathVariable Long matchId,
                                                       @Valid @RequestBody FinishMatchRequestDTO request) {
        Match match = finishMatchUseCase.finishMatch(matchMapper.toFinishCommand(matchId, request));
        return ResponseEntity.ok(matchMapper.toResponse(match));
    }

    @PostMapping("/{matchId}/postpone")
    public ResponseEntity<MatchResponseDTO> postponeMatch(@PathVariable Long matchId) {
        Match match = postponeMatchUseCase.postponeMatch(matchId);
        return ResponseEntity.ok(matchMapper.toResponse(match));
    }

    @DeleteMapping("/{matchId}")
    public ResponseEntity<Void> delete(@PathVariable Long matchId) {
        deleteMatchUseCase.delete(matchId);
        return ResponseEntity.noContent().build();
    }
}
