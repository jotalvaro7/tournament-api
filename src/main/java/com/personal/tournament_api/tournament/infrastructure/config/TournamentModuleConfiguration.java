package com.personal.tournament_api.tournament.infrastructure.config;

import com.personal.tournament_api.shared.domain.ports.DomainEventPublisher;
import com.personal.tournament_api.tournament.application.*;
import com.personal.tournament_api.tournament.application.usecases.*;
import com.personal.tournament_api.tournament.domain.TournamentDomainService;
import com.personal.tournament_api.tournament.domain.model.Tournament;
import com.personal.tournament_api.tournament.domain.ports.TournamentRepository;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.transaction.annotation.Transactional;

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
        return new CreateTournamentAdapter(new CreateTournamentService(tournamentRepository, tournamentDomainService));
    }

    @Bean
    public GetTournamentUseCase getTournamentUseCase(TournamentRepository tournamentRepository) {
        return new GetTournamentService(tournamentRepository);
    }

    @Bean
    public UpdateTournamentUseCase updateTournamentUseCase(TournamentRepository tournamentRepository,
                                                           TournamentDomainService tournamentDomainService) {
        return new UpdateTournamentAdapter(new UpdateTournamentService(tournamentRepository, tournamentDomainService));
    }

    @Bean
    public DeleteTournamentUseCase deleteTournamentUseCase(TournamentRepository tournamentRepository,
                                                           DomainEventPublisher domainEventPublisher) {
        return new DeleteTournamentAdapter(new DeleteTournamentService(tournamentRepository, domainEventPublisher));
    }

    @Bean
    public StartTournamentUseCase startTournamentUseCase(TournamentRepository tournamentRepository) {
        return new StartTournamentAdapter(new StartTournamentService(tournamentRepository));
    }

    @Bean
    public EndTournamentUseCase endTournamentUseCase(TournamentRepository tournamentRepository) {
        return new EndTournamentAdapter(new EndTournamentService(tournamentRepository));
    }

    @Bean
    public CancelTournamentUseCase cancelTournamentUseCase(TournamentRepository tournamentRepository) {
        return new CancelTournamentAdapter(new CancelTournamentService(tournamentRepository));
    }

    // --- Transactional Adapters (infrastructure concern) ---

    static class CreateTournamentAdapter implements CreateTournamentUseCase {
        private final CreateTournamentUseCase delegate;
        CreateTournamentAdapter(CreateTournamentUseCase delegate) { this.delegate = delegate; }

        @Override @Transactional
        public Tournament create(CreateTournamentCommand command) { return delegate.create(command); }
    }

    static class UpdateTournamentAdapter implements UpdateTournamentUseCase {
        private final UpdateTournamentUseCase delegate;
        UpdateTournamentAdapter(UpdateTournamentUseCase delegate) { this.delegate = delegate; }

        @Override @Transactional
        public Tournament update(UpdateTournamentCommand command) { return delegate.update(command); }
    }

    static class DeleteTournamentAdapter implements DeleteTournamentUseCase {
        private final DeleteTournamentUseCase delegate;
        DeleteTournamentAdapter(DeleteTournamentUseCase delegate) { this.delegate = delegate; }

        @Override @Transactional
        public void delete(Long tournamentId) { delegate.delete(tournamentId); }
    }

    static class StartTournamentAdapter implements StartTournamentUseCase {
        private final StartTournamentUseCase delegate;
        StartTournamentAdapter(StartTournamentUseCase delegate) { this.delegate = delegate; }

        @Override @Transactional
        public Tournament start(Long tournamentId) { return delegate.start(tournamentId); }
    }

    static class EndTournamentAdapter implements EndTournamentUseCase {
        private final EndTournamentUseCase delegate;
        EndTournamentAdapter(EndTournamentUseCase delegate) { this.delegate = delegate; }

        @Override @Transactional
        public Tournament end(Long tournamentId) { return delegate.end(tournamentId); }
    }

    static class CancelTournamentAdapter implements CancelTournamentUseCase {
        private final CancelTournamentUseCase delegate;
        CancelTournamentAdapter(CancelTournamentUseCase delegate) { this.delegate = delegate; }

        @Override @Transactional
        public Tournament cancel(Long tournamentId) { return delegate.cancel(tournamentId); }
    }
}