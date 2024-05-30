package com.example.prethesispractice.entities;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class ClientsStatus {
    @SerializedName("status")
    @Expose
    private String status;

    public ClientsStatus() {

    }

    public ClientsStatus(String status) {
        this.status = status;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }
}
