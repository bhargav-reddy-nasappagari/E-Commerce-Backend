package com.ecommerce.backend.util;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.data.domain.Page;

import java.util.List;

/**
 * Standard paginated response wrapper.
 *
 * @param <T> the type of elements in the page
 */
@Getter
@AllArgsConstructor(access = AccessLevel.PRIVATE)
public final class PageResponse<T> {

    private final List<T> content;

    private final int pageNumber;

    private final int pageSize;

    private final long totalElements;

    private final int totalPages;

    private final boolean last;

    /**
     * Creates a PageResponse from a Spring Data Page.
     *
     * @param page the source page
     * @param <T> the response element type
     * @return a PageResponse instance
     */
    public static <T> PageResponse<T> of(Page<T> page) {
        return new PageResponse<>(
                page.getContent(),
                page.getNumber(),
                page.getSize(),
                page.getTotalElements(),
                page.getTotalPages(),
                page.isLast()
        );
    }
}