package com.najdi.android.najdiapp.launch.model;

public class LoginRequestModel {
    String phone;
    String password;
    String fcm_token;
    String device_type = "ANDROID";
    String lang;


    public void setLang(String lang) {
        this.lang = lang;
    }

    public void setFcmToken(String fcm_token) {
        this.fcm_token = fcm_token;
    }

    public void setPhone(String userName) {
        this.phone = userName;
    }

    public void setPassword(String password) {
        this.password = password;
    }
}

