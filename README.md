AddressPickerLib
仿京东地址选择器
依赖方法


使用方法
不管是用在popwindow上还是在activity或者fragment里面，都只要直接就在xml文件里面这么用就行了
<com.example.addresspickerlib.views.AddressPickerView

   android:id="@+id/apvAddress"

   app:layout_constraintBottom_toBottomOf="parent"

   android:layout_width="match_parent"

   android:layout_height="wrap_content">
</com.example.addresspickerlib.views.AddressPickerView>

然后再设置一下回调就行了，如下
addressView.setOnAddressPickerSure(new AddressPickerView.OnAddressPickerSureListener() {

   @Override
   public void onSureClick(String address, String provinceCode, String cityCode, String districtCode) {

       mTvAddress.setText(address);
    
   }
});
