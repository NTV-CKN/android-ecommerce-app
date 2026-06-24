package com.infix.phukiencongnghe.ui.adapter.feature_product;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RatingBar;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.infix.phukiencongnghe.R;
import com.infix.phukiencongnghe.data.dto.response.ReviewDTO;

import java.util.List;

public class ProductReviewAdapter extends RecyclerView.Adapter<ProductReviewAdapter.ReviewHolder> {
    private final List<ReviewDTO> reviewList;

    public ProductReviewAdapter(List<ReviewDTO> reviewList) {
        this.reviewList = reviewList;
    }

    @NonNull
    @Override
    public ProductReviewAdapter.ReviewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_product_comment, parent,false);
        return new ReviewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ProductReviewAdapter.ReviewHolder holder, int position) {
        ReviewDTO reviewDTO = reviewList.get(position);
        holder.tvUser.setText(reviewDTO.getName());
        holder.tvDate.setText(reviewDTO.getEvaluateDate());
        holder.tvContent.setText(reviewDTO.getEvolute());
        if(reviewDTO.getNumStar() !=null){
            holder.rating.setRating(reviewDTO.getNumStar());
        }else {
            holder.rating.setRating(5.0f);
        }
    }

    @Override
    public int getItemCount() {
        if(reviewList!=null){
            return reviewList.size();
        }
        return 0;
    }
    static class ReviewHolder extends RecyclerView.ViewHolder{
        TextView tvUser, tvDate, tvContent;
        RatingBar rating;
        public ReviewHolder(@NonNull View itemView) {
            super(itemView);
            tvUser = itemView.findViewById(R.id.tvReviewUser);
            tvDate = itemView.findViewById(R.id.tvReviewDate);
            tvContent = itemView.findViewById(R.id.tvReviewContent);
            rating = itemView.findViewById(R.id.ratingBarReview);
        }
    }
}
