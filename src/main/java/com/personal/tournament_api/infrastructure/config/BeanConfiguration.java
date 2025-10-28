package com.personal.tournament_api.infrastructure.config;

import com.personal.tournament_api.domain.services.TournamentDomainService;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class BeanConfiguration {

    @Bean
    public TournamentDomainService tournamentDomainService() {
        return new TournamentDomainService();
    }
}