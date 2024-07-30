package cn.edu.buaa.patpat.boot.common.dto;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.List;

/**
 * Used to return a list of items with 1-based pagination information.
 */
@Data
@AllArgsConstructor(access = AccessLevel.PRIVATE)
public class PageListDto<TData> {
    private final List<TData> items;
    private final int total;
    private final int page;
    private final int pageSize;

    public static <TData> PageListDto<TData> of(List<TData> items, int total, int page, int pageSize) {
        return new PageListDto<>(items, total, page, pageSize);
    }

    public boolean isLast() {
        return page * pageSize >= total;
    }
}
