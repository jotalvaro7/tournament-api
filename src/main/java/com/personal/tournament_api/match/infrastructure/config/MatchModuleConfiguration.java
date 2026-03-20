package com.personal.tournament_api.match.infrastructure.config;

import com.personal.tournament_api.match.application.*;
import com.personal.tournament_api.match.application.usecases.*;
import com.personal.tournament_api.match.domain.model.Match;
import com.personal.tournament_api.match.domain.ports.MatchRepository;
import com.personal.tournament_api.match.domain.ports.MatchTeamPort;
import com.personal.tournament_api.match.domain.services.MatchResultService;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.transaction.annotation.Transactional;

@Configuration
public class MatchModuleConfiguration {

    // --- Domain Services ---

    @Bean
    public MatchResultService matchResultService() {
        return new MatchResultService();
    }

    // --- Application Use Cases ---

    @Bean
    public CreateMatchUseCase createMatchUseCase(MatchRepository matchRepository) {
        return new CreateMatchAdapter(new CreateMatchService(matchRepository));
    }

    @Bean
    public GetMatchUseCase getMatchUseCase(MatchRepository matchRepository) {
        return new GetMatchService(matchRepository);
    }

    @Bean
    public UpdateMatchUseCase updateMatchUseCase(MatchRepository matchRepository) {
        return new UpdateMatchAdapter(new UpdateMatchService(matchRepository));
    }

    @Bean
    public DeleteMatchUseCase deleteMatchUseCase(MatchRepository matchRepository,
                                                 MatchTeamPort matchTeamPort,
                                                 MatchResultService matchResultService) {
        return new DeleteMatchAdapter(new DeleteMatchService(matchRepository, matchTeamPort, matchResultService));
    }

    @Bean
    public FinishMatchUseCase finishMatchUseCase(MatchRepository matchRepository,
                                                 MatchTeamPort matchTeamPort,
                                                 MatchResultService matchResultService) {
        return new FinishMatchAdapter(new FinishMatchService(matchRepository, matchTeamPort, matchResultService));
    }

    @Bean
    public PostponeMatchUseCase postponeMatchUseCase(MatchRepository matchRepository) {
        return new PostponeMatchAdapter(new PostponeMatchService(matchRepository));
    }

    // --- Transactional Adapters (infrastructure concern) ---

    static class CreateMatchAdapter implements CreateMatchUseCase {
        private final CreateMatchUseCase delegate;

        CreateMatchAdapter(CreateMatchUseCase delegate) {
            this.delegate = delegate;
        }

        @Override
        @Transactional
        public Match create(CreateMatchCommand command) {
            return delegate.create(command);
        }
    }

    static class UpdateMatchAdapter implements UpdateMatchUseCase {
        private final UpdateMatchUseCase delegate;

        UpdateMatchAdapter(UpdateMatchUseCase delegate) {
            this.delegate = delegate;
        }

        @Override
        @Transactional
        public Match update(UpdateMatchCommand command) {
            return delegate.update(command);
        }
    }

    static class DeleteMatchAdapter implements DeleteMatchUseCase {
        private final DeleteMatchUseCase delegate;

        DeleteMatchAdapter(DeleteMatchUseCase delegate) {
            this.delegate = delegate;
        }

        @Override
        @Transactional
        public void delete(Long matchId) {
            delegate.delete(matchId);
        }
    }

    static class FinishMatchAdapter implements FinishMatchUseCase {
        private final FinishMatchUseCase delegate;

        FinishMatchAdapter(FinishMatchUseCase delegate) {
            this.delegate = delegate;
        }

        @Override
        @Transactional
        public Match finishMatch(FinishMatchCommand command) {
            return delegate.finishMatch(command);
        }
    }

    static class PostponeMatchAdapter implements PostponeMatchUseCase {
        private final PostponeMatchUseCase delegate;

        PostponeMatchAdapter(PostponeMatchUseCase delegate) {
            this.delegate = delegate;
        }

        @Override
        @Transactional
        public Match postponeMatch(Long matchId) {
            return delegate.postponeMatch(matchId);
        }
    }
}