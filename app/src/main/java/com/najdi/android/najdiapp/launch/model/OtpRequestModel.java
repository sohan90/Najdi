package com.najdi.android.najdiapp.launch.model;

public class OtpRequestModel {
    private String otp;
    private String temp_id;
    private String lang;
    private String token;
    private String phone;

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public void setLang(String lang) {
        this.lang = lang;
    }

    public void setOtp(String otp) {
        this.otp = otp;
    }

    public void setTempId(String tempId) {
        this.temp_id = tempId;
    }
}
