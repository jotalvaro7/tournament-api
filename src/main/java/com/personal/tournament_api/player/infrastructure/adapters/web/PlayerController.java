package com.personal.tournament_api.player.infrastructure.adapters.web;

import com.personal.tournament_api.player.application.usecases.CreatePlayerUseCase;
import com.personal.tournament_api.player.domain.model.Player;
import com.personal.tournament_api.player.infrastructure.adapters.web.dto.PlayerRequestDTO;
import com.personal.tournament_api.player.infrastructure.adapters.web.dto.PlayerResponseDTO;
import com.personal.tournament_api.player.infrastructure.adapters.web.mapper.PlayerMapper;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/tournaments/{tournamentId}/teams/{teamId}/players")
@RequiredArgsConstructor
public class PlayerController {

    private final CreatePlayerUseCase createPlayerUseCase;
    private final PlayerMapper playerMapper;

    @PostMapping
    public ResponseEntity<PlayerResponseDTO> create(@PathVariable Long tournamentId,
                                                    @PathVariable Long teamId,
                                                    @Valid @RequestBody PlayerRequestDTO request) {

        var command = playerMapper.toCreateCommand(tournamentId, teamId, request);
        Player player = createPlayerUseCase.execute(command);
        return ResponseEntity.status(HttpStatus.CREATED).body(playerMapper.toResponse(player));
    }
}
