package com.example.prethesispractice.entities;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class Operation {
    @SerializedName("operationId")
    @Expose
    private Integer operationId;
    @SerializedName("name")
    @Expose
    private String name;
    @SerializedName("date")
    @Expose
    private String date;
    @SerializedName("counteragentLead")
    @Expose
    private int counteragentLead;
    @SerializedName("counteragentSecondary")
    @Expose
    private Integer counteragentSecondary;
    @SerializedName("estateObjectId")
    @Expose
    private Integer estateObjectId;
    @SerializedName("hostId")
    @Expose
    private int hostId;
    private Employee host;
    @SerializedName("status")
    @Expose
    private String status;
    @SerializedName("actType")
    @Expose
    private String actType;
    @SerializedName("fkOperationSecondary")
    @Expose
    private Integer fkOperationSecondary;
    @SerializedName("fkOperationLead")
    @Expose
    private Integer fkOperationLead;

    public Operation() {

    }

    public Operation(Integer operationId, String name, String date, int counteragentLead, Integer counteragentSecondary,
                     Integer estateObjectId, int hostId, Employee host, String status, String actType,
                     Integer fkOperationLead, Integer fkOperationSecondary) {
        if (name == null || name.isBlank()) {
            throw new IllegalArgumentException("Operation name string is either empty or null");
        } else if (date == null || date.isBlank()) {
            throw new IllegalArgumentException("Operation date is either empty or null");
        } else if (hostId < 1) {
            throw new IllegalArgumentException("Host ID can't be less than 1");
        } else if (estateObjectId != null && estateObjectId < 1) {
            throw new IllegalArgumentException("Estate object ID can't be less than 1");
        } else if (status == null || status.isBlank()) {
            throw new IllegalArgumentException("Operation status string is either empty or null");
        } else if (operationId != null && operationId < 1) {
            throw new IllegalArgumentException("Operation ID can't be less than 1");
        } else if (counteragentLead < 1) {
            throw new IllegalArgumentException("Operation lead counteragent ID can't be less than 1");
        } else if (counteragentSecondary != null && counteragentSecondary < 1) {
            throw new IllegalArgumentException("Operation secondary counteragent ID can't be less than 1");
        } else if (fkOperationLead != null && fkOperationLead < 1) {
            throw new IllegalArgumentException("Operation lead operation ID can't be less than 1");
        } else if (fkOperationSecondary != null && fkOperationSecondary < 1) {
            throw new IllegalArgumentException("Operation lead operation ID can't be less than 1");
        }

        this.operationId = operationId;
        this.name = name;
        this.date = date;
        this.counteragentLead = counteragentLead;
        this.counteragentSecondary = counteragentSecondary;
        this.estateObjectId = estateObjectId;
        this.hostId = hostId;
        this.host = host;
        this.status = status;
        this.actType = actType;
        this.fkOperationLead = fkOperationLead;
        this.fkOperationSecondary = fkOperationSecondary;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        if (name == null || name.isBlank()) {
            throw new IllegalArgumentException("Operation name string is either empty or null");
        }

        this.name = name;
    }

    public String getDate() {
        return date;
    }

    public String getDateFormatted() {
        LocalDateTime dateToFormat = LocalDateTime.parse(date, DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"));
        return dateToFormat.format(DateTimeFormatter.ofPattern("dd.MM.yyyy HH:mm:ss"));
    }

    public void setDate(String date) {
        if (date == null || date.isBlank()) {
            throw new IllegalArgumentException("Operation date is either empty or null");
        }

        this.date = date;
    }

    public Integer getEstateObjectId() {
        return estateObjectId;
    }

    public void setEstateObjectId(Integer estateObjectId) {
        if (estateObjectId < 1) {
            throw new IllegalArgumentException("Estate object ID can't be less than 1");
        }

        this.estateObjectId = estateObjectId;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        if (status == null || status.isBlank()) {
            throw new IllegalArgumentException("Operation status string is either empty or null");
        }

        this.status = status;
    }

    public Integer getOperationId() {
        return operationId;
    }

    public void setOperationId(Integer operationId) {
        if (operationId != null && operationId < 1) {
            throw new IllegalArgumentException("Operation ID can't be less than 1");
        }

        this.operationId = operationId;
    }

    public int getCounteragentLead() {
        return counteragentLead;
    }

    public void setCounteragentLead(int counteragentLead) {
        if (counteragentLead < 1) {
            throw new IllegalArgumentException("Operation lead counteragent ID can't be less than 1");
        }

        this.counteragentLead = counteragentLead;
    }

    public Integer getCounteragentSecondary() {
        return counteragentSecondary;
    }

    public void setCounteragentSecondary(Integer counteragentSecondary) {
        if (counteragentSecondary != null && counteragentSecondary < 1) {
            throw new IllegalArgumentException("Operation secondary counteragent ID can't be less than 1");
        }

        this.counteragentSecondary = counteragentSecondary;
    }

    public int getHostId() {
        return hostId;
    }

    public void setHostId(int hostId) {
        if (hostId < 1) {
            throw new IllegalArgumentException("Host ID can't be less than 1");
        }

        this.hostId = hostId;
    }

    public Employee getHost() {
        return host;
    }

    public void setHost(Employee host) {
        this.host = host;
    }

    public String getActType() {
        return actType;
    }

    public void setActType(String actType) {
        this.actType = actType;
    }

    public Integer getFkOperationSecondary() {
        return fkOperationSecondary;
    }

    public void setFkOperationSecondary(Integer fkOperationSecondary) {
        if (fkOperationLead != null && fkOperationLead < 1) {
            throw new IllegalArgumentException("Operation lead operation ID can't be less than 1");
        }

        this.fkOperationSecondary = fkOperationSecondary;
    }

    public Integer getFkOperationLead() {
        return fkOperationLead;
    }

    public void setFkOperationLead(Integer fkOperationLead) {
        if (fkOperationSecondary != null && fkOperationSecondary < 1) {
            throw new IllegalArgumentException("Operation lead operation ID can't be less than 1");
        }

        this.fkOperationLead = fkOperationLead;
    }
}
