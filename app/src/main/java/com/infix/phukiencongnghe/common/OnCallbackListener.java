package com.infix.phukiencongnghe.common;

//Interface này dùng để truyền từ UI -> Viewmodel xuống các tầng dưới (Source), khi hoàn thành
//sẽ gọi onRevoke để thực hiện logic callback
public interface OnCallbackListener {
    void onRevoke();
}
