package com.infix.phukiencongnghe.utils.paging;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.infix.phukiencongnghe.data.model.Page;

//Lớp này sẽ được chứa trong viewmodel có logic gọi phân trang
//Mục đích của lớp này là để cung cấp Livedata cho các fragment/activity có phân trang sử dụng,
//khi kéo api về ta sẽ đi thiết lập trang và tổng trang hiện tại chô 2 mutable data, phía frag/activity
//sẽ nhận được dữ liệu và gọi cập nhật để PaginationBarView setup lại giao diện và trang nhấn
public class PaginationManager {
    private final MutableLiveData<Page> _currentPage = new MutableLiveData<>(new Page(1));
    public final LiveData<Page> currentPage = _currentPage;

    private final MutableLiveData<Integer> _totalPages = new MutableLiveData<>(1);
    public final LiveData<Integer> totalPages = _totalPages;

    //Khi tạo ra đối tượng này ta cần truyền page size cho nó. Mục đích là để lần gọi api đầu tiên
    //khi mở frag/activity thì nó sẽ gửi page/pagesize lên server
    private final int pageSize;

    public PaginationManager(int defaultPageSize) {
        this.pageSize = defaultPageSize;
    }

    public PaginationRequest getRequestParams() {
        return new PaginationRequest(_currentPage.getValue() != null ? _currentPage.getValue().getPage() : 1, pageSize);
    }

// code cua vu nguyen
//    public void setTotalPages(int totalCount) {
//        int pages = (int) Math.ceil((double) totalCount / pageSize);
//        _totalPages.setValue(pages == 0 ? 1 : pages);
//    }


    public int getPageSize() {
        return pageSize;
    }

    public void setTotalPages(int totalPages) {
        _totalPages.setValue(
                totalPages <= 0 ? 1 : totalPages
        );
    }

    public void setCurrentPage(int page) {
        _currentPage.setValue(new Page(page));
    }

    public void reset() {
        _currentPage.setValue(new Page(1));
        _totalPages.setValue(1);
    }
}