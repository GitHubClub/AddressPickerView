package com.example.addresspickerlib.callbacks;

public interface OnClickCallBack<T> {
    void onClick(int type, int index, T t);

    void onDismiss();
}
