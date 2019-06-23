package com.najdi.android.najdiapp.launch.model;

public class SignupResponseModel {
    int id;
    private String date_created;
    private String date_created_gmt;
    private String date_modified;
    private String date_modified_gmt;
    private String email;
    private String first_name;
    private String last_name;
    private String role;
    private String username;
    private BillingAddress billing;


    public int getId() {
        return id;
    }

    public String getDate_created() {
        return date_created;
    }

    public String getDate_created_gmt() {
        return date_created_gmt;
    }

    public String getDate_modified() {
        return date_modified;
    }

    public String getDate_modified_gmt() {
        return date_modified_gmt;
    }

    public String getEmail() {
        return email;
    }

    public String getFirst_name() {
        return first_name;
    }

    public String getLast_name() {
        return last_name;
    }

    public String getRole() {
        return role;
    }

    public String getUsername() {
        return username;
    }

    public BillingAddress getBilling() {
        return billing;
    }
}
