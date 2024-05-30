package com.example.prethesispractice.entities;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class Employee {
    @SerializedName("employeeId")
    @Expose
    private Integer employeeId;
    @SerializedName("name")
    @Expose
    private String name;
    @SerializedName("phoneNumber")
    @Expose
    private String phoneNumber;
    @SerializedName("job")
    @Expose
    private String job;
    @SerializedName("birthdate")
    @Expose
    private String birthdate;
    @SerializedName("address")
    @Expose
    private String address;
    @SerializedName("hiredate")
    @Expose
    private String hiredate;
    @SerializedName("promotedate")
    @Expose
    private String promotedate;
    @SerializedName("firedate")
    @Expose
    private String firedate;
    @SerializedName("status")
    @Expose
    private String status;
    @SerializedName("photo")
    @Expose
    private String photo;

    public Employee() {

    }

    public Employee(Integer employeeId, String photo, String name, String job, String phoneNumber,
                    String birthdate, String address, String hiredate, String promotedate,
                    String firedate, String status) {

        if (photo == null || photo.isBlank()) {
            throw new IllegalArgumentException("Photo string is either empty or null");
        } else if (name == null || name.isBlank()) {
            throw new IllegalArgumentException("Employee name string is either empty or null");
        } else if (job == null || job.isBlank()) {
            throw new IllegalArgumentException("Job string is either empty or null");
        } else if (phoneNumber == null || phoneNumber.isBlank()) {
            throw new IllegalArgumentException("Phone number is either empty or null");
        } else if (birthdate == null || birthdate.isBlank()) {
            throw new IllegalArgumentException("Birthdate string is either empty or null");
        } else if (address == null || address.isBlank()) {
            throw new IllegalArgumentException("Address string is either empty or null");
        } else if (hiredate == null || hiredate.isBlank()) {
            throw new IllegalArgumentException("Hiredate string is either empty or null");
        } else if (promotedate == null || promotedate.isBlank()) {
            throw new IllegalArgumentException("Promotedate string is either empty or null");
        } else if (status == null || status.isBlank()) {
            throw new IllegalArgumentException("Status string is either empty or null");
        } else if (employeeId != null && employeeId < 1) {
            throw new IllegalArgumentException("Employee ID can't be less than 1");
        }

        this.employeeId = employeeId;
        this.photo = photo;
        this.name = name;
        this.job = job;
        this.phoneNumber = phoneNumber;
        this.birthdate = birthdate;
        this.address = address;
        this.hiredate = hiredate;
        this.promotedate = promotedate;
        this.status = status;

        if (firedate != null && !firedate.isBlank()) {
            this.firedate = firedate;
        }
    }

    public String getPhoto() {
        return photo;
    }

    public void setPhoto(String photo) {
        if (photo == null || photo.isBlank()) {
            throw new IllegalArgumentException("Negative or zero integer as resource ID");
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

    public String getJob() {
        return job;
    }

    public void setJob(String job) {
        if (job == null || job.isBlank()) {
            throw new IllegalArgumentException("Employee name string is either empty or null");
        }

        this.job = job;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) {
        if (phoneNumber == null || phoneNumber.isBlank()) {
            throw new IllegalArgumentException("Phone number string is either empty or null");
        }

        this.phoneNumber = phoneNumber;
    }

    public Integer getEmployeeId() {
        return employeeId;
    }

    public void setEmployeeId(Integer employeeId) {
        if (employeeId != null && employeeId < 1) {
            throw new IllegalArgumentException("Employee ID can't be less than 1");
        }

        this.employeeId = employeeId;
    }

    public String getBirthdate() {
        return birthdate;
    }

    public void setBirthdate(String birthdate) {
        if (birthdate == null || birthdate.isBlank()) {
            throw new IllegalArgumentException("Birthdate string is either empty or null");
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

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getHiredate() {
        return hiredate;
    }

    public void setHiredate(String hiredate) {
        if (hiredate == null || hiredate.isBlank()) {
            throw new IllegalArgumentException("Hiredate string is either empty or null");
        }

        this.hiredate = hiredate;
    }

    public String getHiredateDateOnly() {
        LocalDateTime date = LocalDateTime.parse(hiredate, DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"));
        return date.format(DateTimeFormatter.ofPattern("yyyy-MM-dd"));
    }

    public String getHiredateFormatted() {
        LocalDateTime date = LocalDateTime.parse(hiredate, DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"));
        return date.format(DateTimeFormatter.ofPattern("dd.MM.yyyy"));
    }

    public String getPromotedate() {
        return promotedate;
    }

    public void setPromotedate(String promotedate) {
        if (promotedate == null || promotedate.isBlank()) {
            throw new IllegalArgumentException("Promotedate string is either empty or null");
        }

        this.promotedate = promotedate;
    }

    public String getPromotedateDateOnly() {
        LocalDateTime date = LocalDateTime.parse(promotedate, DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"));
        return date.format(DateTimeFormatter.ofPattern("yyyy-MM-dd"));
    }

    public String getPromotedateFormatted() {
        LocalDateTime date = LocalDateTime.parse(promotedate, DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"));
        return date.format(DateTimeFormatter.ofPattern("dd.MM.yyyy"));
    }

    public String getFiredate() {
        return firedate;
    }

    public void setFiredate(String firedate) {
        if (firedate != null && !firedate.isBlank()) {
            this.firedate = firedate;
        } else {
            this.firedate = "Not fired";
        }
    }

    public String getFiredateDateOnly() {
        if (this.firedate != null && !this.firedate.equals("Not fired")) {
            LocalDateTime date = LocalDateTime.parse(firedate, DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"));
            return date.format(DateTimeFormatter.ofPattern("yyyy-MM-dd"));
        }

        return firedate;
    }

    public String getFiredateFormatted() {
        if (!this.firedate.equals("Not fired")) {
            LocalDateTime date = LocalDateTime.parse(firedate, DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"));
            return date.format(DateTimeFormatter.ofPattern("dd.MM.yyyy"));
        }

        return firedate;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }
}
