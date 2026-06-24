package com.infix.phukiencongnghe.utils.paging;
public class PaginationRequest {
    private int page;
    private final int pageSize;

    public PaginationRequest(int page, int pageSize) {
        this.page = page;
        this.pageSize = pageSize;
    }

    public int getPage() { return page; }
    public void setPage(int page) { this.page = page; }
    public int getPageSize() { return pageSize; }
}