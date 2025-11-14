package com.personal.tournament_api.match.application.config;

import com.personal.tournament_api.match.domain.services.MatchResultService;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class MatchDomainConfig {

    @Bean
    public MatchResultService matchResultService() {
        return new MatchResultService();
    }
}
