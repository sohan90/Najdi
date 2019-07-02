package com.najdi.android.najdiapp.home.model;

public class ForgotPaswwordRequest {
    String mobile;
    String lang;
    String otp;
    String password;
    String new_mobile;
    String new_password;


    public void setNew_password(String new_password) {
        this.new_password = new_password;
    }

    public void setNew_mobile(String new_mobile) {
        this.new_mobile = new_mobile;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public void setOtp(String otp) {
        this.otp = otp;
    }

    public void setMobile(String mobile) {
        this.mobile = mobile;
    }

    public void setLang(String lang) {
        this.lang = lang;
    }
}
