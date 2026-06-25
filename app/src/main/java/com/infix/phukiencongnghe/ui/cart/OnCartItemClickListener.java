package com.infix.phukiencongnghe.ui.cart;

public interface OnCartItemClickListener {
    void onPlusClick(Integer itemId, Integer currentQty);
    void onMinusClick(Integer itemId, Integer currentQty);
    void onDeleteClick(Integer itemId);
    void onQuantityTextChanged(Integer itemId, Integer newQty);
}
