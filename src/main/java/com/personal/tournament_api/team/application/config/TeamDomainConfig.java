package com.personal.tournament_api.team.application.config;

import com.personal.tournament_api.team.domain.TeamDomainService;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class TeamDomainConfig {

    @Bean
    public TeamDomainService teamDomainService() {
        return new TeamDomainService();
    }
}
