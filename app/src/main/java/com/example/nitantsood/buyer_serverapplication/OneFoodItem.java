package com.example.nitantsood.buyer_serverapplication;

import android.app.Application;
import android.widget.Toast;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.io.Serializable;

/**
 * Created by NITANT SOOD on 23-10-2017.
 */

public class OneFoodItem implements Serializable {
    @JsonIgnore
    private String food_UID;

    public String getFood_UID() {
        return food_UID;
    }

    public void setFood_UID(String food_UID) {
        this.food_UID = food_UID;
    }
//    @JsonProperty("Buyer_UID")
    private String buyer_UID="none";
//    @JsonProperty("Buyer_lat")
    private Double buyer_lat=0.0;
//    @JsonProperty("Buyer_lng")
    private Double buyer_lng=0.0;
//    @JsonProperty("Category")
    private String category="none";
//    @JsonProperty("Delivered")
    private int delivered=0;
//    @JsonProperty("Ordered")
    private int ordered=0;
//    @JsonProperty("Description")
    private String description="none";
//    @JsonProperty("Expiry_date_time")
    private String expiry_date_time="none";
//    @JsonProperty("Picture_URL")
    private String picture_URL="none";
//    @JsonProperty("Price")
    private String price="none";
//    @JsonProperty("Quantity")
    private String quantity="none";
//    @JsonProperty("Rating")
    private String rating="none";
//    @JsonProperty("Review_comments")
    private String review_comments="none";
//    @JsonProperty("Seller_UID")
    private String seller_UID="none";
//    @JsonProperty("Seller_lat")
    private Double seller_lat=0.0;
//    @JsonProperty("Seller_lng")
    private Double seller_lng=0.0;
//    @JsonProperty("Title")
    private String title="none";
    @JsonIgnore
    private String distance="";
    public OneFoodItem(){}

    public String getBuyer_UID() {
        return buyer_UID;
    }

    public void setBuyer_UID(String buyer_UID) {
        this.buyer_UID = buyer_UID;
    }

    public Double getBuyer_lat() {
        return buyer_lat;
    }

    public void setBuyer_lat(Double buyer_lat) {
        this.buyer_lat = buyer_lat;
    }

    public Double getBuyer_lng() {
        return buyer_lng;
    }

    public void setBuyer_lng(Double buyer_lng) {
        this.buyer_lng = buyer_lng;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public int getDelivered() {
        return delivered;
    }

    public void setDelivered(int delivered) {
        this.delivered = delivered;
    }

    public int getOrdered() {
        return ordered;
    }

    public void setOrdered(int ordered) {
        this.ordered = ordered;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getExpiry_date_time() {
        return expiry_date_time;
    }

    public void setExpiry_date_time(String expiry_date_time) {
        this.expiry_date_time = expiry_date_time;
    }

    public String getPicture_URL() {
        return picture_URL;
    }

    public void setPicture_URL(String picture_URL) {
        this.picture_URL = picture_URL;
    }

    public String getPrice() {
        return price;
    }

    public void setPrice(String price) {
        this.price = price;
    }

    public String getQuantity() {
        return quantity;
    }

    public void setQuantity(String quantity) {
        this.quantity = quantity;
    }

    public String getRating() {
        return rating;
    }

    public void setRating(String rating) {
        this.rating = rating;
    }

    public String getReview_comments() {
        return review_comments;
    }

    public void setReview_comments(String review_comments) {
        this.review_comments = review_comments;
    }

    public String getSeller_UID() {
        return seller_UID;
    }

    public void setSeller_UID(String seller_UID) {
        this.seller_UID = seller_UID;
    }

    public Double getSeller_lat() {
        return seller_lat;
    }

    public void setSeller_lat(Double seller_lat) {
        this.seller_lat = seller_lat;
    }

    public Double getSeller_lng() {
        return seller_lng;
    }

    public void setSeller_lng(Double seller_lng) {
        this.seller_lng = seller_lng;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDistance() {
        return distance;
    }

    public void setDistance(String distance) {
        this.distance = distance;
    }
}
