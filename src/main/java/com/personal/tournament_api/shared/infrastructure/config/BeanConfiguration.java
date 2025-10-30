package com.personal.tournament_api.shared.infrastructure.config;

import com.personal.tournament_api.tournament.domain.TournamentDomainService;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class BeanConfiguration {

    @Bean
    public TournamentDomainService tournamentDomainService() {
        return new TournamentDomainService();
    }
}