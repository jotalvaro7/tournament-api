package com.personal.tournament_api.match.infrastructure.adapters.web;

import com.personal.tournament_api.match.application.usecases.*;
import com.personal.tournament_api.match.domain.model.*;
import com.personal.tournament_api.match.infrastructure.adapters.web.dto.FinishMatchRequestDTO;
import com.personal.tournament_api.match.infrastructure.adapters.web.dto.MatchRequestDTO;
import com.personal.tournament_api.match.infrastructure.adapters.web.dto.MatchResponseDTO;
import com.personal.tournament_api.match.infrastructure.adapters.web.dto.PageResponseDTO;
import com.personal.tournament_api.match.infrastructure.adapters.web.mapper.MatchFilterBuilder;
import com.personal.tournament_api.match.infrastructure.adapters.web.mapper.MatchMapper;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
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
    private final MatchFilterBuilder matchFilterBuilder;

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
    public ResponseEntity<PageResponseDTO<MatchResponseDTO>> getAllByTournamentId(
            @PathVariable Long tournamentId,
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate specificDate,
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate dateFrom,
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate dateTo,
            @RequestParam(required = false) MatchStatus status,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "15") int size,
            @RequestParam(defaultValue = "matchDate") String sortBy,
            @RequestParam(defaultValue = "ASC") String direction) {

        MatchSearchCriteria criteria = matchFilterBuilder.buildSearchCriteria(specificDate, dateFrom, dateTo, status);
        PageRequest pageRequest = matchFilterBuilder.buildPageRequest(page, size, sortBy, direction);

        Page<Match> matchPage = getMatchUseCase.getByTournamentIdWithFilters(tournamentId, criteria, pageRequest);

        List<MatchResponseDTO> matchDTOs = matchMapper.toResponseList(matchPage.getContent());
        Page<MatchResponseDTO> dtoPage = new Page<>(matchDTOs, matchPage.getPage(), matchPage.getSize(), matchPage.getTotalElements());

        return ResponseEntity.ok(PageResponseDTO.from(dtoPage));
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
