package com.personal.tournament_api.match.infrastructure.config;

import com.personal.tournament_api.match.application.*;
import com.personal.tournament_api.match.application.usecases.*;
import com.personal.tournament_api.match.domain.ports.MatchRepository;
import com.personal.tournament_api.match.domain.ports.MatchTeamPort;
import com.personal.tournament_api.match.domain.services.MatchResultService;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

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
        return new CreateMatchService(matchRepository);
    }

    @Bean
    public GetMatchUseCase getMatchUseCase(MatchRepository matchRepository) {
        return new GetMatchService(matchRepository);
    }

    @Bean
    public UpdateMatchUseCase updateMatchUseCase(MatchRepository matchRepository) {
        return new UpdateMatchService(matchRepository);
    }

    @Bean
    public DeleteMatchUseCase deleteMatchUseCase(
            MatchRepository matchRepository,
            MatchTeamPort matchTeamPort,
            MatchResultService matchResultService) {
        return new DeleteMatchService(matchRepository, matchTeamPort, matchResultService);
    }

    @Bean
    public FinishMatchUseCase finishMatchUseCase(
            MatchRepository matchRepository,
            MatchTeamPort matchTeamPort,
            MatchResultService matchResultService) {
        return new FinishMatchService(matchRepository, matchTeamPort, matchResultService);
    }

    @Bean
    public PostponeMatchUseCase postponeMatchUseCase(MatchRepository matchRepository) {
        return new PostponeMatchService(matchRepository);
    }
}
