package com.example.prethesispractice.models;

public class RegistrationModel {
    private String login;
    private byte[] password;
    private String role;
    private int employeeId;

    public RegistrationModel() {

    }

    public RegistrationModel(String login, byte[] password, String role, int employeeId) {
        this.login = login;
        this.password = password;
        this.role = role;
        this.employeeId = employeeId;
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
