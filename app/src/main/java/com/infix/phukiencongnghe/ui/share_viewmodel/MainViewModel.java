package com.infix.phukiencongnghe.ui.share_viewmodel;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import javax.inject.Inject;

import dagger.hilt.android.lifecycle.HiltViewModel;

@HiltViewModel
public class MainViewModel extends ViewModel {
    private final MutableLiveData<Long> _cartBadgeCount = new MutableLiveData<>();
    public final LiveData<Long> cartBadgetCount = _cartBadgeCount;

    public MutableLiveData<Long> getCartBadgeCount() {
        return _cartBadgeCount;
    }

    @Inject
    public MainViewModel() {}

    public void setCartBadgetCount(long count){
        _cartBadgeCount.setValue(count);
    }
}
