package com.example.prethesispractice.entities;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class EstateObject {
    @SerializedName("estateObjectId")
    @Expose
    private Integer estateObjectId;
    @SerializedName("photo")
    @Expose
    private String photo;
    @SerializedName("address")
    @Expose
    private String address;
    @SerializedName("type")
    @Expose
    private String type;
    @SerializedName("area")
    @Expose
    private double area;
    @SerializedName("price")
    @Expose
    private double price;
    @SerializedName("status")
    @Expose
    private String status;

    public EstateObject() {

    }

    public EstateObject(Integer estateObjectId, String photo, String address, String type, double area, double price, String status) {
        if (estateObjectId != null && estateObjectId < 1) {
            throw new IllegalArgumentException("Estate object ID can't be less than 1");
        } else if (photo == null || photo.isBlank()) {
            throw new IllegalArgumentException("Negative or zero integer as resource ID");
        } else if (address == null || address.isBlank()) {
            throw new IllegalArgumentException("Object address string is either empty or null");
        } else if (type == null || type.isBlank()) {
            throw new IllegalArgumentException("Object type string is either empty or null");
        } else if (area <= 0) {
            throw new IllegalArgumentException("Object's area can't be negative or equal to zero");
        } else if (price < 0) {
            throw new IllegalArgumentException("Object's price can't be negative");
        }

        this.estateObjectId = estateObjectId;
        this.photo = photo;
        this.address = address;
        this.type = type;
        this.area = area;
        this.price = price;
        this.status = status;
    }

    public Integer getEstateObjectId() {
        return estateObjectId;
    }

    public void setEstateObjectId(Integer estateObjectId) {
        if (estateObjectId != null && estateObjectId < 1) {
            throw new IllegalArgumentException("Estate object ID can't be less than 1");
        }

        this.estateObjectId = estateObjectId;
    }

    public String getPhoto() {
        return photo;
    }

    public void setPhoto(String photo) {
        if (photo == null || photo.isBlank()) {
            throw new IllegalArgumentException("Negative or zero integer as resource ID");
        }

        this.photo = photo;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        if (address == null || address.isBlank()) {
            throw new IllegalArgumentException("Object address string is either empty or null");
        }

        this.address = address;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        if (type == null || type.isBlank()) {
            throw new IllegalArgumentException("Object type string is either empty or null");
        }

        this.type = type;
    }

    public double getArea() {
        return area;
    }

    public void setArea(double area) {
        if (area <= 0) {
            throw new IllegalArgumentException("Object's area can't be negative or equal to zero");
        }

        this.area = area;
    }

    public double getPrice() {
        return price;
    }

    public void setPrice(double price) {
        if (price < 0) {
            throw new IllegalArgumentException("Object's price can't be negative");
        }

        this.price = price;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }
}