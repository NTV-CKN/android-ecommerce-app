package com.infix.phukiencongnghe.ui.searchadvance;

import android.graphics.Rect;
import android.view.View;
import androidx.recyclerview.widget.RecyclerView;

public class GridSpacingItemDecoration
        extends RecyclerView.ItemDecoration {

    private final int spanCount;
    private final int spacing;

    public GridSpacingItemDecoration(
            int spanCount,
            int spacing
    ) {
        this.spanCount = spanCount;
        this.spacing = spacing;
    }

    @Override
    public void getItemOffsets(
            Rect outRect,
            View view,
            RecyclerView parent,
            RecyclerView.State state
    ) {

        int position =
                parent.getChildAdapterPosition(view);

        int column =
                position % spanCount;


        outRect.left =
                spacing / 2;

        outRect.right =
                spacing / 2;

        outRect.top =
                spacing / 2;

        outRect.bottom =
                spacing / 2;
    }
}