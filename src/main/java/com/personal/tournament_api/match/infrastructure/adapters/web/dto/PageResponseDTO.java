package com.personal.tournament_api.match.infrastructure.adapters.web.dto;

import java.util.List;

/**
 * DTO genérico para respuestas paginadas.
 */
public record PageResponseDTO<T>(
        List<T> content,
        int page,
        int size,
        long totalElements,
        int totalPages,
        boolean first,
        boolean last,
        boolean hasNext,
        boolean hasPrevious
) {
    public static <T> PageResponseDTO<T> from(com.personal.tournament_api.match.domain.model.Page<T> domainPage) {
        return new PageResponseDTO<>(
                domainPage.getContent(),
                domainPage.getPage(),
                domainPage.getSize(),
                domainPage.getTotalElements(),
                domainPage.getTotalPages(),
                domainPage.isFirst(),
                domainPage.isLast(),
                domainPage.hasNext(),
                domainPage.hasPrevious()
        );
    }
}
