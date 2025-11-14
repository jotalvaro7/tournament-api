package com.personal.tournament_api.match.infrastructure.adapters.persistence.mapper;

import com.personal.tournament_api.match.domain.model.Page;
import com.personal.tournament_api.match.domain.model.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 * Mapper for converting between domain pagination objects and Spring Data pagination objects.
 * Handles the transformation of PageRequest and Page between domain and infrastructure layers.
 */
@Component
public class PaginationMapper {

    /**
     * Converts domain PageRequest to Spring Data Pageable.
     *
     * @param pageRequest domain page request object
     * @return Spring Data Pageable for use with repositories
     */
    public org.springframework.data.domain.Pageable toSpringPageable(PageRequest pageRequest) {
        Sort.Direction direction = pageRequest.getDirection() == PageRequest.SortDirection.ASC
                ? Sort.Direction.ASC
                : Sort.Direction.DESC;
        Sort sort = Sort.by(direction, pageRequest.getSortBy());
        return org.springframework.data.domain.PageRequest.of(pageRequest.getPage(), pageRequest.getSize(), sort);
    }

    /**
     * Converts Spring Data Page to domain Page.
     *
     * @param springPage Spring Data page object
     * @param domainContent list of domain objects (already mapped from entities)
     * @param <T> type of domain objects
     * @return domain Page object
     */
    public <T> Page<T> toDomainPage(org.springframework.data.domain.Page<?> springPage, List<T> domainContent) {
        return new Page<>(
                domainContent,
                springPage.getNumber(),
                springPage.getSize(),
                springPage.getTotalElements()
        );
    }
}
