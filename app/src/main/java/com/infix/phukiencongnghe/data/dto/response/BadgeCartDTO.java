package com.infix.phukiencongnghe.data.dto.response;

public class BadgeCartDTO {
    private Long numOfItems;

    public BadgeCartDTO(Long numOfItems) {
        this.numOfItems = numOfItems;
    }

    public Long getNumOfItems() {
        return numOfItems;
    }

    public void setNumOfItems(Long numOfItems) {
        this.numOfItems = numOfItems;
    }
}
