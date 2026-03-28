package com.personal.tournament_api.match.domain.model;

import java.time.LocalDate;
import java.util.Objects;

/**
 * Object Value that encapsulates the search criteria for matches.
 */
public class MatchSearchCriteria {
    private final LocalDate specificDate;
    private final LocalDate dateFrom;
    private final LocalDate dateTo;
    private final MatchStatus status;
    private final Integer matchday;

    private MatchSearchCriteria(LocalDate specificDate, LocalDate dateFrom, LocalDate dateTo, MatchStatus status, Integer matchday) {
        this.specificDate = specificDate;
        this.dateFrom = dateFrom;
        this.dateTo = dateTo;
        this.status = status;
        this.matchday = matchday;
        validate();
    }

    public static MatchSearchCriteria withSpecificDate(LocalDate date, MatchStatus status, Integer matchday) {
        return new MatchSearchCriteria(date, null, null, status, matchday);
    }

    public static MatchSearchCriteria withDateRange(LocalDate from, LocalDate to, MatchStatus status, Integer matchday) {
        return new MatchSearchCriteria(null, from, to, status, matchday);
    }

    public static MatchSearchCriteria withStatus(MatchStatus status, Integer matchday) {
        return new MatchSearchCriteria(null, null, null, status, matchday);
    }

    public static MatchSearchCriteria empty() {
        return new MatchSearchCriteria(null, null, null, null, null);
    }

    private void validate() {

        if (specificDate != null && (dateFrom != null || dateTo != null)) {
            throw new IllegalArgumentException("Cannot use specific date and date range filters simultaneously");
        }

        if ((dateFrom != null && dateTo == null) || (dateFrom == null && dateTo != null)) {
            throw new IllegalArgumentException("Date range requires both 'from' and 'to' dates");
        }

        if (dateFrom != null && dateTo != null && dateFrom.isAfter(dateTo)) {
            throw new IllegalArgumentException("'dateFrom' must be before or equal to 'dateTo'");
        }
    }

    public boolean hasSpecificDate() {
        return specificDate != null;
    }

    public boolean hasDateRange() {
        return dateFrom != null && dateTo != null;
    }

    public boolean hasStatus() {
        return status != null;
    }

    public boolean hasMatchday() {
        return matchday != null;
    }

    public boolean isEmpty() {
        return specificDate == null && dateFrom == null && dateTo == null && status == null && matchday == null;
    }

    // Getters
    public LocalDate getSpecificDate() {
        return specificDate;
    }

    public LocalDate getDateFrom() {
        return dateFrom;
    }

    public LocalDate getDateTo() {
        return dateTo;
    }

    public MatchStatus getStatus() {
        return status;
    }

    public Integer getMatchday() {
        return matchday;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        MatchSearchCriteria that = (MatchSearchCriteria) o;
        return Objects.equals(specificDate, that.specificDate) &&
               Objects.equals(dateFrom, that.dateFrom) &&
               Objects.equals(dateTo, that.dateTo) &&
               status == that.status &&
               Objects.equals(matchday, that.matchday);
    }

    @Override
    public int hashCode() {
        return Objects.hash(specificDate, dateFrom, dateTo, status, matchday);
    }

    @Override
    public String toString() {
        return "MatchSearchCriteria{" +
                "specificDate=" + specificDate +
                ", dateFrom=" + dateFrom +
                ", dateTo=" + dateTo +
                ", status=" + status +
                ", matchday=" + matchday +
                '}';
    }
}
