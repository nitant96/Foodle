package com.example.nitantsood.buyer_serverapplication;

import com.fasterxml.jackson.annotation.JsonIgnore;

import java.io.Serializable;

/**
 * Created by NITANT SOOD on 26-10-2017.
 */

public class OneSellerDetail implements Serializable {
    @JsonIgnore
    String seller_UID;
    String name;
    String email;
    String company_name;
    String contact_no;
    String aadhaar_UID;
    String rating;
    int transactions;

    public OneSellerDetail() {
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getCompany_name() {
        return company_name;
    }

    public void setCompany_name(String company_name) {
        this.company_name = company_name;
    }

    public String getContact_no() {
        return contact_no;
    }

    public void setContact_no(String contact_no) {
        this.contact_no = contact_no;
    }

    public String getAadhaar_UID() {
        return aadhaar_UID;
    }

    public void setAadhaar_UID(String aadhaar_UID) {
        this.aadhaar_UID = aadhaar_UID;
    }

    public String getRating() {
        return rating;
    }

    public void setRating(String rating) {
        this.rating = rating;
    }

    public int getTransactions() {
        return transactions;
    }

    public void setTransactions(int transactions) {
        this.transactions = transactions;
    }

    public String getSeller_UID() {
        return seller_UID;
    }

    public void setSeller_UID(String seller_UID) {
        this.seller_UID = seller_UID;
    }
}
