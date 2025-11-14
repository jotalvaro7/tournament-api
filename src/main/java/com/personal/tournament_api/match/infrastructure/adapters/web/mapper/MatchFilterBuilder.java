package com.personal.tournament_api.match.infrastructure.adapters.web.mapper;

import com.personal.tournament_api.match.domain.model.MatchSearchCriteria;
import com.personal.tournament_api.match.domain.model.MatchStatus;
import com.personal.tournament_api.match.domain.model.PageRequest;
import org.springframework.stereotype.Component;

import java.time.LocalDate;

/**
 * Builder for constructing domain filter objects from HTTP request parameters.
 * Handles the transformation of web layer parameters into domain value objects.
 */
@Component
public class MatchFilterBuilder {

    /**
     * Builds MatchSearchCriteria from HTTP query parameters.
     *
     * @param specificDate specific date filter (mutually exclusive with dateFrom/dateTo)
     * @param dateFrom start date for range filter (must be used with dateTo)
     * @param dateTo end date for range filter (must be used with dateFrom)
     * @param status match status filter
     * @return MatchSearchCriteria domain object
     * @throws IllegalArgumentException if specificDate is used together with dateFrom/dateTo
     */
    public MatchSearchCriteria buildSearchCriteria(LocalDate specificDate, LocalDate dateFrom, LocalDate dateTo, MatchStatus status) {
        if (specificDate != null && (dateFrom != null || dateTo != null)) {
            throw new IllegalArgumentException("Cannot use 'specificDate' together with 'dateFrom' or 'dateTo'");
        }

        if (specificDate != null) {
            return MatchSearchCriteria.withSpecificDate(specificDate, status);
        } else if (dateFrom != null && dateTo != null) {
            return MatchSearchCriteria.withDateRange(dateFrom, dateTo, status);
        } else if (status != null) {
            return MatchSearchCriteria.withStatus(status);
        } else {
            return MatchSearchCriteria.empty();
        }
    }

    /**
     * Builds PageRequest from HTTP query parameters.
     *
     * @param page page number (zero-based)
     * @param size number of items per page
     * @param sortBy field name to sort by
     * @param direction sort direction (ASC or DESC)
     * @return PageRequest domain object
     */
    public PageRequest buildPageRequest(int page, int size, String sortBy, String direction) {
        PageRequest.SortDirection sortDirection = "DESC".equalsIgnoreCase(direction)
                ? PageRequest.SortDirection.DESC
                : PageRequest.SortDirection.ASC;
        return PageRequest.of(page, size, sortBy, sortDirection);
    }
}
