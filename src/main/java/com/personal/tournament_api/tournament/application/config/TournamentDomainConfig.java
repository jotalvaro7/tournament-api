package com.personal.tournament_api.tournament.application.config;

import com.personal.tournament_api.tournament.domain.TournamentDomainService;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class TournamentDomainConfig {

    @Bean
    public TournamentDomainService tournamentDomainService() {
        return new TournamentDomainService();
    }
}
