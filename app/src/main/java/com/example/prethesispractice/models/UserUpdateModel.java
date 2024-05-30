package com.example.prethesispractice.models;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class UserUpdateModel {
    @SerializedName("userId")
    @Expose
    private Integer userId;
    @SerializedName("login")
    @Expose
    private String login;
    @SerializedName("password")
    @Expose
    private byte[] password;
    @SerializedName("role")
    @Expose
    private String role;
    @SerializedName("employeeId")
    @Expose
    private int employeeId;

    public UserUpdateModel() {

    }

    public UserUpdateModel(Integer userId, String login, byte[] password, String role, int employeeId) {
        this.userId = userId;
        this.login = login;
        this.password = password;
        this.role = role;
        this.employeeId = employeeId;
    }

    public Integer getUserId() {
        return userId;
    }

    public void setUserId(Integer userId) {
        this.userId = userId;
    }

    public String getLogin() {
        return login;
    }

    public void setLogin(String login) {
        this.login = login;
    }

    public byte[] getPassword() {
        return password;
    }

    public void setPassword(byte[] password) {
        this.password = password;
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
}
