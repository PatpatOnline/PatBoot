package cn.edu.buaa.patpat.boot.common.dto;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.List;

@Data
@AllArgsConstructor(access = AccessLevel.PRIVATE)
public class PageListResponse<TData> {
    private final List<TData> items;
    private final int page;
    private final int pageSize;
    private final int total;

    public static <TData> PageListResponse<TData> of(List<TData> items, int total, int page, int pageSize) {
        return new PageListResponse<>(items, total, page, pageSize);
    }

    public boolean isLast() {
        return (page + 1) * pageSize >= total;
    }
}
