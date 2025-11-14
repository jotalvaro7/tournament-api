package com.personal.tournament_api.match.infrastructure.adapters.persistence.repository;

import com.personal.tournament_api.match.domain.model.MatchSearchCriteria;
import com.personal.tournament_api.match.infrastructure.adapters.persistence.entity.MatchEntity;
import org.springframework.data.jpa.domain.Specification;

import java.time.LocalDate;
import java.time.LocalTime;

/**
 * Util class for building JPA Specifications for MatchEntity based on various criteria.
 */
public class MatchSpecifications {

    private MatchSpecifications() {

    }

    public static Specification<MatchEntity> withTournamentId(Long tournamentId) {
        return (root, query, cb) -> cb.equal(root.get("tournamentId"), tournamentId);
    }

    public static Specification<MatchEntity> withSpecificDate(LocalDate date) {
        return (root, query, cb) -> {
            var startOfDay = date.atStartOfDay();
            var endOfDay = date.atTime(LocalTime.MAX);
            return cb.between(root.get("matchDate"), startOfDay, endOfDay);
        };
    }

    public static Specification<MatchEntity> withDateRange(LocalDate from, LocalDate to) {
        return (root, query, cb) -> {
            var startOfDay = from.atStartOfDay();
            var endOfDay = to.atTime(LocalTime.MAX);
            return cb.between(root.get("matchDate"), startOfDay, endOfDay);
        };
    }

    public static Specification<MatchEntity> withStatus(com.personal.tournament_api.match.domain.model.MatchStatus status) {
        return (root, query, cb) -> cb.equal(root.get("status"), status);
    }

    public static Specification<MatchEntity> fromCriteria(Long tournamentId, MatchSearchCriteria criteria) {
        Specification<MatchEntity> spec = withTournamentId(tournamentId);

        if (criteria.hasSpecificDate()) {
            spec = spec.and(withSpecificDate(criteria.getSpecificDate()));
        }

        if (criteria.hasDateRange()) {
            spec = spec.and(withDateRange(criteria.getDateFrom(), criteria.getDateTo()));
        }

        if (criteria.hasStatus()) {
            spec = spec.and(withStatus(criteria.getStatus()));
        }

        return spec;
    }
}
