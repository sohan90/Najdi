package com.najdi.android.najdiapp.launch.model;

public class SignupRequestModel {
    String phone;
    String lang;
    String email;
    String password;
    String first_name;
    String last_name;
    String fcm_token;
    String full_name;
    String device_type = "ANDROID";

    public void setFcmToken(String fcm_token) {
        this.fcm_token = fcm_token;
    }

    public void setMobile(String mobile) {
        this.phone = mobile;
    }

    public void setFullName(String full_name) {
        this.full_name = full_name;
    }

    public void setLang(String lang) {
        this.lang = lang;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public void setFirstName(String first_name) {
        this.first_name = first_name;
    }

    public void setLast_name(String last_name) {
        this.last_name = last_name;
    }

}
