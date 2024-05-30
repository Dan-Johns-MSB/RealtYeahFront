package com.example.prethesispractice.models;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class AuthenticationModel {
    @SerializedName("Login")
    @Expose
    private String login;
    @SerializedName("Password")
    @Expose
    private String password;

    public AuthenticationModel() {

    }

    public AuthenticationModel(String login, String password) {
        this.login = login;
        this.password = password;
    }

    public String getLogin() {
        return login;
    }

    public void setLogin(String login) {
        this.login = login;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }
}
