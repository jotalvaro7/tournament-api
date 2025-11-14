package com.personal.tournament_api.match.domain.model;

import java.util.Objects;

/**
 * Value Object para solicitudes de paginación.
 * Equivalente a Pageable de Spring pero sin dependencias de frameworks.
 */
public class PageRequest {
    private final int page;
    private final int size;
    private final String sortBy;
    private final SortDirection direction;

    public enum SortDirection {
        ASC, DESC
    }

    private PageRequest(int page, int size, String sortBy, SortDirection direction) {
        this.page = page;
        this.size = size;
        this.sortBy = sortBy;
        this.direction = direction;
        validate();
    }

    public static PageRequest of(int page, int size) {
        return new PageRequest(page, size, "matchDate", SortDirection.ASC);
    }

    public static PageRequest of(int page, int size, String sortBy, SortDirection direction) {
        return new PageRequest(page, size, sortBy, direction);
    }

    private void validate() {
        if (page < 0) {
            throw new IllegalArgumentException("Page number must be non-negative");
        }
        if (size <= 0) {
            throw new IllegalArgumentException("Page size must be positive");
        }
        if (size > 100) {
            throw new IllegalArgumentException("Page size must not exceed 100");
        }
    }

    public int getPage() {
        return page;
    }

    public int getSize() {
        return size;
    }

    public String getSortBy() {
        return sortBy;
    }

    public SortDirection getDirection() {
        return direction;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        PageRequest that = (PageRequest) o;
        return page == that.page &&
               size == that.size &&
               Objects.equals(sortBy, that.sortBy) &&
               direction == that.direction;
    }

    @Override
    public int hashCode() {
        return Objects.hash(page, size, sortBy, direction);
    }

    @Override
    public String toString() {
        return "PageRequest{" +
                "page=" + page +
                ", size=" + size +
                ", sortBy='" + sortBy + '\'' +
                ", direction=" + direction +
                '}';
    }
}
