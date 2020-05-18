package com.najdi.android.najdiapp.launch.model;

public class OtpRequestModel {
    String mobile;
    String lang;
    String otp;
    String temp_Id;


    public void setLang(String lang) {
        this.lang = lang;
    }

    public void setMobile(String mobile) {
        this.mobile = mobile;
    }

    public void setOtp(String otp) {
        this.otp = otp;
    }

    public void setTempId(String tempId) {
        this.temp_Id = tempId;
    }
}
