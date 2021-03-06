package com.najdi.android.najdiapp.launch.model;

public class BillingAddress {
    private String first_name;
    private String last_name;
    private String company;
    private String address_1;
    private String address_2;
    private String city;
    private String state;
    private String postcode;
    private String country;
    private String email;
    private String phone;
    private String lat;
    private String lng;
    private String user_id;
    private String full_name;
    private String address;
    private String map_address;
    private String lang;
    private int payment_method;

    private String total_cart_amount;
    private String total_attributes_amount;
    private String tax_amount;
    private String coupon_applied;
    private String coupon_token;
    private String discount;
    private String total_payable_amount;


    public void setTax_amount(String tax_amount) {
        this.tax_amount = tax_amount;
    }

    public void setCoupon_applied(String coupon_applied) {
        this.coupon_applied = coupon_applied;
    }

    public void setCoupon_token(String coupon_token) {
        this.coupon_token = coupon_token;
    }

    public void setDiscount(String discount) {
        this.discount = discount;
    }

    public void setTotal_attributes_amount(String total_attributes_amount) {
        this.total_attributes_amount = total_attributes_amount;
    }

    public void setTotal_cart_amount(String total_cart_amount) {
        this.total_cart_amount = total_cart_amount;
    }

    public void setTotal_payable_amount(String total_payable_amount) {
        this.total_payable_amount = total_payable_amount;
    }

    public void setPayment_method(int payment_method) {
        this.payment_method = payment_method;
    }

    public void setLang(String lang) {
        this.lang = lang;
    }

    public void setUserId(String user_id) {
        this.user_id = user_id;
    }

    public void setFull_name(String full_name) {
        this.full_name = full_name;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public void setMap_address(String map_address) {
        this.map_address = map_address;
    }

    public void setLat(String lat) {
        this.lat = lat;
    }

    public String getLat() {
        return lat;
    }

    public void setLng(String lng) {
        this.lng = lng;
    }

    public String getLng() {
        return lng;
    }

    public void setFirst_name(String first_name) {
        this.first_name = first_name;
    }

    public void setLast_name(String last_name) {
        this.last_name = last_name;
    }

    public void setCompany(String company) {
        this.company = company;
    }

    public void setAddress_1(String address_1) {
        this.address_1 = address_1;
    }

    public void setAddress_2(String address_2) {
        this.address_2 = address_2;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public void setState(String state) {
        this.state = state;
    }

    public void setPostcode(String postcode) {
        this.postcode = postcode;
    }

    public void setCountry(String country) {
        this.country = country;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getFirst_name() {
        return first_name;
    }

    public String getLast_name() {
        return last_name;
    }

    public String getCompany() {
        return company;
    }

    public String getAddress_1() {
        return address_1;
    }

    public String getAddress_2() {
        return address_2;
    }

    public String getCity() {
        return city;
    }

    public String getState() {
        return state;
    }

    public String getPostcode() {
        return postcode;
    }

    public String getCountry() {
        return country;
    }

    public String getEmail() {
        return email;
    }

    public String getPhone() {
        return phone;
    }
}
