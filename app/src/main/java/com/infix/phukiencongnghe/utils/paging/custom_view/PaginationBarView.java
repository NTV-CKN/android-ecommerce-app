package com.infix.phukiencongnghe.utils.paging.custom_view;

import android.annotation.SuppressLint;
import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import androidx.annotation.Nullable;
import com.infix.phukiencongnghe.R;

public class PaginationBarView extends LinearLayout {
    private ImageButton btnPrev, btnNext;
    private LinearLayout pageNumbers;
    private int currentPage = 1;
    private int totalPages = 1;
    private OnPageChangeListener listener;

    public interface OnPageChangeListener {
        void onPageChanged(int newPage);
    }

    public PaginationBarView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        init(context);
    }

    @SuppressLint("WrongViewCast")
    private void init(Context context) {
        LayoutInflater.from(context).inflate(R.layout.view_pagination_bar, this, true);
        btnPrev = findViewById(R.id.btn_prev_page);
        btnNext = findViewById(R.id.btn_next_page);
        pageNumbers = findViewById(R.id.lnr_page_numbers_container);

        btnPrev.setOnClickListener(v -> { if (currentPage > 1) listener.onPageChanged(currentPage - 1); });
        btnNext.setOnClickListener(v -> { if (currentPage < totalPages) listener.onPageChanged(currentPage + 1); });
    }

    public void setOnPageChangeListener(OnPageChangeListener listener) {
        this.listener = listener;
    }

    public void updatePagination(int currentPage, int totalPages) {
        this.currentPage = currentPage;
        this.totalPages = totalPages;

        //Nếu như current page > 1 thì enable nút trở lại trang trước
        btnPrev.setEnabled(currentPage > 1);
        //Nếu như current page < tổng trang thì enable nút next trang tiếp theo
        btnNext.setEnabled(currentPage < totalPages);

        pageNumbers.removeAllViews();
        //tạo ra các số page xoay quanh currentpage
        //Nếu current page - 2 vẫn lớn hơn 1 thì hiển thị 2 nút trang trước đó
        //Nếu current page + 2 mà vẫn nhỏ hơn tổng trang thì hiển thị 2 nút cho 2 trang kế 
        for (int i = Math.max(1, currentPage - 2); i <= Math.min(totalPages, currentPage + 2); i++) {
            Button btnNum = new Button(getContext());
            btnNum.setLayoutParams(new LinearLayout.LayoutParams(80, 80));
            btnNum.setText(String.valueOf(i));

            if (i == currentPage) {
                btnNum.setSelected(true);
            }

            final int pageSelected = i;
            btnNum.setOnClickListener(v -> { if (listener != null) listener.onPageChanged(pageSelected); });
            pageNumbers.addView(btnNum);
        }
    }
}