package com.wirehall.commandhunt.backend.mapper;

import com.wirehall.commandhunt.backend.dto.filter.Filter;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;

public class PaginationMapper {
    public Pageable mapToPageable(Filter filter) {
        return PageRequest.of(filter.getPagination().getPageNumber() -1, filter.getPagination().getPageSize(),
                Sort.by(Sort.Direction.fromString(filter.getPagination().getSort().getSortOrder().toString()),
                        filter.getPagination().getSort().getSortBy()));
    }
}
