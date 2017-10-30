package com.example.nitantsood.buyer_serverapplication;

import com.fasterxml.jackson.annotation.JsonIgnore;

import java.io.Serializable;
import java.util.ArrayList;

/**
 * Created by NITANT SOOD on 27-10-2017.
 */

public class OnePoolItem implements Serializable {
    String dateTime;
    String place;
    Double poolLat,poolLng;
    String name;
    @JsonIgnore
    String pool_UID;

    public String getPool_UID() {
        return pool_UID;
    }

    public void setPool_UID(String pool_UID) {
        this.pool_UID = pool_UID;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDateTime() {
        return dateTime;
    }

    public void setDateTime(String dateTime) {
        this.dateTime = dateTime;
    }

    public String getPlace() {
        return place;
    }

    public void setPlace(String place) {
        this.place = place;
    }

    public Double getPoolLat() {
        return poolLat;
    }

    public void setPoolLat(Double poolLat) {
        this.poolLat = poolLat;
    }

    public Double getPoolLng() {
        return poolLng;
    }

    public void setPoolLng(Double poolLng) {
        this.poolLng = poolLng;
    }
}
