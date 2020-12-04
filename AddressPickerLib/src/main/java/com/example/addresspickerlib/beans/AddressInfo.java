package com.example.addresspickerlib.beans;

import java.io.Serializable;


public class AddressInfo implements Serializable {
    private String address;
    private String provinceCode;
    private String cityCode;
    private String districtCode;

    public AddressInfo(String address, String provinceCode, String cityCode, String districtCode) {
        this.address = address;
        this.provinceCode = provinceCode;
        this.cityCode = cityCode;
        this.districtCode = districtCode;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getProvinceCode() {
        return provinceCode;
    }

    public void setProvinceCode(String provinceCode) {
        this.provinceCode = provinceCode;
    }

    public String getCityCode() {
        return cityCode;
    }

    public void setCityCode(String cityCode) {
        this.cityCode = cityCode;
    }

    public String getDistrictCode() {
        return districtCode;
    }

    public void setDistrictCode(String districtCode) {
        this.districtCode = districtCode;
    }
}
