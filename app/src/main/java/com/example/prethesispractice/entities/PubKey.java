package com.example.prethesispractice.entities;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class PubKey {
    @SerializedName("pubKey")
    @Expose
    private String pubKey;

    public PubKey() {

    }

    public PubKey(String pubKey) {
        this.pubKey = pubKey;
    }

    public String getPubKey() {
        return pubKey;
    }

    public void setPubKey(String pubKey) {
        this.pubKey = pubKey;
    }
}