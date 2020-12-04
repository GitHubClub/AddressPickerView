package com.example.addresspickerlib.popus.popus;

import android.content.Context;
import android.graphics.drawable.ColorDrawable;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.WindowManager;
import android.widget.PopupWindow;

import com.example.addresspickerlib.R;
import com.example.addresspickerlib.beans.AddressInfo;
import com.example.addresspickerlib.beans.YwpAddressBean;
import com.example.addresspickerlib.callbacks.OnClickCallBack;
import com.example.addresspickerlib.views.AddressPickerView;


public class AddressPickerWindow extends PopupWindow {
    private OnDismissListener listener;

    public AddressPickerWindow(Context mContext, YwpAddressBean data, OnClickCallBack<AddressInfo> callBack) {
        View contentView = LayoutInflater.from(mContext).inflate(R.layout.tu_pop_address_picker, null);
        AddressPickerView addressPickerView = contentView.findViewById(R.id.addressPickerView);

        addressPickerView.initData(data);
        addressPickerView.setOnAddressPickerSure(new AddressPickerView.OnAddressPickerSureListener() {
            @Override
            public void onSureClick(String address, String provinceCode, String cityCode, String districtCode) {
                if ("-1".equals(address) && "-1".equals(provinceCode) && "-1".equals(cityCode) && "-1".equals(districtCode)) {

                } else {
                    callBack.onClick(1, 1, new AddressInfo(address, provinceCode, cityCode, districtCode));
                }
                dismiss();
            }
        });

        setBackgroundDrawable(new ColorDrawable());
        // 设置SelectPicPopupWindow弹出窗体可点击
        this.setFocusable(false);
        this.setTouchable(true);
        this.setOutsideTouchable(false);
        //设置宽与高
        setWidth(WindowManager.LayoutParams.MATCH_PARENT);
        setHeight(WindowManager.LayoutParams.MATCH_PARENT);
        setContentView(contentView);

        setOnDismissListener(new PopupWindow.OnDismissListener() {
            @Override
            public void onDismiss() {
                //执行跳转操作
                if (listener != null) {
                    listener.onDismiss();
                }
            }
        });
    }

    public void showPopupWindow(View parent) {
        if (!isShowing()) {
            showAtLocation(parent, Gravity.CENTER, 0, 0);
        } else {
            this.dismiss();
        }
    }

    public void setOnDismissListener(OnDismissListener listener) {
        this.listener = listener;
    }

    public interface OnDismissListener {
        void onDismiss();
    }
}
