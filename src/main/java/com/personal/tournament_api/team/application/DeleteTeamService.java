package com.personal.tournament_api.team.application;

import com.personal.tournament_api.match.domain.model.Match;
import com.personal.tournament_api.match.domain.ports.MatchRepository;
import com.personal.tournament_api.shared.domain.ports.DomainEventPublisher;
import com.personal.tournament_api.team.application.usecases.DeleteTeamUseCase;
import com.personal.tournament_api.team.domain.events.TeamDeletedEvent;
import com.personal.tournament_api.team.domain.exceptions.TeamNotFoundException;
import com.personal.tournament_api.team.domain.model.Team;
import com.personal.tournament_api.team.domain.ports.TeamRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional
@Slf4j
@RequiredArgsConstructor
public class DeleteTeamService implements DeleteTeamUseCase {

    private final TeamRepository teamRepository;
    private final MatchRepository matchRepository;
    private final DomainEventPublisher domainEventPublisher;

    @Override
    public void delete(Long teamId) {
        log.info("Deleting team with id: {}", teamId);
        Team team = teamRepository.findById(teamId)
                .orElseThrow(() -> new TeamNotFoundException(teamId));

        List<Match> associatedMatches = matchRepository.findAllByTeamId(teamId);
        team.validateCanBeDeleted(associatedMatches.size());

        domainEventPublisher.publish(new TeamDeletedEvent(teamId));

        teamRepository.deleteById(team.getId());
        log.info("Team deleted with id: {}", teamId);
    }
}
