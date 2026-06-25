package com.infix.phukiencongnghe.data.dto.response;

import com.google.gson.annotations.SerializedName;

import java.util.List;

@SuppressWarnings("unused")
public class PageResponseDTO<T> {
    @SerializedName("results")
    private List<T> items;
    @SerializedName("current_page")
    private int currentPage;
    @SerializedName("page_size")
    private int pageSize;
    @SerializedName("total_pages")
    private int totalPages;

    public PageResponseDTO(List<T> items, int currentPage, int pageSize, long totalElements) {
        this.items = items;
        this.currentPage = currentPage;
        this.pageSize = pageSize;

        this.totalPages = (int) Math.ceil((double) totalElements / pageSize);
    }

    public List<T> getItems() { return items; }
    public int getCurrentPage() { return currentPage; }
    public int getPageSize() { return pageSize; }
    public int getTotalPages() { return totalPages; }
}