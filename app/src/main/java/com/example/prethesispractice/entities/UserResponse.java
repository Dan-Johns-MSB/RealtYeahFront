package com.example.prethesispractice.entities;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class UserResponse {
    @SerializedName("login")
    @Expose
    private String login;
    @SerializedName("role")
    @Expose
    private String role;
    @SerializedName("employeeId")
    @Expose
    private int employeeId;
    @SerializedName("token")
    @Expose
    private String token;

    public UserResponse() {

    }

    public UserResponse(String login, String role, int employeeId, String token) {
        this.login = login;
        this.role = role;
        this.employeeId = employeeId;
        this.token = token;
    }

    public String getLogin() {
        return login;
    }

    public void setLogin(String login) {
        this.login = login;
    }

    public String getRole() {
        return role;
    }

    public void setRole(String role) {
        this.role = role;
    }

    public int getEmployeeId() {
        return employeeId;
    }

    public void setEmployeeId(int employeeId) {
        this.employeeId = employeeId;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }
}
