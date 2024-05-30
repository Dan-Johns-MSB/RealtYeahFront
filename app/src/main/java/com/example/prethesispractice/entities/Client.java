package com.example.prethesispractice.entities;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class Client {
    @SerializedName("clientId")
    @Expose
    private Integer clientId;
    @SerializedName("passportNumber")
    @Expose
    private String passportNumber;
    @SerializedName("taxpayerNumber")
    @Expose
    private String taxpayerNumber;
    @SerializedName("name")
    @Expose
    private String name;
    @SerializedName("phoneNumber")
    @Expose
    private String phoneNumber;
    @SerializedName("email")
    @Expose
    private String email;
    @SerializedName("birthdate")
    @Expose
    private String birthdate;
    @SerializedName("birthplace")
    @Expose
    private String birthplace;
    @SerializedName("gender")
    @Expose
    private String gender;
    @SerializedName("photo")
    @Expose
    private String photo;

    public Client() {

    }

    public Client(Integer clientId, String passportNumber, String taxpayerNumber, String name, String phoneNumber,
                  String email, String birthdate, String birthplace, String gender, String photo) {
        if (clientId != null && clientId < 1) {
            throw new IllegalArgumentException("Client ID can't be less then 1");
        } else if (photo == null || photo.isBlank()) {
            throw new IllegalArgumentException("Photo string is either empty or null");
        } else if (name == null || name.isBlank()) {
            throw new IllegalArgumentException("Name string is either empty or null");
        } else if (passportNumber == null || passportNumber.isBlank()) {
            throw new IllegalArgumentException("Passport string is either empty or null");
        } else if (taxpayerNumber == null || taxpayerNumber.isBlank()) {
            throw new IllegalArgumentException("Taxpayer number is null");
        } else if (phoneNumber == null || taxpayerNumber.isBlank()) {
            throw new IllegalArgumentException("Phone number is null");
        } else if (email == null || email.isBlank()) {
            throw new IllegalArgumentException("Email string is either empty or null");
        } else if (birthdate == null || birthdate.isBlank()) {
            throw new IllegalArgumentException("Birthdate is null");
        } else if (birthplace == null || birthplace.isBlank()) {
            throw new IllegalArgumentException("Birthplace string is either empty or null");
        } else if (gender == null || gender.isBlank()) {
            throw new IllegalArgumentException("Gender string is either empty or null");
        }

        this.clientId = clientId;
        this.passportNumber = passportNumber;
        this.taxpayerNumber = taxpayerNumber;
        this.name = name;
        this.phoneNumber = phoneNumber;
        this.email = email;
        this.birthdate = birthdate;
        this.birthplace = birthplace;
        this.gender = gender;
        this.photo = photo;
    }

    public Integer getClientId() {
        return clientId;
    }

    public void setClientId(Integer clientId) {
        if (clientId != null && clientId < 1) {
            throw new IllegalArgumentException("Client ID can't be less then 1");
        }

        this.clientId = clientId;
    }

    public String getPhoto() {
        return photo;
    }

    public void setPhoto(String photo) {
        if (photo == null || photo.isBlank()) {
            throw new IllegalArgumentException("Photo string is either empty or null");
        }

        this.photo = photo;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        if (name == null || name.isBlank()) {
            throw new IllegalArgumentException("Name string is either empty or null");
        }

        this.name = name;
    }

    public String getPassportNumber() {
        return passportNumber;
    }

    public void setPassportNumber(String passportNumber) {
        if (passportNumber == null || passportNumber.isBlank()) {
            throw new IllegalArgumentException("Passport string is either empty or null");
        }

        this.passportNumber = passportNumber;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) {
        if (phoneNumber == null || phoneNumber.isBlank()) {
            throw new IllegalArgumentException("Phone number is null");
        }

        this.phoneNumber = phoneNumber;
    }

    public String getTaxpayerNumber() {
        return taxpayerNumber;
    }

    public void setTaxpayerNumber(String taxpayerNumber) {
        if (taxpayerNumber == null || taxpayerNumber.isBlank()) {
            throw new IllegalArgumentException("Taxpayer number is null");
        }

        this.taxpayerNumber = taxpayerNumber;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        if (email == null || email.isBlank()) {
            throw new IllegalArgumentException("Email string is either empty or null");
        }

        this.email = email;
    }

    public String getBirthdate() {
        return birthdate;
    }

    public void setBirthdate(String birthdate) {
        if (birthdate == null || birthdate.isBlank()) {
            throw new IllegalArgumentException("Birthdate is null or empty");
        }

        this.birthdate = birthdate;
    }

    public String getBirthdateDateOnly() {
        LocalDateTime date = LocalDateTime.parse(birthdate, DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"));
        return date.format(DateTimeFormatter.ofPattern("yyyy-MM-dd"));
    }

    public String getBirthdateFormatted() {
        LocalDateTime date = LocalDateTime.parse(birthdate, DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"));
        return date.format(DateTimeFormatter.ofPattern("dd.MM.yyyy"));
    }

    public String getBirthplace() {
        return birthplace;
    }

    public void setBirthplace(String birthplace) {
        if (birthplace == null || birthplace.isBlank()) {
            throw new IllegalArgumentException("Birthplace string is either empty or null");
        }

        this.birthplace = birthplace;
    }

    public String getGender() {
        return gender;
    }

    public void setGender(String gender) {
        if (gender == null || gender.isBlank()) {
            throw new IllegalArgumentException("Gender string is either empty or null");
        }

        this.gender = gender;
    }
}
