package com.hieudt79.user_service.dto.res;

import lombok.Builder;
import lombok.Data;

@Builder
@Data
public class PageResponse<T> {
    int pageNo;
    int pageSize;
    int totalPage;
    T item;
}
