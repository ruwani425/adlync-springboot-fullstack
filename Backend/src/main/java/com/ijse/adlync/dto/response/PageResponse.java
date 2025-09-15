package com.ijse.adlync.dto.response;


import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class PageResponse<T> {
    private List<T> content;
    private int pageNumber;
    private int pageSize;
    private long totalElements;
    private int totalPages;
    private boolean last;

    public PageResponse(List<T> content, int pageNumber, int totalPages) {
        this.content = content;
        this.pageNumber = pageNumber;
        this.pageSize = content.size(); // current page size
        this.totalElements = content.size() * totalPages; // approximate total elements
        this.totalPages = totalPages;
        this.last = pageNumber + 1 >= totalPages;
    }
}
