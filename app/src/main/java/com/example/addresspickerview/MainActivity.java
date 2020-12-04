package com.example.addresspickerview;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import com.example.addresspickerlib.beans.AddressInfo;
import com.example.addresspickerlib.beans.YwpAddressBean;
import com.example.addresspickerlib.callbacks.OnClickCallBack;
import com.example.addresspickerlib.popus.AddressPickerWindow;
import com.google.gson.Gson;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }

    public void button(View view) {
        /**
         * 初始化数据
         * 拿assets下的json文件
         */
        StringBuilder jsonSB = new StringBuilder();
        try {
            BufferedReader addressJsonStream = new BufferedReader(new InputStreamReader(this.getAssets().open("address.json")));
            String line;
            while ((line = addressJsonStream.readLine()) != null) {
                jsonSB.append(line);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        // 将数据转换为对象
        YwpAddressBean data = new Gson().fromJson(jsonSB.toString(), YwpAddressBean.class);
        AddressPickerWindow payPopWindow = new AddressPickerWindow(this, data, new OnClickCallBack<AddressInfo>() {
            @Override
            public void onClick(int type, int index, AddressInfo addressInfo) {
                Toast.makeText(MainActivity.this, addressInfo.getAddress(), Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onDismiss() {

            }
        });
        payPopWindow.showPopupWindow(MainActivity.this.getWindow().getDecorView());
    }
}