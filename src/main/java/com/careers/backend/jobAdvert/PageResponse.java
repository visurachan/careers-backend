package com.careers.backend.jobAdvert;

import java.util.List;

public record PageResponse<T>(
        List<T> content,
        int totalPages,
        long totalElements,
        int currentPage,
        int pageSize
) {}