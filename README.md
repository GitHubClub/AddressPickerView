AddressPickerLib
仿京东地址选择器弹框
依赖方法

allprojects {
   repositories {
      ...
      maven { url 'https://jitpack.io' }
   }
}
dependencies {
        implementation 'com.github.GitHubClub:AddressPickerView:1.0.0'
}

使用方法
data为地址数据生成的bean，需要将接口返回的数据封装成YwpAddressBean格式的bean
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
