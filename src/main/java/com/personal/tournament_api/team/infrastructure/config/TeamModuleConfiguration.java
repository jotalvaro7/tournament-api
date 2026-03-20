package com.personal.tournament_api.team.infrastructure.config;

import com.personal.tournament_api.shared.domain.ports.DomainEventPublisher;
import com.personal.tournament_api.team.application.*;
import com.personal.tournament_api.team.application.usecases.*;
import com.personal.tournament_api.team.domain.TeamDomainService;
import com.personal.tournament_api.team.domain.model.Team;
import com.personal.tournament_api.team.domain.ports.TeamMatchesPort;
import com.personal.tournament_api.team.domain.ports.TeamRepository;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.transaction.annotation.Transactional;

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
        return new CreateTeamAdapter(new CreateTeamService(teamRepository, teamDomainService));
    }

    @Bean
    public GetTeamUseCase getTeamUseCase(TeamRepository teamRepository) {
        return new GetTeamService(teamRepository);
    }

    @Bean
    public UpdateTeamUseCase updateTeamUseCase(TeamRepository teamRepository,
                                               TeamDomainService teamDomainService) {
        return new UpdateTeamAdapter(new UpdateTeamService(teamRepository, teamDomainService));
    }

    @Bean
    public DeleteTeamUseCase deleteTeamUseCase(TeamRepository teamRepository,
                                               TeamMatchesPort teamMatchesPort,
                                               DomainEventPublisher domainEventPublisher) {
        return new DeleteTeamAdapter(new DeleteTeamService(teamRepository, teamMatchesPort, domainEventPublisher));
    }

    // --- Transactional Adapters (infrastructure concern) ---

    static class CreateTeamAdapter implements CreateTeamUseCase {
        private final CreateTeamUseCase delegate;
        CreateTeamAdapter(CreateTeamUseCase delegate) { this.delegate = delegate; }

        @Override @Transactional
        public Team create(CreateTeamCommand command) { return delegate.create(command); }
    }

    static class UpdateTeamAdapter implements UpdateTeamUseCase {
        private final UpdateTeamUseCase delegate;
        UpdateTeamAdapter(UpdateTeamUseCase delegate) { this.delegate = delegate; }

        @Override @Transactional
        public Team update(UpdateTeamCommand command) { return delegate.update(command); }
    }

    static class DeleteTeamAdapter implements DeleteTeamUseCase {
        private final DeleteTeamUseCase delegate;
        DeleteTeamAdapter(DeleteTeamUseCase delegate) { this.delegate = delegate; }

        @Override @Transactional
        public void delete(Long teamId) { delegate.delete(teamId); }
    }
}