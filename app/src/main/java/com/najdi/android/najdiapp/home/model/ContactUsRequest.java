package com.najdi.android.najdiapp.home.model;

public class ContactUsRequest {
    private String user_id;
    private String message;
    private String lang;

    public void setLang(String lang) {
        this.lang = lang;
    }

    public void setUseId(String user_id) {
        this.user_id = user_id;
    }

    public void setMessage(String message) {
        this.message = message;
    }
}
