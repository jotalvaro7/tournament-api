package com.personal.tournament_api.match.domain.model;

import java.util.List;
import java.util.Objects;

/**
 * Value Object que representa una página de resultados.
 * Equivalente a Page de Spring pero sin dependencias de frameworks.
 */
public class Page<T> {
    private final List<T> content;
    private final int page;
    private final int size;
    private final long totalElements;
    private final int totalPages;

    public Page(List<T> content, int page, int size, long totalElements) {
        this.content = Objects.requireNonNull(content, "Content cannot be null");
        this.page = page;
        this.size = size;
        this.totalElements = totalElements;
        this.totalPages = size > 0 ? (int) Math.ceil((double) totalElements / size) : 0;
    }

    public List<T> getContent() {
        return content;
    }

    public int getPage() {
        return page;
    }

    public int getSize() {
        return size;
    }

    public long getTotalElements() {
        return totalElements;
    }

    public int getTotalPages() {
        return totalPages;
    }

    public boolean hasContent() {
        return !content.isEmpty();
    }

    public boolean isEmpty() {
        return content.isEmpty();
    }

    public boolean isFirst() {
        return page == 0;
    }

    public boolean isLast() {
        return page >= totalPages - 1;
    }

    public boolean hasNext() {
        return page < totalPages - 1;
    }

    public boolean hasPrevious() {
        return page > 0;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Page<?> page1 = (Page<?>) o;
        return page == page1.page &&
               size == page1.size &&
               totalElements == page1.totalElements &&
               totalPages == page1.totalPages &&
               Objects.equals(content, page1.content);
    }

    @Override
    public int hashCode() {
        return Objects.hash(content, page, size, totalElements, totalPages);
    }

    @Override
    public String toString() {
        return "Page{" +
                "content=" + content.size() + " items" +
                ", page=" + page +
                ", size=" + size +
                ", totalElements=" + totalElements +
                ", totalPages=" + totalPages +
                '}';
    }
}
