package com.example.prethesispractice.entities;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

public class OperationsStatus {

    @SerializedName("status")
    @Expose
    private String status;

    public OperationsStatus() {

    }

    public OperationsStatus(String status) {
        this.status = status;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }
}
