package com.infix.phukiencongnghe.data.model;

import java.time.LocalDateTime;
import java.util.Objects;

//Dùng khi ta muốn MutableLivedata cập nhật lại trạng thái nếu trùng page trước đó
public class Page {
    private int page;
    private LocalDateTime current;

    public Page(int page) {
        this.page = page;
    }

    public int getPage() {
        return page;
    }

    @Override
    public boolean equals(Object o) {
        if (!(o instanceof Page)) return false;
        Page page1 = (Page) o;
        return page == page1.page && Objects.equals(current, page1.current);
    }

    @Override
    public int hashCode() {
        return Objects.hash(page, current);
    }
}