package com.najdi.android.najdiapp.home.model;

public class UpdateProfileModelRequest {
    private String id;
    private String full_name;
    private String last_name;
    private String email;
    private String address;
    private String city;
    private String lang;
    private String zip;
    private String phone;
    private String password;


    public void setLang(String lang) {
        this.lang = lang;
    }

    public void setId(String id) {
        this.id = id;
    }

    public void setFullName(String first_name) {
        this.full_name = first_name;
    }

    public void setLastName(String last_name) {
        this.last_name = last_name;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public void setZip(String zip) {
        this.zip = zip;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public void setPassword(String password) {
        this.password = password;
    }
}
