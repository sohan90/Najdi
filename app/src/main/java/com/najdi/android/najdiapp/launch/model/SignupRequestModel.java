package com.najdi.android.najdiapp.launch.model;

public class SignupRequestModel {
    String email;
    String password;
    String first_name;
    String last_name;
    String username;
    BillingAddress billing;


    public void setBilling(BillingAddress billing) {
        this.billing = billing;
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

    public void setUsername(String username) {
        this.username = username;
    }
}
