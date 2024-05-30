package com.example.prethesispractice.entities;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

public class ObjectsStatus {
    @SerializedName("status")
    @Expose
    private String status;

    public ObjectsStatus() {

    }

    public ObjectsStatus(String status) {
        this.status = status;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }
}
