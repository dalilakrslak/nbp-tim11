package com.nbp.cinemaapp.util;

import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;

public class Pagination {

    private final Integer page;
    private final Integer size;

    public Pagination(final Integer page, final Integer size) {
        this.page = page != null ? page : 0;
        this.size = size != null ? size : 50;
    }

    public Integer getPage() {
        return page;
    }

    public Integer getSize() {
        return size;
    }

    public Pageable toPageable() {
        return PageRequest.of(page, size);
    }
}
