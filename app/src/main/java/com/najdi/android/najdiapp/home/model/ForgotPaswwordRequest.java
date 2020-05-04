package com.najdi.android.najdiapp.home.model;

public class ForgotPaswwordRequest {
    private String phone;
    private String lang;
    private String otp;
    private String password;
    private String new_mobile;
    private String new_password;
    private String id;
    private String token;

    //new changes
    String cp;
    String np;
    String cnp;

    public void setCp(String cp) {
        this.cp = cp;
    }

    public void setNp(String np) {
        this.np = np;
    }

    public void setCnp(String cnp) {
        this.cnp = cnp;
    }

    public void setUserid(String user_id) {
        this.id = user_id;
    }

    public void setToken(String token) {
        this.token = token;
    }

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
        this.phone = mobile;
    }

    public void setLang(String lang) {
        this.lang = lang;
    }
}
