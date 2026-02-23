package com.appswave.membership.web.dto.response;

import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.util.List;

@Data
@NoArgsConstructor
public class PageResponse<T> extends Response implements Serializable {

    private static final long serialVersionUID = 1L;
    private List<T> content;
    private int page;
    private int size;
    private long totalElements;
    private int totalPages;
    private boolean last;
}
