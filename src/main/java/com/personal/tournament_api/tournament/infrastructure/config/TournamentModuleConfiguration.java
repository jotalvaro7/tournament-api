package com.personal.tournament_api.tournament.infrastructure.config;

import com.personal.tournament_api.shared.domain.ports.DomainEventPublisher;
import com.personal.tournament_api.tournament.application.*;
import com.personal.tournament_api.tournament.application.usecases.*;
import com.personal.tournament_api.tournament.domain.TournamentDomainService;
import com.personal.tournament_api.tournament.domain.ports.TournamentRepository;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class TournamentModuleConfiguration {

    // --- Domain Services ---

    @Bean
    public TournamentDomainService tournamentDomainService() {
        return new TournamentDomainService();
    }

    // --- Application Use Cases ---

    @Bean
    public CreateTournamentUseCase createTournamentUseCase(TournamentRepository tournamentRepository,
                                                           TournamentDomainService tournamentDomainService) {
        return new CreateTournamentService(tournamentRepository, tournamentDomainService);
    }

    @Bean
    public GetTournamentUseCase getTournamentUseCase(TournamentRepository tournamentRepository) {
        return new GetTournamentService(tournamentRepository);
    }

    @Bean
    public UpdateTournamentUseCase updateTournamentUseCase(TournamentRepository tournamentRepository,
                                                           TournamentDomainService tournamentDomainService) {
        return new UpdateTournamentService(tournamentRepository, tournamentDomainService);
    }

    @Bean
    public DeleteTournamentUseCase deleteTournamentUseCase(TournamentRepository tournamentRepository,
                                                           DomainEventPublisher domainEventPublisher) {
        return new DeleteTournamentService(tournamentRepository, domainEventPublisher);
    }

    @Bean
    public StartTournamentUseCase startTournamentUseCase(TournamentRepository tournamentRepository) {
        return new StartTournamentService(tournamentRepository);
    }

    @Bean
    public EndTournamentUseCase endTournamentUseCase(TournamentRepository tournamentRepository) {
        return new EndTournamentService(tournamentRepository);
    }

    @Bean
    public CancelTournamentUseCase cancelTournamentUseCase(TournamentRepository tournamentRepository) {
        return new CancelTournamentService(tournamentRepository);
    }
}
