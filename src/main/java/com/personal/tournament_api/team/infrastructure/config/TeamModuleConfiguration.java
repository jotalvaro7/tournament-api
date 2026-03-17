package com.personal.tournament_api.team.infrastructure.config;

import com.personal.tournament_api.shared.domain.ports.DomainEventPublisher;
import com.personal.tournament_api.team.application.*;
import com.personal.tournament_api.team.application.usecases.*;
import com.personal.tournament_api.team.domain.TeamDomainService;
import com.personal.tournament_api.team.domain.ports.TeamMatchesPort;
import com.personal.tournament_api.team.domain.ports.TeamRepository;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class TeamModuleConfiguration {

    // --- Domain Services ---

    @Bean
    public TeamDomainService teamDomainService() {
        return new TeamDomainService();
    }

    // --- Application Use Cases ---

    @Bean
    public CreateTeamUseCase createTeamUseCase(TeamRepository teamRepository,
                                               TeamDomainService teamDomainService) {
        return new CreateTeamService(teamRepository, teamDomainService);
    }

    @Bean
    public GetTeamUseCase getTeamUseCase(TeamRepository teamRepository) {
        return new GetTeamService(teamRepository);
    }

    @Bean
    public UpdateTeamUseCase updateTeamUseCase(TeamRepository teamRepository,
                                               TeamDomainService teamDomainService) {
        return new UpdateTeamService(teamRepository, teamDomainService);
    }

    @Bean
    public DeleteTeamUseCase deleteTeamUseCase(TeamRepository teamRepository,
                                               TeamMatchesPort teamMatchesPort,
                                               DomainEventPublisher domainEventPublisher) {
        return new DeleteTeamService(teamRepository, teamMatchesPort, domainEventPublisher);
    }
}
