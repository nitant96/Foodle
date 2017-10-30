package com.example.nitantsood.buyer_serverapplication;

import com.fasterxml.jackson.annotation.JsonIgnore;

import java.io.Serializable;

/**
 * Created by NITANT SOOD on 26-10-2017.
 */

public class OneUserDetails implements Serializable {

    String name;
    @JsonIgnore
    String user_UID;
    String email;
    String contact_no;
    String aadhaar_UID;

    public OneUserDetails() {
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

    public String getUser_UID() {
        return user_UID;
    }

    public void setUser_UID(String user_UID) {
        this.user_UID = user_UID;
    }
}
