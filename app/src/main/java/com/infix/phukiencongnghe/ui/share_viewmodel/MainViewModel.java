package com.infix.phukiencongnghe.ui.share_viewmodel;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

public class MainViewModel extends ViewModel {
    private final MutableLiveData<Long> _cartBadgeCount = new MutableLiveData<>();
    public final LiveData<Long> cartBadgetCount = _cartBadgeCount;

    public void setCartBadgetCount(long count){
        _cartBadgeCount.setValue(count);
    }
}
