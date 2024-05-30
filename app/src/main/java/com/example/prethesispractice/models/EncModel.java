package com.example.prethesispractice.models;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class EncModel {
    @SerializedName("encModel")
    @Expose
    String encModel;

    public EncModel() {

    }

    public EncModel(String encModel) {
        this.encModel = encModel;
    }

    public String getEncModel() {
        return encModel;
    }

    public void setEncModel(String encModel) {
        this.encModel = encModel;
    }
}
