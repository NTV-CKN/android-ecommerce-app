package com.infix.phukiencongnghe.data.dto.response;

import com.google.gson.annotations.SerializedName;
import java.util.List;

public class ProductPageDTO {

    @SerializedName("results")
    private List<FeatureProductDTO> products;

    @SerializedName("current_page")
    private Integer currentPage;

    @SerializedName("page_size")
    private Integer pageSize;

    @SerializedName("total_pages")
    private Integer totalPages;

    public List<FeatureProductDTO> getProducts() {
        return products;
    }

    public Integer getCurrentPage() {
        return currentPage;
    }

    public Integer getPageSize() {
        return pageSize;
    }

    public Integer getTotalPages() {
        return totalPages;
    }
}