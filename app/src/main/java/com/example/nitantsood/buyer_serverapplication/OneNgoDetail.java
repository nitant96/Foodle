package com.example.nitantsood.buyer_serverapplication;

import java.io.Serializable;

/**
 * Created by NITANT SOOD on 27-10-2017.
 */

public class OneNgoDetail implements Serializable {
    String email,contact_no,organization_name,aadhaar_UID,ngo_UID;

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

    public String getOrganization_name() {
        return organization_name;
    }

    public void setOrganization_name(String organization_name) {
        this.organization_name = organization_name;
    }

    public String getAadhaar_UID() {
        return aadhaar_UID;
    }

    public void setAadhaar_UID(String aadhaar_UID) {
        this.aadhaar_UID = aadhaar_UID;
    }

    public String getNgo_UID() {
        return ngo_UID;
    }

    public void setNgo_UID(String ngo_UID) {
        this.ngo_UID = ngo_UID;
    }
}
